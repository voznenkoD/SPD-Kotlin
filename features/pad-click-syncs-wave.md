## Feature: Pad Click Syncs Wave List

### Summary
When the user clicks a pad's main or sub area, the referenced wave is highlighted and scrolled into view in the right-hand wave list (auto-switching to the Waves tab if needed). Also cleans up the pad view labels: drops the `Main:` / `Sub:` prefixes and shows the wave **name** instead of its numeric ID.

### Acceptance Criteria

**Pad → Wave list sync**
- [ ] Clicking a pad's main area sets `selectedWave` to the wave referenced by `pad.main.wave`; the corresponding row in the wave list is visually highlighted in the **same style** that clicking a row directly in the list applies. Both user-entry paths must end up in the same visual state.
- [ ] Clicking a pad's sub area does the same for `pad.sub.wave`.
- [ ] Highlighting works in all three wave-sort modes (BY_NAME, BY_CATEGORY_NAME, BY_CATEGORY_NUM).
- [ ] In the BY_CATEGORY views, if the selected wave belongs to a currently-collapsed category, that category auto-expands so the row is visible.
- [ ] The `LazyColumn` **smoothly** scrolls (`animateScrollToItem`) the selected row into view when the selection changes because of a pad click.
- [ ] If the Kits tab (tab 0) is currently active when the user clicks a pad, the Waves tab (tab 1) **auto-switches** into focus so the highlighted row is actually visible.
- [ ] Clicking a pad whose `main.wave == 0` or `sub.wave == 0` (empty slot) **clears** `selectedWave` (the right-hand wave detail panel empties), but the wave list itself remains visible with nothing highlighted.
- [ ] Clicking a row in the wave list continues to work as before — the row highlights, the detail panel fills in, and pad selection state is not affected.

**Pad view label cleanup**
- [ ] The `Main:` and `Sub:` text prefixes are removed from `PadItem` — only the wave value is shown in each half.
- [ ] Instead of the numeric wave ID, each half shows the wave **name** (looked up via `device.waves.find { it.number == n }?.name`).
- [ ] Empty slots (`wave == 0`) display `"----"`.
- [ ] Orphaned references (wave number > 0 but no matching wave in `device.waves`) also display `"----"` — treated identically to empty slots. This case should not occur in practice.
- [ ] Label font sizing, color, bold-on-select styling, and the existing red/yellow divider between main and sub are preserved.

### Affected Layers
- **Model:** No changes.
- **DeviceManager:** No changes.
- **ViewModel:** `MainViewModel`
  - Update `selectPad` so that when the referenced `main.wave` / `sub.wave` is `0` (or not found in `device.waves`), `_selectedWave` is **cleared** (`.value = null`) rather than left untouched.
  - Update `selectPad` so that on every pad click it also sets the lists-tab state to the Waves tab (index `1`).
  - Lift the lists `selectedTab` out of `ListsScreen`'s local `remember { mutableStateOf }` and into a new `MainViewModel` `StateFlow<Int> listsSelectedTab` with `selectListsTab(index: Int)` setter, so `selectPad` can drive it.
- **Components:**
  - `ListsScreen.kt`:
    - Replace the local `selectedTab` state with a `selectedTab: Int` + `onSelectedTabChange: (Int) -> Unit` prop pair wired to the VM.
    - Add a `selectedWaveNumber: Int?` prop and thread it into `WaveListByName` and `WaveListByCategory` → `WaveListItem`.
  - `WaveListItem` — accept `isHighlighted: Boolean` and apply a background highlight when true. This same highlight must fire when the user clicks a row directly, so the "click in list" path should also flow through `selectedWaveNumber` state (route via `MainViewModel.selectWave` → the existing `selectedWave` flow → prop → highlight).
  - `WaveListByName` — hold a `rememberLazyListState()`; `LaunchedEffect(selectedWaveNumber)` looks up the index of the matching row and calls `animateScrollToItem(index)` when found.
  - `WaveListByCategory` — same scroll effect, plus: when `selectedWaveNumber` changes and the matching category is currently collapsed (`expandedCategories[category.name] != true`), set it to `true` before computing the scroll target so the item is actually laid out.
  - `KitPadsScreen.PadItem` — accept a `waveNameLookup: (Int) -> String?` lambda. Replace `"Main: ${pad.main.wave}"` and `"Sub: ${pad.sub.wave}"` with `waveNameLookup(pad.main.wave) ?: "----"` and the sub equivalent. The lookup must return `null` for `wave == 0` or for a number not present in `device.waves`, so both cases naturally render `"----"`.
  - Thread `waveNameLookup` through `KitPadsScreen` and `PadScreen`.
- **Screen:** `MainScreen.kt`
  - Collect `listsSelectedTab` from the VM and pass it + `mainViewModel::selectListsTab` into `ListsScreen`.
  - Pass `selectedWaveNumber = selectedWave?.number` into `ListsScreen`.
  - Build `val waveNameLookup: (Int) -> String? = remember(waves) { val byNumber = waves.associateBy { it.number }; { n -> byNumber[n]?.name } }` and thread it into `PadScreen`.
- **Navigation:** No new screen.

### Mapping Reference
`Map_Pad.md` — `Main` / `Sub` sound wave references. `Map_System.md` — wave list structure (wave → number → name mapping).

### Key Design Decisions (confirmed by user)
- **Tab auto-switch:** Yes — clicking a pad while on the Kits tab auto-switches to the Waves tab.
- **Orphan / empty handling:** Both empty (`wave == 0`) and unresolved references render as `"----"`. Orphans are not expected in practice.
- **Scroll behavior:** Smooth — `animateScrollToItem`.
- **Highlight style:** Same visual highlight as when the user clicks a wave row directly in the list. Both entry paths must converge on identical visual state.
- **Empty-slot click:** Clears `selectedWave` (right-hand detail panel empties) while keeping the wave list visible with nothing highlighted.
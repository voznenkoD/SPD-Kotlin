## Test Plan: Pad Click Syncs Wave List

Covers the feature specified in `features/pad-click-syncs-wave.md`. The change has three facets: (1) `MainViewModel.selectPad` coordination state, (2) `ListsScreen` / wave-list highlight + scroll + auto-expand, (3) `PadItem` label cleanup + `waveNameLookup` rendering.

---

### 1. Unit: `MainViewModel.selectPad`

No persistence needed — construct `MainViewModel(DeviceManager())` against a tiny fixture device, call `selectPad`, read `StateFlow.value`.

- **T1.1 main click with valid wave** — pad's `main.wave = 5`, wave #5 exists. Expected: `selectedWave.value?.number == 5`, `isMainSelected.value == true`, `listsSelectedTab.value == 1`, `selectedPadNumber.value == the clicked pad`.

- **T1.2 sub click with valid wave** — pad's `sub.wave = 7`. Expected: `selectedWave.value?.number == 7`, `isMainSelected.value == false`, `listsSelectedTab.value == 1`.

- **T1.3 main click on empty slot** — `main.wave == 0`. Expected: `selectedWave.value == null`, pad selection state still set, `listsSelectedTab.value == 1`.

- **T1.4 sub click on empty slot** — `sub.wave == 0`. Same as T1.3.

- **T1.5 main click on orphaned reference** — `main.wave == 42` but no wave with number 42 in `device.waves`. Expected: `selectedWave.value == null` (same fallback as empty).

- **T1.6 tab switch side-effect stands alone** — start with `listsSelectedTab.value == 0`, call `selectPad`. Expected: tab becomes `1` regardless of wave outcome (including empty slot).

- **T1.7 `selectListsTab`** — calling `selectListsTab(0)` sets the flow back to `0`; subsequent `selectPad` flips it to `1`.

- **T1.8 `selectWave` does not touch `listsSelectedTab`** — start on tab `0`, call `selectWave(...)`. Expected: tab stays `0` (only pad clicks auto-switch).

---

### 2. Unit: `waveNameLookup` behavior (MainScreen wiring)

This is a pure function; it's worth testing in isolation by extracting the lambda or by asserting through `PadItem` screenshot tests. A simpler unit-level check:

- **T2.1** — Given `waves = [Wave(1,"Kick"), Wave(2,"Snare")]`, lookup for `1` → `"Kick"`, for `2` → `"Snare"`, for `0` → `null`, for `99` (orphan) → `null`.

This can live alongside any helper tests or be asserted via a thin extract if needed.

---

### 3. Composable: `WaveListByName` scroll & highlight

These need `createComposeRule()` from `androidx.compose.ui.test.junit4`. Snapshot-style tests against the composable.

- **T3.1 highlight applied when `selectedWaveNumber` matches** — render with a list of 5 waves and `selectedWaveNumber = 3`. Assert the row for wave #3 has the highlight background (check via a test tag or semantics property you can add if needed).

- **T3.2 highlight not applied when selection doesn't match** — render with `selectedWaveNumber = 99`. No row highlighted.

- **T3.3 scrolls selected into view on initial composition** — 200 waves, `selectedWaveNumber = 150`. Assert `listState.firstVisibleItemIndex` ends up near 150 (allow +/- a few due to viewport size).

- **T3.4 no scroll when already visible** — scroll to the top, set `selectedWaveNumber = 2` (which is already visible). Assert no scroll happens (`firstVisibleItemIndex == 0`).

- **T3.5 smooth scroll on prop change** — mutate `selectedWaveNumber` from `2` to `180`. Assert the list ends up showing #180. (animate vs instant is hard to verify without timing; `advanceUntilIdle` on a test clock is enough.)

---

### 4. Composable: `WaveListByCategory` auto-expand + scroll

- **T4.1 auto-expand containing category** — categories: `Default` (expanded), `Drums` (collapsed). `selectedWaveNumber` points to a wave in `Drums`. After recomposition, `expandedCategories["Drums"] == true`. The row's highlight background is set.

- **T4.2 already-expanded category is not collapsed** — selecting a wave already in an expanded category should leave other categories' expansion state untouched.

- **T4.3 selection not in any category (orphan ref)** — `selectedWaveNumber = 999`, not present. No category should be newly expanded, no scroll.

- **T4.4 correct target index math** — categories `[A(2 waves), B(3 waves), C(1 wave)]`, all expanded. The first wave of `C` is at LazyList index `2 + 1 + 3 + 1 = 7` (A header + 2 A waves + B header + 3 B waves + C header + 0 offset). A bug in header-count arithmetic off-by-one would show up as scrolling to the wrong item — use a visible-items assertion.

- **T4.5 correct index when middle category collapsed** — `A` expanded, `B` collapsed, `C` expanded. Target is first wave of `C`. Expected index: `1 (A header) + 2 (A waves) + 1 (B header, but no waves because collapsed) + 1 (C header) = 5`, then + 0 for first C wave = `5`.

---

### 5. Composable: `ListsScreen` tab lifting

- **T5.1 prop-driven tab switching** — render `ListsScreen(selectedTab=0, ...)` then re-render with `selectedTab=1`. Assert that the wave list is displayed (not the kit list), proving the tab state is fully externally controlled.

- **T5.2 `onSelectedTabChange` fires on click** — render with `selectedTab=0`, click the Waves tab. Assert the callback receives `1`. The local `selectedTab` should not change unless the parent re-renders with the new value.

---

### 6. Composable: `PadItem` label rendering

- **T6.1 main/sub labels show wave names** — `pad.main.wave == 3`, `pad.sub.wave == 7`, `waveNameLookup = { n -> mapOf(3 to "Kick", 7 to "HiHat")[n] }`. Find text "Kick" and "HiHat" in the rendered pad; find no "Main:" / "Sub:" prefixes.

- **T6.2 empty slot renders "----"** — `pad.main.wave == 0`. Find text "----" in the main half.

- **T6.3 orphan reference renders "----"** — `pad.main.wave == 999`, lookup returns null. Find text "----".

- **T6.4 selection bold+white styling preserved** — `isSelected=true, isMainSelected=true`. The main half's text node has `FontWeight.Bold` and `Color.White`.

- **T6.5 Main: / Sub: strings are gone** — Search the rendered semantics tree for the literal strings "Main:" and "Sub:" — both should be absent.

---

### 7. Manual UI smoke tests

Run `./gradlew desktopRun` against a real device folder.

- **M7.1 labels** — Open a kit with mixed main/sub waves and empty slots. Pad labels show wave names (not numbers), empty slots show `----`, no "Main:" / "Sub:" prefix anywhere.

- **M7.2 pad-click sync, Kits tab active** — Start on Kits tab. Click pad A's main area. Waves tab auto-switches in; the wave list scrolls to and highlights the corresponding row. Detail panel updates with the wave info.

- **M7.3 pad-click sync, Waves tab active** — Already on Waves tab. Click pad B's sub area. List smoothly scrolls to and highlights the sub wave. No tab flicker.

- **M7.4 empty slot clears selection** — Click a pad half whose label reads `----`. Expected: the wave detail panel empties, no row is highlighted in the wave list, Waves tab is still visible.

- **M7.5 BY_CATEGORY auto-expand** — Switch sort mode to `By Category (Name)`. Collapse all categories except `Default`. Click a pad whose wave lives in a collapsed category. The category expands, the wave list scrolls into it, the row is highlighted.

- **M7.6 direct list click still works** — Click a wave row directly in the list. Same highlight treatment applies. Detail panel updates. Pad selection unaffected (selected pad still shows its original highlight state).

- **M7.7 sort-mode changes preserve highlight** — With a wave selected via pad click, toggle between BY_NAME, BY_CATEGORY_NAME, and BY_CATEGORY_NUM. Highlight follows the same wave in each view, scrolling into visibility each time.

- **M7.8 wave name updates reflect in pad labels** — Rename a wave (if possible via existing UI), return to kit view — pad labels pick up the new name immediately (thanks to `remember(waves)` key).

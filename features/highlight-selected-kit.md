## Feature: Highlight selected kit in the kit list

### Summary
Visually highlight the currently selected kit in the kit list (Main screen, right-side "Kits" tab), mirroring how the selected wave is marked in the wave list — a `ColorSurfaceSelected` background on the active row.

### Acceptance Criteria
- [ ] When a kit is selected (via the kit list, a pad, or any path that sets `MainViewModel.selectedKitIndex`), that kit's row in the **Kits** tab shows the selected-background highlight (`ColorSurfaceSelected`), matching the wave list's selected style.
- [ ] All non-selected kit rows keep the default `ColorSurface` background.
- [ ] Drag/drop visual states still take precedence while active: a row being dragged shows `ColorSurfaceHover`, a drop-target shows `ColorAccentYellow`; selection highlight applies only when no drag/drop state is active.
- [ ] The highlight updates reactively when the selection changes (selecting a different kit moves the highlight).
- [ ] Selected-row text remains legible on the highlighted background (match the wave list's text treatment).
- [ ] No regression to existing kit list behavior (click-to-select, copy/paste context menu, reordering).

### Affected Layers
- **Model:** none
- **DeviceManager:** none
- **ViewModel:** none — reuse existing `MainViewModel.selectedKitIndex: StateFlow<Int?>` (already collected in `MainScreen.kt:32`).
- **Components:**
  - `KitListView.kt` — add `selectedKitIndex: Int? = null` param; compute `isSelected = index == selectedKitIndex`; fold into the existing `when` background (priority: dragged → drop-target → selected → default).
  - `ListsScreen.kt` — add `selectedKitIndex: Int? = null` param and pass it to `KitListView`.
- **Screen:** `MainScreen.kt` — pass the already-collected `selectedKitIndex` into the `ListsScreen(...)` call (currently only `selectedWaveNumber` is passed).
- **Navigation:** no new screen.

### Mapping Reference
None — UI-only highlight change, no device parameters.

### Decisions
1. No auto-scroll to the selected kit for now — highlight only.
2. Mirror the wave list exactly, including its text-color treatment on the selected background.
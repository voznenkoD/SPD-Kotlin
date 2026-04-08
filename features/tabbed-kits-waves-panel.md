## Feature: Tabbed Kits & Waves Panel

### Summary
Replace the side-by-side kits/waves layout in `ListsScreen` with a single tabbed panel. This frees horizontal space so the detail and pad columns can be larger.

### Acceptance Criteria
- [ ] `ListsScreen` renders a `TabRow` with two tabs: "Kits" and "Waves"
- [ ] Selecting the "Kits" tab shows the existing `KitListView` (with drag-and-drop, copy/paste context menu)
- [ ] Selecting the "Waves" tab shows the existing wave list views (by name / by category name / by category num, with sorting context menu, usage indicators, and "Used in" context menu)
- [ ] The wave sorting mode context menu (right-click) remains functional on the Waves tab
- [ ] All existing callbacks continue working as before
- [ ] The previously side-by-side 0.45/0.55 column split is replaced by a single full-width tabbed panel
- [ ] Default tab on app start is "Kits"

### Affected Layers
- **Model:** No changes
- **DeviceManager:** No changes
- **ViewModel:** No changes
- **Components:** `ListsScreen.kt` — replace `Row` with `TabRow` + `when(selectedTab)` pattern. Reuse existing components.
- **Screen:** No changes to `MainScreen.kt`
- **Navigation:** No new screen needed
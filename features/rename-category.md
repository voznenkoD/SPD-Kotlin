## Feature: Rename Category

### Summary
Right-click on a category header in the wave list shows a context menu with "Rename Category…" option that opens a dialog to enter a new name. Updates the in-memory wave list holder; persists to `tag_list.spd` on save.

### Acceptance Criteria
- [ ] Right-clicking a category header (in BY_CATEGORY_NAME or BY_CATEGORY_NUM views) shows a context menu with "Rename Category…" entry
- [ ] Clicking "Rename Category…" opens a dialog with a text field pre-filled with the current category name and OK/Cancel buttons
- [ ] The new name is enforced to 12 characters max (TagPrm encoding limit)
- [ ] Empty names are rejected (OK disabled)
- [ ] Renaming to a name that already exists in the category list is blocked (OK disabled, with hint)
- [ ] Submitting the dialog replaces the `Category` key in both `wavesByNamePerCategory` and `wavesByNumPerCategory` with `Category(newName, sameOrder)` preserving wave lists
- [ ] The UI re-renders with the updated tag list — order remains tied to the existing Category.order field
- [ ] The expand/collapse state persists across the rename
- [ ] On save, the renamed category is written to `tag_list.spd` via existing `WaveListsHolder.toRaw()`

### Affected Layers
- **Model:** Add `WaveListsHolder.renameCategory(oldName, newName): WaveListsHolder` helper that returns a new holder with the renamed category key in both maps
- **DeviceManager:** Add `renameCategory(oldName, newName)` that calls the helper and reassigns `device.waveLists`
- **ViewModel:** `MainViewModel` exposes `renameCategory(oldName, newName)` that delegates to DeviceManager
- **Components:** `ListsScreen.kt` — wrap category header `Row` in a `ContextMenuArea`; add a rename `AlertDialog` with `TextField`; accept `onRenameCategory` callback
- **Screen:** `MainScreen.kt` — pass `mainViewModel::renameCategory` through to `ListsScreen`
- **Navigation:** No new screen

### Key Design Decisions (confirmed by user)
- Block rename if the new name already exists among categories
- No order editing in the dialog — Category.order is preserved; UI re-renders naturally based on the updated tag list
## Feature: Kit List Drag-and-Drop Reorder

### Summary
Allow users to reorder kits in the kit list panel by dragging and dropping items. This changes the in-memory kit order and persists it to disk (files are renamed to reflect new positions: `KIT_000.spd`, `KIT_001.spd`, etc.).

### Acceptance Criteria
- [ ] User can click-and-hold a kit item in the list, then drag it to a new position
- [ ] A visual indicator (e.g., highlight or insertion line) shows where the kit will be dropped
- [ ] On drop, the kit list reorders immediately in the UI
- [ ] The reorder is persisted to disk — kit files are renamed to match new indices
- [ ] If a kit is currently selected and gets moved, the selection follows it (selected kit stays selected)
- [ ] Drag-and-drop works within the existing `LazyColumn` in `KitListView`
- [ ] The entire kit row is draggable (no separate drag handle icon)
- [ ] Only the kit list supports drag-and-drop (not the wave list)

### Affected Layers
- **Model:** No changes needed — `List<Kit>` ordering is positional
- **DeviceManager:** Add `moveKit(fromIndex: Int, toIndex: Int)` method that reorders the kits list and saves to disk
- **ViewModel:** Add `moveKit(from, to)` in `MainViewModel` that delegates to `DeviceManager.moveKit()` and updates `selectedKitIndex` if needed
- **Components:** Modify `KitListView.kt` to support drag-and-drop reordering (Compose `detectDragGestures` or a reorderable lazy list approach)
- **Screen:** `ListsScreen.kt` — wire the new move callback through to `KitListView`
- **Navigation:** No new screen needed

### Mapping Reference
`Map_Kit.md` — kit ordering is file-index-based
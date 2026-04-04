## Feature: Kit Context Menu Copy/Paste

### Summary
Replace the static Copy/Paste/Init buttons at the bottom of the kit list with a right-click context menu on each kit item. Right-clicking a kit shows options to Copy and Paste.

### Acceptance Criteria
- [ ] Right-clicking a kit item in the list shows a context menu with "Copy" and "Paste" options
- [ ] "Copy" stores the clicked kit's data in an in-memory clipboard (ViewModel state)
- [ ] "Paste" is only enabled when a kit has been copied; it replaces the right-clicked kit's data with the copied kit's data
- [ ] The dedicated Copy/Paste/Init button row at the bottom of the kit list in `ListsScreen.kt` is removed
- [ ] Left-click behavior (select kit) remains unchanged

### Affected Layers
- **Model:** No changes
- **DeviceManager:** No changes — `updateKit(index, updatedKit)` already exists
- **ViewModel:** Add clipboard state (`MutableStateFlow<Kit?>`) and `copyKit(index)` / `pasteKit(index)` methods to `MainViewModel`
- **Components:** Add right-click context menu support to kit list items; remove button row from `ListsScreen`
- **Screen:** No new screens
- **Navigation:** No

### Mapping Reference
`Map_Kit.md`

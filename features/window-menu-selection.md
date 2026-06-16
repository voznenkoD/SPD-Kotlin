## Feature: Move screen selection from "View" menu to "Window" menu

### Summary
Relocate the screen-switching options (Main / Setup / System) from the **View** menu to a new **Window** menu, using radio items to indicate the active screen. The **View** menu remains in place (empty) for future use.

### Acceptance Criteria
- [ ] A new **"Window"** menu appears in the menu bar after **File** (mnemonic `'W'`).
- [ ] The Window menu contains **Main**, **Setup**, **System** as **radio items** (`RadioButtonItem`), where the item matching `AppState.currentScreen` is selected.
- [ ] Selecting an item switches `AppState.currentScreen` to the corresponding screen, and the radio selection updates to reflect it.
- [ ] The **View** menu remains in the menu bar (mnemonic `'V'`) but is now empty, reserved for later use.
- [ ] No behavioral regression in screen navigation.

### Affected Layers
- **Model / DeviceManager / ViewModel / Components / Screen:** none
- **Navigation:** Only the `MenuBar` block in `App.kt`. Add a `Menu("Window", mnemonic = 'W')` with `RadioButtonItem`s bound to `AppState.currentScreen`; keep an empty `Menu("View", mnemonic = 'V')`.

### Mapping Reference
None — menu-only UI change.

### Open Questions
None — resolved (radio items; View menu retained empty).
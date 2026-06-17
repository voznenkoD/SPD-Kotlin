## Feature: Detachable left/right/bottom panels (pop-out windows)

### Summary
On all three screens (Main, Setup, System), the left, right, and bottom regions become toggleable via the **View** menu. Toggling a region OFF removes it from the main layout; a narrow icon strip on the far left shows a button (square with a thick line on the matching side) that opens that region in a separate pop-up `Window`.

### Region → content mapping
| Screen | Left | Right | Bottom |
|---|---|---|---|
| Main | DetailsTabs | ListsScreen | WaveDetailsScreen (nested in mid column) |
| Setup | SetupGeneralView | SetupMidiView | PadSetupView (full-width row) |
| System | Click+VisualControl column | KitChainView | **Audio** panel (nested in mid column) |

The "mid" content (Main: PadScreen; Setup: PadsSetupScreen; System: MasterEffect) is always docked — never detachable.

### Confirmed decisions
1. System's bottom region = the mid-column **Audio** panel (option b).
2. Main's bottom region = WaveDetails (popping it out leaves the pad grid in the mid area). OK.
3. Toggling a region OFF does **not** auto-open the window — it only makes the region's icon appear in the strip. The window opens only when the icon is clicked.
4. The icon strip is shown only when ≥1 region is off (zero width otherwise).
5. Panel state is **in-memory only** (resets to all-docked on app restart).
6. Re-docking is done by re-checking the View-menu item; closing the pop-up window alone does NOT re-dock (the icon remains to reopen).

### Acceptance Criteria
- [ ] View menu has three **CheckboxItem**s — "Left Panel", "Right Panel", "Bottom Panel" — checked = docked. They reflect/toggle the **current screen's** regions (default all checked).
- [ ] Unchecking a region removes it from the main layout; remaining docked regions expand to fill the space (weight-based).
- [ ] When ≥1 region is off, a narrow vertical icon strip appears on the far left with one button per off-region; the icon is a square outline with a thick accent line on the region's edge (left/right/bottom).
- [ ] Clicking an off-region icon opens its content in a second pop-up `Window` (title e.g. "Left Panel"); clicking again / closing the window sets windowOpen=false but leaves the region off (icon stays).
- [ ] Popped-out content is fully interactive and shares the screen's ViewModel/state (same instance) — edits reflect identically whether docked or windowed.
- [ ] Re-checking the menu item re-docks the region and closes its pop-up window.
- [ ] Each screen has independent panel state; switching screens shows that screen's menu checkmarks, icon strip, and windows. Pop-out windows render only for the currently active screen.

### Affected Layers
- **Model / DeviceManager:** none.
- **State:** new app-level `PanelLayoutState` (singleton `object`, Compose `mutableStateOf`), keyed by `ScreenId {Main,Setup,System} × PanelRegion {Left,Right,Bottom}` → `PanelEntry(docked: Boolean=true, windowOpen: Boolean=false)`. Helper `entry(screenId, region)`, plus `dock(...)`, `setWindowOpen(...)`, `anyUndocked(screenId)`.
- **Components:** new `PanelIconStrip` (far-left button column) and `PanelEdgeIcon` (Canvas: square outline + thick line on the region edge). A small helper to title windows per region.
- **Screens:** refactor `MainScreen`, `SetupScreen`, `SystemScreen` so each region's content is an extractable composable used in BOTH the docked slot (conditionally included with its weight) and a pop-out `Window`. Pop-out `Window`s are declared **inside each screen composable** (so they share that screen's ViewModel/state), guarded by `docked==false && windowOpen==true`.
- **App.kt:** replace the empty "View" menu with the three `CheckboxItem`s bound to `PanelLayoutState` for `AppState.currentScreen`; render `PanelIconStrip` to the left of the screen content in `App()` when `anyUndocked(currentScreen)`.

### Implementation notes
- Use weights for docked regions; simply omit a region's column/row when undocked — remaining weights re-proportion automatically.
- Main & System "bottom" are nested in the mid column (Column { mid; bottom? }); Setup "bottom" is the full-width bottom Row.
- `Window` composables can be nested inside the screen composition (Compose Desktop supports secondary windows declared anywhere under `application{}`).
- Re-check menu item → set docked=true AND windowOpen=false.

### Mapping Reference
None — pure UI/layout.

### Open Questions
None — all resolved.
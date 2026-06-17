# Test Plan: Detachable left/right/bottom panels (pop-out windows)

Feature spec: `features/detachable-panels.md`
Code under test:
- `ui/panels/PanelLayoutState.kt` (state)
- `ui/panels/PanelEdgeIcon.kt`, `PanelIconStrip.kt`, `DetachablePanelWindow.kt`
- `App.kt` (View menu CheckboxItems + icon strip in `App()`)
- `ui/screens/MainScreen.kt`, `SetupScreen.kt`, `SystemScreen.kt` (region extraction)

## Region mapping
| Screen | Left | Right | Bottom |
|---|---|---|---|
| Main | DetailsTabs | ListsScreen | WaveDetails |
| Setup | SetupGeneral | SetupMidi | PadSetupView |
| System | Click+VisualControl | KitChain | Audio |

## Scope
UI/state only; in-memory; no device-model changes. Manual run is primary.

## Manual Test Cases

### TC1 — Toggle a region off (per screen)
For each screen (Main, Setup, System):
1. Open the **View** menu — three checkable items (Left/Right/Bottom Panel), all checked.
2. Uncheck **Left Panel**.
- [ ] The left region disappears from the layout; remaining regions expand to fill.
- [ ] A narrow icon strip appears on the far left with a "left-edge" square icon.
- [ ] No pop-out window opens yet (icon only — per decision #3).
3. Repeat for Right and Bottom on that screen.
- [ ] Each undock adds its icon (thick line on the matching edge: right / bottom).

### TC2 — Open / close pop-out window
1. With a region undocked, click its icon in the strip.
- [ ] A new OS window opens titled "Left/Right/Bottom Panel" containing that region's content.
- [ ] The icon shows a selected/active background while the window is open.
2. Edit something in the pop-out window (e.g. a value/slider/selection).
- [ ] The edit is reflected in the main window state (shared ViewModel) — switch the region back on to confirm, or observe dependent UI.
3. Close the pop-out window (OS close button).
- [ ] Window closes; the region stays undocked; the icon remains (click reopens).

### TC3 — Re-dock via menu
1. Re-check the region in the **View** menu.
- [ ] The region returns to its docked position in the layout.
- [ ] Its icon disappears from the strip; if its pop-out window was open, it closes.
- [ ] When all regions are re-docked, the icon strip disappears entirely (zero width).

### TC4 — Per-screen independence
1. Undock Main's Left. Switch to Setup (Window menu).
- [ ] Setup shows its own menu checkmarks (all checked) and no icon strip (unless Setup has its own undocked regions).
2. Switch back to Main.
- [ ] Main still shows Left undocked with its icon.
- [ ] Only the active screen's pop-out windows are shown.

### TC5 — System bottom = Audio
1. On System, uncheck **Bottom Panel**.
- [ ] The Audio panel leaves the mid column; MasterEffect fills the mid column height.
- [ ] Bottom icon appears; clicking opens Audio in a window.

### TC6 — In-memory reset
1. Undock several regions, then restart the app.
- [ ] All regions are docked again (state is not persisted).

### TC7 — Regression
- [ ] With all panels docked (default), all three screens look/behave exactly as before.
- [ ] Main wave drag-overlay, kit/pad selection, Setup pad selection, System kit-chain edits all still work.

## Regression Checks
- [ ] `./gradlew compileKotlinDesktop` succeeds.
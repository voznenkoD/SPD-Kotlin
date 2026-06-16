# Test Plan: Highlight selected kit in the kit list

Feature spec: `features/highlight-selected-kit.md`
Code under test:
- `ui/components/lists/KitListView.kt` (selection background)
- `ui/components/lists/ListsScreen.kt` (param threading)
- `ui/screens/MainScreen.kt` (wires `selectedKitIndex` into `ListsScreen`)

## Scope
UI-only highlight change. No model/service/viewmodel changes. Verified by
compilation plus manual run; Compose list rendering is not unit-tested in this
project.

## Manual Test Cases

### TC1 — Selected kit is highlighted
1. `./gradlew desktopRun`, choose a folder with a device export.
2. On the Main screen, ensure the right-side list is on the **Kits** tab.
3. Click a kit in the list.
- [ ] The clicked kit's row shows the dark selected background (`ColorSurfaceSelected`).
- [ ] All other kit rows keep the light default background (`ColorSurface`).
- [ ] The kit name text remains readable (same treatment as the wave list — no special color).

### TC2 — Highlight follows selection
1. Click kit A → A highlighted.
2. Click kit B → B highlighted, A reverts to default.
- [ ] Only one kit row is highlighted at a time; highlight tracks the active selection.

### TC3 — Selection from other sources (parity with wave behavior)
1. Select a kit indirectly (e.g. via a pad or "Used in: <kit>" wave context menu that calls `selectKitByName`).
- [ ] The corresponding kit row in the Kits tab becomes highlighted, reflecting `MainViewModel.selectedKitIndex`.

### TC4 — Drag/drop precedence (no regression)
1. Long-press a kit and start dragging.
- [ ] The dragged row shows `ColorSurfaceHover` (not the selected color), even if it was the selected kit.
- [ ] The drop-target row shows `ColorAccentYellow`.
2. Release to reorder.
- [ ] Reorder works; after drop, the selected kit highlight is shown again on the appropriate row.

### TC5 — Existing behavior intact
- [ ] Click-to-select still selects the kit and updates the detail/pad views.
- [ ] Right-click Copy / Paste context menu still works.

## Regression Checks
- [ ] `./gradlew compileKotlinDesktop` succeeds.
- [ ] Wave list selected-highlight still works (shared `ColorSurfaceSelected` styling unchanged).
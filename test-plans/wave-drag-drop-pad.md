## Test Plan: Wave Drag-and-Drop to Pad

Covers the feature specified in `features/wave-drag-drop-pad.md`.

---

### 1. Unit: MainViewModel drag state

- **T1.1** `startWaveDrag(5, "Kick")` → `dragInfo.value == DragInfo(5, "Kick")`
- **T1.2** `updateDragPosition(Offset(100, 200))` → `dragPosition.value == Offset(100, 200)`
- **T1.3** `cancelWaveDrag()` → both `dragInfo` and `dragPosition` are null
- **T1.4** `endWaveDrag()` with no registered pad bounds → clears drag state, no pad mutation
- **T1.5** `endWaveDrag()` with position inside a registered main bound → updates `pad.main.wave`
- **T1.6** `endWaveDrag()` with position inside a registered sub bound → updates `pad.sub.wave`
- **T1.7** `endWaveDrag()` with no selected kit → clears drag state, no mutation
- **T1.8** `registerPadBounds` / `unregisterPadBounds` lifecycle

### 2. Manual UI smoke tests

Run `./gradlew desktopRun` against a real device folder.

- **M2.1 Drag initiation** — Long-press a wave row in the list. After ~300ms hold, start dragging. A floating label appears near the cursor showing the wave number + name.
- **M2.2 Floating label follows cursor** — While dragging, the label tracks cursor movement across the entire window.
- **M2.3 Pad highlight on hover** — Drag over a pad's top half → green highlight appears on the main area. Drag to the bottom half → highlight switches to sub area.
- **M2.4 Drop on main** — Drop on a pad's top half. The pad's main wave label updates to the dropped wave name.
- **M2.5 Drop on sub** — Drop on a pad's bottom half. The pad's sub wave label updates.
- **M2.6 Drop outside** — Drag then release outside any pad. No wave assignment, floating label disappears.
- **M2.7 No kit selected** — Deselect kit, drag a wave over pads. No highlight, no assignment on drop.
- **M2.8 FS pads** — Drag onto foot switch pads. Both main and sub halves work as drop targets.
- **M2.9 All sort modes** — Drag from BY_NAME, BY_CATEGORY_NAME, BY_CATEGORY_NUM wave lists. All work.
- **M2.10 Click still works** — Single click on wave rows still selects the wave (no drag conflict).
- **M2.11 Context menu still works** — Right-click on wave rows still shows context menu.
- **M2.12 Scroll still works** — Scrolling the wave list still works normally (no drag conflict on short touches).
- **M2.13 Persistence** — After drag-dropping a wave onto a pad, save the device. Reload. The wave assignment persists.
- **M2.14 TRIG pads** — Drag onto trigger pads (TRIG_1–TRIG_4). Both main/sub halves work.

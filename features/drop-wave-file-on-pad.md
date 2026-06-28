## Feature: Drop Wave File On Pad

### Summary
Drag a `.wav` file from the OS filesystem onto a pad's main or sub zone: the file is imported
(as the last wave in the Default category, exactly like `importWave`) and the resulting wave is
assigned to that pad as main or sub depending on which zone it was dropped onto.

### Acceptance Criteria
- [ ] Dropping a `.wav` from the OS onto a pad's **main** zone imports it (Default category, next/last wave number) and sets it as that pad's **main** wave.
- [ ] Dropping onto a pad's **sub** zone does the same but sets the **sub** wave.
- [ ] Import reuses `DeviceManager.importWave(file, "Default")` unchanged — WAV validation, name sanitization (≤12 chars), next wave number (last in list), copy to `WAVE/DATA`, `.spd` write, wavelist + SYSTEM file updates.
- [ ] On import failure (invalid/duplicate/non-wav/library full), the existing import-error dialog is shown (reuse `_importError`) and NO pad assignment happens.
- [ ] Pad assignment uses the existing in-memory path (`updatePad`), consistent with the in-app wave drag: the wave file is persisted by import; the pad assignment persists on the next explicit save.
- [ ] The external drop reuses the existing per-pad `mainBounds`/`subBounds` (window coords, already registered by `PadScreen`) to decide pad + zone.
- [ ] **Drop not over a pad zone, or with no kit selected → ignore entirely** (no import, no assignment).
- [ ] **Multiple files dropped → handle the first valid `.wav` only**; ignore the rest.
- [ ] **Hover highlight:** while an OS file is dragged over a pad, the target main/sub zone highlights (reuse the existing `isDragActive` + `dragPosition` highlight). Highlight clears on drop/exit/cancel.
- [ ] Build compiles (`./gradlew compileKotlinDesktop`).

### Affected Layers
- **Model:** none.
- **DeviceManager:** none — reuse `importWave(file, "Default")` and `updatePad(kitIndex, padNumber, transform)`.
- **ViewModel:** `MainViewModel` —
  - Add `dropExternalWaveFile(file: File, position: Offset)`: require a selected kit and that `position` hits a registered pad `mainBounds`/`subBounds` (mirror `endWaveDrag`'s hit-test); if not, return without importing. Otherwise call `deviceManager.importWave(file, "Default")`; on `Success(newWave)` assign `newWave.number` to the hit pad's main or sub via `updatePad`; on `Error` set `_importError`.
  - Add external-drag position tracking so the pad hover highlight works during the OS drag (e.g. `updateExternalDragPosition(Offset?)` feeding the same `dragPosition`/`isDragActive` the in-app drag uses, or a parallel pair). Clear on drop/exit.
- **Components:** attach a Compose `Modifier.dragAndDropTarget` (Compose 1.7.0; fall back to `onExternalDrag` if needed) at/above `MainScreen` content so OS file drops are received. Extract the dropped file (first valid `.wav`) and the drop position in **window coordinates** (must match `boundsInWindow()` used by pad bounds). Feed move-position to the VM for highlight and the drop to `dropExternalWaveFile`. Reuse `PadScreen`'s existing main/sub hover highlight.
- **Screen:** `MainScreen` / `App` wires the drop target around the pad area; `PadScreen` bounds registration is already in place.
- **Navigation:** none.

### Mapping Reference
`Map_Pad.md` (pad main/sub wave assignment). Wave persistence as in `importWave`.

### Decisions (resolved)
1. Drop off-pad or with no kit selected → ignore entirely (no import).
2. Multiple files → first valid `.wav` only.
3. Show pad hover highlight during the external drag (reuse existing highlight).

### Notes for build
- Reuse the existing `padDropTargets` map + hit-test logic from `endWaveDrag` (main before sub). Consider extracting a shared `hitTestPad(position): Pair<PadNumber, isMain>?` helper used by both `endWaveDrag` and the new external-drop handler.
- Drop position must be in the same coordinate space as `boundsInWindow()`. The `dragAndDropTarget` event position is relative to the target component; place the target at the root/content so it aligns with window coords, or offset-correct as needed.
- Import already persists the wave to disk; pad assignment stays in memory until the user saves (consistent with current in-app drag behavior).
- WAV validity is enforced by `WavValidator` inside `importWave`; non-wav/invalid drops surface via the existing import-error dialog.

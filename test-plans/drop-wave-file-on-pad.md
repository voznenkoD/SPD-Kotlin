# Test Plan: Drop Wave File On Pad

Feature file: `features/drop-wave-file-on-pad.md`
Implementation:
- `viewmodel/MainViewModel.kt` — `dropExternalWaveFileOnPad`, refactored `hitTestPad`/`assignWaveToPad`
- `ui/components/pad/KitPadsScreen.kt` — per-zone `dragAndDropTarget`, hover state, drop helpers
- `ui/screens/MainScreen.kt` — wires `onExternalWaveDrop = mainViewModel::dropExternalWaveFileOnPad`

## Setup
1. `./gradlew desktopRun`, open a folder with an SPD device export.
2. Select a kit (so a kit is active). Have a few `.wav` files in a file manager.

## Manual Test Cases

### 1. Drop onto main zone
- [ ] Drag a `.wav` from the OS file manager onto a pad's **upper (main)** half and drop.
- [ ] The wave is imported: it appears as the **last** wave in the **Default** category in the Waves list (Number = previous max + 1).
- [ ] The pad's **main** slot now shows the imported wave's name.
- [ ] The `.wav` and its `.spd` exist under `WAVE/DATA` / `WAVE/PRM` (import persists immediately).

### 2. Drop onto sub zone
- [ ] Drop a `.wav` onto a pad's **lower (sub)** half.
- [ ] Imported the same way, and assigned to the pad's **sub** slot.

### 3. Hover highlight
- [ ] While dragging an OS file over a pad's main or sub zone (before releasing), that zone highlights (yellow tint), matching the in-app wave-drag highlight.
- [ ] Moving off the zone / leaving the window clears the highlight; dropping clears it.

### 4. Off-pad / no-kit
- [ ] Dropping a `.wav` somewhere that is NOT a pad zone (e.g. the lists panel or empty area) does nothing — no import, no assignment.
- [ ] With no kit selected, dropping on a pad does nothing (no import).

### 5. Invalid / non-wav files
- [ ] Drop a non-`.wav` file (e.g. `.txt`, `.mp3`) on a pad → nothing happens (only `.wav` is picked up).
- [ ] Drop a `.wav` that fails validation (wrong format) or a duplicate name → the existing import-error dialog appears; no pad assignment.
- [ ] Drop when the library is full (1000 waves) → import-error dialog; no assignment.

### 6. Multiple files
- [ ] Select and drop **several** `.wav` files at once on a pad → only the **first valid `.wav`** is imported and assigned; the rest are ignored.

### 7. Persistence semantics
- [ ] The imported wave file is on disk immediately (import writes it).
- [ ] The **pad assignment** is in memory until an explicit Save (consistent with in-app wave drag): reload without saving → pad assignment is gone but the imported wave remains in the library.

### 8. Regression — in-app wave drag still works
- [ ] Drag a wave from the Waves list onto a pad main/sub (the existing in-app drag) → still assigns correctly, highlight still works, no double-handling.

## Notes
- External drops are routed by Compose `dragAndDropTarget` per pad zone (main/sub), so the zone under the cursor receives the drop — no manual position hit-testing for external drops.
- File extraction uses the AWT transferable (`DataFlavor.javaFileListFlavor`); only names ending in `.wav` (case-insensitive) are accepted.

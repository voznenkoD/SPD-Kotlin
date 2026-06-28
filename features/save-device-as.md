## Feature: Save Device As

### Summary
Add a **"Save As…"** File-menu option that copies the entire current device (all `.wav` audio + all
`.spd`/system metadata) to a user-chosen folder/drive, with a confirmation dialog (target path + name)
and a byte-based progress bar during the copy. After a successful copy the new location becomes the
current device (future saves target it); the original is left untouched.

### Acceptance Criteria
- [ ] **"Save As…"** item in the File menu (next to "Save").
- [ ] Selecting it opens `FolderChooser.openFolderDialog` to pick the destination parent folder/drive.
- [ ] A confirmation dialog shows the resulting **target path** and an **editable device folder name** (pre-filled with the current device folder's name); Confirm/Cancel.
- [ ] Target = `<destination>/<name>`. If it already exists or is non-empty → **refuse with a clear error**, leave current device/rootPath unchanged.
- [ ] On Confirm, the full device tree is copied to the target: `SYSTEM/`, `KIT/`, `WAVE/PRM/`, **and `WAVE/DATA/` (audio physically copied)**.
- [ ] Capture **current in-memory edits**: after copying the audio, write the current in-memory metadata (system config, kits, wave params, wave lists) into the target so Save As reflects the working state (pad assignments, unsaved param edits). The **original location is not modified**.
- [ ] A **`LinearProgressIndicator`** reflects copy progress (byte-based); the copy runs **off the UI thread** (`Dispatchers.IO`), UI stays responsive.
- [ ] On success, the in-memory `Device` is **rebased to the new rootPath** (`device.copy(rootPath = target)`) and treated as current; a subsequent "Save" writes there.
- [ ] On IO error, a clear error message is shown; current device/rootPath unchanged.
- [ ] Build compiles (`./gradlew compileKotlinDesktop`).

### Affected Layers
- **Model:** none — `Device` is a data class; rebase via `copy(rootPath = …)`.
- **DeviceManager:**
  - Refactor `saveDevice()` → private `saveDeviceTo(rootPath: String)` containing the existing write logic; `saveDevice()` delegates with `device.rootPath`.
  - Add `sealed class SaveAsResult { Success; data class Error(message) }`.
  - Add `suspend fun saveDeviceAs(targetRootPath: String, onProgress: (Float) -> Unit): SaveAsResult`:
    1. Validate device loaded and `targetRootPath` does not already exist / is empty (else Error).
    2. On `Dispatchers.IO`: enumerate source files under current rootPath, copy each to the mirrored path under target, updating `onProgress` by bytes copied / total bytes (covers WAVE/DATA audio + all spd/system).
    3. Call `saveDeviceTo(targetRootPath)` to write the current in-memory metadata into the target (captures unsaved edits).
    4. On the main thread, rebase: `device = currentDevice.copy(rootPath = targetRootPath)`.
    5. Return Success; wrap IO in try/catch → Error.
- **ViewModel/State:** Save-As UI state (target parent path, name, progress 0..1 or null, error, in-progress flag) held at App level (existing Save/Open are wired in `App.kt`). Use `rememberCoroutineScope()` to launch and collect progress.
- **Components:** `SaveAsDialog` composable — AlertDialog mirroring existing dialog style (`ColorSurface`, `ColorTextPrimary`, accent buttons) with: editable name `TextField`/`BasicTextField`, target-path text, `LinearProgressIndicator` (shown during copy), Confirm (disabled while copying / name blank), Cancel.
- **Screen/Navigation:** no new screen; File-menu `Item("Save As…")` + the dialog wired in `App.kt`.

### Mapping Reference
N/A — filesystem/device I/O. On-disk layout under `rootPath`: `SYSTEM/*.spd`, `KIT/KIT_%03d.spd`, `WAVE/PRM/%02d/%02d.spd`, `WAVE/DATA/<rel>/<wav>`.

### Decisions (resolved)
1. Editable name, default = current device folder name; target = `<destination>/<name>`.
2. Copy includes current unsaved in-memory edits (write current model metadata into the target after copying audio); original untouched.
3. If target exists / is non-empty → refuse with error.

### Notes for build
- Progress by total bytes for smoothness across large WAV libraries; update on a throttled cadence is fine.
- The metadata write (step 3) overwrites the just-copied stale `.spd` with the current model — this is intended so unsaved edits are captured.
- `device` (Compose `mutableStateOf`) assignment should happen on the main thread after the IO completes.
- `FolderChooser.openFolderDialog` returns the chosen folder via callback; the dialog is modal (call it from the menu action), then show the confirmation dialog.

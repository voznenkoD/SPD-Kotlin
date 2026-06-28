# Test Plan: Save Device As

Feature file: `features/save-device-as.md`
Implementation:
- `service/DeviceManager.kt` — `saveDeviceTo(rootPath)` refactor, `SaveAsResult`, `suspend saveDeviceAs(target, onProgress)`
- `ui/components/common/SaveAsDialog.kt` — confirmation + progress dialog
- `App.kt` — File ▸ "Save As…" menu item + dialog wiring

## Setup
1. `./gradlew desktopRun`, open a device folder (File ▸ Choose folder).

## Manual Test Cases

### 1. Menu availability
- [ ] File menu shows "Save As…" beneath "Save".
- [ ] "Save As…" is **disabled** when no device is loaded; enabled once a device is open.

### 2. Happy path
- [ ] Click "Save As…" → folder picker opens; choose an empty destination parent folder.
- [ ] Dialog shows an editable name (pre-filled with the current device folder's name) and "Save to: <dest>/<name>".
- [ ] Click Save → progress bar advances 0→100%, "Copying… N%" updates; UI stays responsive.
- [ ] On completion the dialog closes.
- [ ] On disk, `<dest>/<name>` contains the full tree: `SYSTEM/`, `KIT/`, `WAVE/PRM/`, **`WAVE/DATA/` with all `.wav` files**.
- [ ] The app now treats the copy as current: make an edit and File ▸ Save → it writes into `<dest>/<name>` (not the original). The **original folder is unchanged** by the Save As.

### 3. Captures unsaved edits
- [ ] Make an edit (e.g. assign a wave to a pad, change a wave param) WITHOUT saving.
- [ ] Save As to a new folder → open/inspect the copy's metadata: the edit IS present in the copy.
- [ ] The original folder still does NOT have that edit (it was never saved there).

### 4. Rename
- [ ] In the dialog, change the name field → the "Save to:" path updates live; the copy is created under the new name.

### 5. Existing/non-empty target → refused
- [ ] Save As into a destination where `<dest>/<name>` already exists and is non-empty → an error is shown in the dialog; nothing is copied; current device/rootPath unchanged.
- [ ] Targeting a brand-new (or empty) folder name succeeds.

### 6. Cancel
- [ ] Open the dialog and Cancel before saving → no copy, dialog closes.
- [ ] During an active copy, Cancel and the name field are disabled (can't cancel mid-copy).

### 7. Error handling
- [ ] Choose a destination on a read-only/locked location → an error message appears; current device unchanged; app stays usable.

### 8. Large library
- [ ] With a large WAVE/DATA set, the progress bar moves smoothly (byte-based) and the window does not freeze during the copy.

## Notes
- Copy runs on `Dispatchers.IO`; the device rebase (`device.copy(rootPath = target)`) happens on the main thread.
- The copy physically duplicates audio; metadata is then re-written from the in-memory model so unsaved edits are included.

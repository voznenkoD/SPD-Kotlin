## Feature: Import Wave From .wav File

### Summary
Add an **Import Wave…** action to the wave list's right-click context menu that prompts the user to pick a target category, opens a native file chooser restricted to `.wav`, validates the file is 16-bit / 44.1 kHz PCM (mono or stereo), writes a new `WvPrm` `.spd` under `WAVE/PRM`, copies the audio payload into the matching `WAVE/DATA` subfolder, and refreshes the UI so the new wave appears in place immediately.

### Acceptance Criteria
- [ ] Right-clicking inside the wave list (all three sorting modes — BY_NAME, BY_CATEGORY_NAME, BY_CATEGORY_NUM) shows a context menu item **"Import Wave…"**
- [ ] Selecting it opens a small dialog letting the user pick a target category from the existing categories (defaulting to `Default`), with **Continue** / **Cancel** buttons
- [ ] On **Continue**, a native file chooser opens restricted to `.wav` files
- [ ] Selected file is validated: must be WAV PCM, 16-bit sample depth, 44,100 Hz sample rate; mono and stereo are both accepted. On failure, an error `AlertDialog` explains the reason and the import aborts (no files written)
- [ ] Wave name is derived from the source filename stem, sanitized to ASCII-safe characters, and truncated to 12 characters to fit `Nm0..Nm11`
- [ ] If the derived name collides with any existing wave name in the whole wave list, an error `AlertDialog` asks the user to choose a different name and aborts the import (no files written)
- [ ] Next free wave slot is computed as `maxExistingWaveNumber + 1` (1-indexed; PRM folder = `(n-1)/100` zero-padded to two digits, PRM file = `(n-1)%100` zero-padded to two digits)
- [ ] Import is refused with an error dialog if the next slot would exceed `09/99` (wave #1000) — this is the hard cap
- [ ] If the target PRM folder already has 100 files (00–99), the next two-digit folder is created under `WAVE/PRM/` and a matching folder is created under `WAVE/DATA/`
- [ ] The source `.wav` file is copied to `WAVE/DATA/<folder>/<sanitizedFilename>.wav`
- [ ] A new `WvPrm` XML is written to `WAVE/PRM/<folder>/<file>.spd` with: `Nm0..Nm11` encoding the wave name, `<Path>` = `"<folder>/<sanitizedFilename>.wav"`, `<Tag>` = the chosen category's tag index. `Tempo`, `Beat`, `Measure`, `Start`, `End` are written as `0` (they are required by `WvPrm` even though the example `.spd` omits them)
- [ ] The updated `tag_list.spd` and `wavelist_*.spd` index files are also persisted to disk immediately as part of the import (so the on-disk wave tree stays consistent after import)
- [ ] In-memory `Device.waves` is updated and `WaveListsHolder` is rebuilt so the new wave appears in `wavesByName`, `wavesByNamePerCategory`, and `wavesByNumPerCategory`
- [ ] UI re-renders and the new wave is visible in its correct position (alphabetical within the chosen category in `BY_CATEGORY_NAME`, appended to the end in `BY_CATEGORY_NUM`, alphabetical in `BY_NAME`) with no manual refresh required
- [ ] Existing save-on-metadata-change behavior is preserved: subsequent in-session edits (kit, pad, rename, etc.) still batch until the user triggers global Save

### Affected Layers
- **Model:** No new domain classes. Reuse `Wave` and `WvPrm`. Add a `WavValidator` utility (in `data/` or `service/`) that reads the WAV RIFF header and returns a `ValidationResult` describing format, bit depth, sample rate, and channel count.
- **DeviceManager:**
  - New `importWave(sourceFile: File, categoryName: String): ImportResult` (sealed result type: `Success(Wave)` / `Error(message)`) that: validates the WAV, derives + sanitizes + uniqueness-checks the name, computes the next slot, creates folders as needed, copies the `.wav`, writes the new `WvPrm` `.spd`, updates `device.waves`, rebuilds `device.waveLists`, and persists the wave list index files (`tag_list.spd`, `wavelist_name.spd`, `wavelist_tagname.spd`, `wavelist_tagnum.spd`) to disk immediately.
  - Reuse existing `xmlParser.writeWaveFile` / `writeSystemFile` for persistence — do not duplicate write logic.
- **ViewModel:** `MainViewModel.importWave(sourceFile: File, categoryName: String)` delegates to `DeviceManager`; surfaces errors through a `StateFlow<String?>` (e.g. `importError`) so the UI can show an error dialog and reset state.
- **Components:** `ListsScreen.kt` —
  - Add `ContextMenuItem("Import Wave…")` to the wave list context menus (available in all three sorting modes; attach it to `WaveListItem`'s menu and to category headers in the BY_CATEGORY views so users can right-click anywhere meaningful).
  - Add a `CategoryPickDialog` composable (category dropdown + Continue / Cancel).
  - Hook into a native WAV file picker (use `java.awt.FileDialog` with a `.wav` filter, consistent with the existing `FolderChooser` service's approach).
  - Add an error `AlertDialog` driven by the ViewModel's `importError` state.
- **Screen:** `MainScreen.kt` — pass `mainViewModel::importWave` and `importError` state through to `ListsScreen`.
- **Navigation:** No new screen.

### Mapping Reference
`Map_System.md` — wave list files (`wavelist_name.spd`, `wavelist_tagname.spd`, `wavelist_tagnum.spd`) and `tag_list.spd` for category/tag mapping; wave PRM structure per `examples/wave.spd`.

### Key Design Decisions (confirmed by user)
- **Category selection:** User is prompted via a dialog to choose a target category before the file picker opens (default = `Default`)
- **Wave name derivation:** From filename stem, sanitized and silently truncated to 12 characters
- **Collision handling:** If the derived/truncated name collides with any existing wave in the list, abort with an error dialog asking the user to rename the source file
- **Audio format:** 16-bit / 44.1 kHz PCM required; both mono and stereo accepted
- **Persistence on import:** Wave PRM `.spd`, `.wav` payload, AND wave list index files (`tag_list.spd`, `wavelist_*.spd`) are written to disk immediately on import. Other in-session metadata changes continue to batch until global Save
- **Hard cap:** Maximum wave slot is `09/99` (wave #1000). Exceeding it is refused with an error
- **Missing `WvPrm` numeric fields:** `Tempo`, `Beat`, `Measure`, `Start`, `End` default to `0` (required by the data class even though absent from `examples/wave.spd`)
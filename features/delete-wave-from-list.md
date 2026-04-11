## Feature: Delete Wave From List

### Summary
Add a **Delete Wave…** action to the wave list's right-click context menu that removes the wave from the device — deleting its `WvPrm` `.spd` file and its `.wav` payload from disk, and dropping it from `Device.waves` and all three `WaveListsHolder` views. If the wave is currently referenced by any kit pad (main or sub), deletion is refused with an error dialog listing the kits using it.

### Acceptance Criteria
- [ ] Right-clicking a `WaveListItem` in all three sorting modes (BY_NAME, BY_CATEGORY_NAME, BY_CATEGORY_NUM) shows a context menu item **"Delete Wave…"**
- [ ] If the wave is used by at least one pad (checked via the existing `DeviceManager.buildWaveUsageMap`), selecting **Delete Wave…** opens an error `AlertDialog` titled *"Cannot delete wave"* that lists the referencing kit names as **plain text** (not clickable), and aborts with **no files written or removed**
- [ ] If the wave is unused, a confirmation `AlertDialog` warns the user the action is irreversible (shows wave number + name, mentions both the `.spd` and `.wav` files will be deleted from disk) with **Delete** / **Cancel** buttons
- [ ] On **Delete** confirm, the corresponding `WAVE/PRM/<folder>/<file>.spd` file is deleted (folder/file derived from `wave.number` the same way import computes them: `(n-1)/100` zero-padded, `(n-1)%100` zero-padded)
- [ ] The `.wav` payload at `WAVE/DATA/<wave.path>` is deleted
- [ ] If either file is already missing on disk, deletion still proceeds and does not surface an error (best-effort cleanup)
- [ ] **Partial failure is best-effort**: if one of the two file deletes throws, the other is still attempted, and the in-memory removal + index-file rewrite still happen. A non-fatal warning may be surfaced via the error state but state is never rolled back
- [ ] The wave is removed from `Device.waves`
- [ ] `WaveListsHolder` is rebuilt so the wave disappears from `wavesByName`, `wavesByNamePerCategory`, and `wavesByNumPerCategory` via a new `withRemovedWave(waveNumber: Int)` helper on the holder
- [ ] Updated `tag_list.spd` and `wavelist_*.spd` index files are persisted to disk immediately as part of the delete (matching `importWave`'s persistence behavior)
- [ ] If the deleted wave was the currently selected one in `MainViewModel`, `selectedWave` is cleared so no stale wave sits in the detail pane
- [ ] UI re-renders and the wave no longer appears in any view — no manual refresh required
- [ ] Wave numbers are **not** renumbered: a gap is left in the slot sequence. Next import continues to use `max(number) + 1`, so gaps are never reclaimed (consistent with current `importWave` logic)
- [ ] Context menu entry is offered on individual wave rows only — **not** on category headers

### Affected Layers
- **Model:**
  - `WaveListsHolder`: add `withRemovedWave(waveNumber: Int): WaveListsHolder` mirroring `withAddedWave`. Removes the matching `ListedWave` from `wavesByName`, `wavesByNamePerCategory`, and `wavesByNumPerCategory` without touching category keys.
- **DeviceManager:**
  - Add `sealed class DeleteResult { Success; Error(message); InUse(kitNames: List<String>) }`.
  - New `deleteWave(waveNumber: Int): DeleteResult` that:
    1. Looks up the wave in `device.waves`; returns `Error` if missing.
    2. Checks `buildWaveUsageMap(device.kits)[waveNumber]`; returns `InUse` with the kit-name list if non-empty.
    3. Attempts to delete both `WAVE/PRM/<folder>/<file>.spd` and `WAVE/DATA/<wave.path>` — each wrapped so one failure does not block the other.
    4. Updates `device.waves` (filter out the wave) and `device.waveLists` (via `withRemovedWave`).
    5. Persists `tag_list.spd`, `wavelist_name.spd`, `wavelist_tagname.spd`, `wavelist_tagnum.spd` via the existing `xmlParser.writeSystemFile` calls.
    6. Returns `Success`.
  - Reuses existing persistence helpers — no new write logic.
- **ViewModel:** `MainViewModel`
  - New `deleteWave(waveNumber: Int)` that calls `DeviceManager.deleteWave` and routes the result.
  - New `StateFlow<DeleteBlockedInfo?> deleteBlocked` where `DeleteBlockedInfo(waveName: String, kitNames: List<String>)` drives the "Cannot delete wave" dialog.
  - New `StateFlow<String?> deleteError` for generic errors.
  - Clears `selectedWave` on success if the removed wave was selected.
  - New `clearDeleteBlocked()` and `clearDeleteError()` helpers.
- **Components:** `ListsScreen.kt`
  - Add `ContextMenuItem("Delete Wave…")` to `WaveListItem`'s menu (per-row only — category headers are unchanged).
  - Add a `DeleteWaveConfirmDialog` composable with destructive styling (Cancel / Delete buttons), showing the wave number + name and mentioning on-disk file deletion.
  - Add a `WaveInUseDialog` composable that displays the wave name and the list of referencing kits as plain non-interactive text.
  - Add a generic error `AlertDialog` driven by `deleteError`.
  - Thread `onDeleteWave: (waveNumber: Int) -> Unit`, `deleteBlocked`, `deleteError`, `onClearDeleteBlocked`, `onClearDeleteError` through `ListsScreen` and the three wave-list sub-composables the same way `onImportWave` / `importError` are today.
- **Screen:** `MainScreen.kt` — pass `mainViewModel::deleteWave`, `deleteBlocked` and `deleteError` state flows, and the two clear callbacks into `ListsScreen`.
- **Navigation:** No new screen.

### Mapping Reference
`Map_System.md` — `tag_list.spd` and `wavelist_*.spd` index files that must be rewritten after deletion. Wave PRM file layout per `examples/wave.spd` / existing `importWave` logic for folder/file naming.

### Key Design Decisions (confirmed by user)
- **Confirmation dialog:** Yes — destructive-action confirmation is shown before any file is touched.
- **Wave number gaps:** Gaps are left behind; the next import still uses `max(number) + 1`. No renumbering, no gap reclamation.
- **"Used in" dialog:** Plain non-interactive text listing the kit names. No jump-to-kit behavior.
- **Partial failure handling:** Best-effort continue. If one file delete fails, the other is still attempted and in-memory + index files are still updated. State is never rolled back.
- **Category header delete:** Not offered. Delete is per-wave only.
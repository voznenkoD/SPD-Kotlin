## Test Plan: Delete Wave From List

Covers the Delete Wave feature specified in `features/delete-wave-from-list.md`. The feature has three interacting layers (pure model, disk + in-memory mutation in `DeviceManager`, and UI in `ListsScreen`), so the plan splits tests by layer.

---

### 1. Unit: `WaveListsHolder.withRemovedWave`

Pure function, no I/O. These can run under `desktopTest` with plain JUnit 4.

**Setup helpers:** build a `WaveListsHolder` with two categories (`Default`, `Drums`) and 4–6 waves across the three internal maps (`wavesByName`, `wavesByNamePerCategory`, `wavesByNumPerCategory`).

- **T1.1 removes wave from all three maps**
  Given a wave number present in `wavesByName`, its category in `wavesByNamePerCategory`, and its category in `wavesByNumPerCategory`, the returned holder must not contain that number in any of the three collections.

- **T1.2 preserves ordering of surviving waves**
  `wavesByName` should keep alphabetical order of the remaining waves. `wavesByNumPerCategory` should keep its original insertion order.

- **T1.3 preserves category keys**
  Categories (including empty ones that result from removal) must still be present as keys in `wavesByNamePerCategory` and `wavesByNumPerCategory` after the removal.

- **T1.4 no-op for unknown wave number**
  Calling `withRemovedWave(Int.MAX_VALUE)` returns a holder structurally equal to the original.

- **T1.5 removes the only wave in a category**
  Wave list for the now-empty category is `emptyList()`, not missing.

---

### 2. Integration: `DeviceManager.deleteWave`

Requires a temporary device folder on disk. Use `@Rule val tempFolder = TemporaryFolder()` (JUnit 4) and either (a) copy a small fixture device tree from `examples/` if one exists, or (b) construct a minimal one programmatically with a couple of `WAVE/PRM/00/00.spd` + `WAVE/DATA/00/foo.wav` files and the SYSTEM index files.

- **T2.1 happy path: unused wave is deleted from disk, memory, and index files**
  - Arrange: device with wave #3 unused by any kit pad.
  - Act: `deleteWave(3)`.
  - Assert:
    - Returns `DeleteResult.Success`.
    - `WAVE/PRM/00/02.spd` no longer exists.
    - `WAVE/DATA/<wave.path>` no longer exists.
    - `device.waves` no longer contains wave #3.
    - `device.waveLists` has wave #3 removed from all three maps.
    - `SYSTEM/tag_list.spd`, `wavelist_name.spd`, `wavelist_tagname.spd`, `wavelist_tagnum.spd` are rewritten with the new content (re-parse to confirm wave #3 is absent).

- **T2.2 in-use wave is refused and leaves disk untouched**
  - Arrange: device where wave #5 is referenced by a pad's `main.wave` in `Kit[2]` and another pad's `sub.wave` in `Kit[7]`.
  - Act: `deleteWave(5)`.
  - Assert:
    - Returns `DeleteResult.InUse` with kit names in insertion-order and without duplicates (matching `buildWaveUsageMap` semantics).
    - `device.waves`, `device.waveLists`, and the on-disk PRM and DATA files are all unchanged.

- **T2.3 unknown wave number returns `Error`**
  - Arrange: device with waves #1..#3.
  - Act: `deleteWave(99)`.
  - Assert: `DeleteResult.Error`, no state touched.

- **T2.4 best-effort continue — PRM already missing**
  - Arrange: wave exists in memory but the PRM file on disk was deleted out-of-band.
  - Act: `deleteWave(n)`.
  - Assert: returns `Success`; the DATA file is still deleted; in-memory state and index files are still updated.

- **T2.5 best-effort continue — DATA already missing**
  - Same as T2.4 but with the `.wav` file missing. Expect `Success` and the PRM removed.

- **T2.6 best-effort continue — both files missing**
  - Both files already gone. Expect `Success`, in-memory removal, index files rewritten.

- **T2.7 delete leaves a gap (no renumbering)**
  - Arrange: waves #1, #2, #3. Delete #2.
  - Assert: remaining waves still have numbers #1 and #3. Subsequent `importWave(...)` uses number `4` (since `max + 1 = 4`), confirming gaps are not reclaimed.

- **T2.8 slot math for folder/file derivation**
  - Delete wave #101 (folder `01`, file `00`) and wave #200 (folder `01`, file `99`). Confirm the correct on-disk paths are removed in each case — off-by-one in `(n-1)/100` / `(n-1)%100` is easy to regress.

- **T2.9 no device loaded**
  - `device = null` → `DeleteResult.Error("No device loaded")`.

- **T2.10 index-file write failure surfaces as `Error` but in-memory removal is already applied**
  - Simulate a write failure by making the `SYSTEM` directory read-only (or by stubbing `xmlParser`).
  - Assert: returns `Error`, but `device.waves` and `device.waveLists` already reflect the removal (per spec: state is never rolled back).

---

### 3. ViewModel: `MainViewModel` delete flow

These can be plain JUnit 4 tests against a `MainViewModel` holding a real `DeviceManager` backed by a fixture device folder. Collect `StateFlow` values via `.value` after each call (no coroutine dispatcher needed since writes are synchronous).

- **T3.1 `requestDeleteWave` on unused wave populates `deleteConfirm`**
  - Assert: `deleteConfirm.value` = `DeleteConfirmInfo(waveNumber, waveName)`; `deleteBlocked.value` = null.

- **T3.2 `requestDeleteWave` on in-use wave populates `deleteBlocked`**
  - Assert: `deleteBlocked.value` = `DeleteBlockedInfo(waveName, kitNames)`; `deleteConfirm.value` = null.

- **T3.3 `requestDeleteWave` on unknown wave is a no-op**
  - Assert: both flows remain null.

- **T3.4 `confirmDeleteWave` with no pending confirm is a no-op**
  - Arrange: no prior `requestDeleteWave`.
  - Act: `confirmDeleteWave()`.
  - Assert: nothing changes; no device mutation.

- **T3.5 `confirmDeleteWave` success path clears `deleteConfirm`**
  - Arrange: request → confirm.
  - Assert: `deleteConfirm.value` = null; `device.waves` no longer has the wave.

- **T3.6 `confirmDeleteWave` clears `selectedWave` if it was the deleted wave**
  - Arrange: select wave W, then request delete W, then confirm.
  - Assert: `selectedWave.value` = null.

- **T3.7 `confirmDeleteWave` leaves `selectedWave` alone if a different wave is selected**
  - Arrange: select wave A, request+confirm delete of B.
  - Assert: `selectedWave.value` still equals A.

- **T3.8 `confirmDeleteWave` surfaces DM `Error` through `deleteError`**
  - Arrange: force DM to return `Error` (e.g. read-only system dir).
  - Assert: `deleteError.value` = the error message; `deleteConfirm.value` = null.

- **T3.9 `confirmDeleteWave` race: wave became in-use between request and confirm**
  - Arrange: request delete (unused), then mutate a kit pad to reference the wave, then confirm.
  - Act: `confirmDeleteWave()`.
  - Assert: DM returns `InUse` → VM surfaces `deleteBlocked.value`; `deleteConfirm.value` = null.

- **T3.10 clear functions**
  - `clearDeleteConfirm`, `clearDeleteBlocked`, `clearDeleteError` each reset only their own flow to null.

---

### 4. Manual UI smoke tests

Run with `./gradlew desktopRun` against a real device folder.

- **M4.1** — Right-click a wave row in each sort mode (BY_NAME, BY_CATEGORY_NAME, BY_CATEGORY_NUM). Verify the **Delete Wave…** entry appears at the bottom of the context menu for each.

- **M4.2** — Right-click a category header. Verify **Delete Wave…** is **not** offered on headers.

- **M4.3** — Pick a wave known to be used by one or more kits (blue dot on the row). Click **Delete Wave…**. Verify the "Cannot delete wave" dialog lists the referencing kit names as plain bullets (not clickable). Dismiss; no files removed from `WAVE/PRM` or `WAVE/DATA`.

- **M4.4** — Pick an unused wave. Click **Delete Wave…**. Confirm the confirmation dialog shows the wave number + name and a destructive-styled **Delete** button. Click **Cancel** — verify the wave is still present.

- **M4.5** — Repeat M4.4 and click **Delete**. Verify:
  - The wave disappears from all three sort modes immediately.
  - `WAVE/PRM/<folder>/<file>.spd` and `WAVE/DATA/<path>` are gone on disk.
  - `SYSTEM/tag_list.spd` + `wavelist_*.spd` on disk no longer reference the wave (diff before/after).
  - If the deleted wave was the currently selected one in the right-hand detail panel, the detail panel clears.

- **M4.6** — After a successful delete, import a new wave via **Import Wave…**. Confirm the new wave's number is `max(existing) + 1` and does not reuse the deleted slot (gap-preserving behavior).

- **M4.7** — Delete a wave, then restart the app against the same folder. The deletion must persist: the wave is not reloaded from disk and the index files are consistent.

- **M4.8** — (Best-effort) Delete a wave whose `.wav` payload was pre-deleted out-of-band (e.g. via Finder). Verify the delete still succeeds silently and the PRM `.spd` + index files are updated.
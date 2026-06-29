## Feature: Extended Wave Operations (Rename, Move to Category)

### Summary
Right-click operations on a wave in the wave list: rename the wave (display name + on-disk
`.wav` + metadata) and move it to a different category. Both persist immediately, consistent
with the existing import/delete flows.

> Note: an earlier iteration also added drag-and-drop wave operations (drag-to-category and
> drag-to-renumber). That was reverted at the user's request — these operations are now
> context-menu only. The pre-existing drag of a wave from the list onto a pad is unchanged.

### Acceptance Criteria
- [ ] **Rename (right-click):** Wave context menu has "Rename Wave…" opening a dialog. Validates:
  non-empty, ≤12 chars, sanitized, unique among waves, differs from current.
- [ ] **Rename persistence:** On confirm, the in-memory wave name updates, the on-disk `.wav` file is
  renamed, and the wave `.spd` metadata + the four wavelist index files are rewritten. Disk writes
  happen before the in-memory commit; the `.wav` rename rolls back if metadata writing fails. A
  case-only rename works on case-insensitive filesystems.
- [ ] **Move to category (right-click):** Wave context menu has a "Move to: <Category>" entry for every
  category except the wave's current one; selecting one updates the wave's `tagRef`, rewrites the
  wave `.spd` and the index files, and persists.
- [ ] **No broken references:** Renaming/moving never breaks pad assignments (pads reference by number).
- [ ] On any failure, an "Operation failed" dialog reports the message and the in-memory state stays
  consistent with disk.

### Affected Layers
- **Model:** `WaveListsHolder.withRenamedWave`, `WaveListsHolder.withMovedWaveToCategory` (no new fields).
- **DeviceManager:** `renameWave`, `moveWaveToCategory` (disk-first, then commit memory). Shared
  `writeWaveListIndexFiles` / `writeWavePrmFile` / `wavePrmFile` helpers (also used to de-duplicate
  the existing import/delete/save index writes).
- **ViewModel (MainViewModel):** `renameWave`, `moveWaveToCategory`, `categoryOfWave`, `_waveOpError`.
- **Components:** shared `RenameDialog` (used by both category and wave rename); wave context menu
  extended with "Rename Wave…" and "Move to: <Category>"; `WaveOps` bundle.
- **Screen:** `ListsScreen` wires the menu + dialogs; `MainScreen` wires the callbacks.
- **Navigation:** No new screen.

### Mapping Reference
`Map_System.md` (categories / tag_list, wavelist index files) and the Wave model (number, name, tagRef, path).
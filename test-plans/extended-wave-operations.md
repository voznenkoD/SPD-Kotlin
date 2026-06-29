# Test Plan: Extended Wave Operations (Rename, Move to Category)

Feature spec: `features/extended-wave-operations.md`

Scope: right-click **Rename Wave** and **Move to Category** only. (Drag-based wave operations were
reverted; drag of a wave from the list onto a pad is a separate, pre-existing feature.)

This project has no automated test source set, so this is a manual test plan plus suggested unit
tests should a `desktopTest` source set be added later.

## Preconditions
- Launch the app: `./gradlew desktopRun`.
- Open a folder containing a valid SPD device export (with `SYSTEM/`, `KIT/`, `WAVE/`).
- Open the **Waves** tab in the right-hand list panel.
- Have at least two categories and several waves, with some waves assigned to pads.

---

## 1. Rename Wave (right-click)

| # | Steps | Expected |
|---|-------|----------|
| 1.1 | Right-click a wave → **Rename Wave…** | Rename dialog opens, pre-filled with current name. |
| 1.2 | Enter a new valid name (≤12 chars, unique) → **OK** | Dialog closes; the wave shows the new name in every view (By Name, By Category Name/Number). |
| 1.3 | Inspect disk: `WAVE/DATA/<folder>/` | The `.wav` file has been renamed to `<newName>.wav`; old filename gone. |
| 1.4 | Inspect disk: `SYSTEM/wavelist_*.spd` and the wave's `WAVE/PRM/<folder>/<file>.spd` | Index files and PRM reflect the new name. |
| 1.5 | Type a name that collides with another wave | Inline error "A wave with this name already exists"; **OK** disabled. |
| 1.6 | Clear the field | Error "Name cannot be empty"; **OK** disabled. |
| 1.7 | Field input | Cannot type beyond 12 characters. |
| 1.8 | Rename a wave that is assigned to a pad, then open that pad | Pad still references the same wave (by number) — assignment intact, name updated. |
| 1.9 | Rename changing only letter-case (e.g. "snare" → "Snare") on macOS | Rename succeeds (not rejected as a duplicate filename). |
| 1.10 | Reopen the device folder (re-read) | Renamed wave persists; no duplicate/phantom wave appears. |

## 2. Move Wave to Category (right-click)

| # | Steps | Expected |
|---|-------|----------|
| 2.1 | Right-click a wave | A **Move to: <Category>** entry appears for every category **except** the wave's current one. |
| 2.2 | Choose **Move to: <OtherCategory>** | Wave disappears from its old category group and appears under the chosen category (sorted by name in the Name view, by number in the Number view). |
| 2.3 | Inspect the wave's `WAVE/PRM` `.spd` `tag` value and `SYSTEM/wavelist_tagname.spd`/`wavelist_tagnum.spd` | Category membership updated on disk. |
| 2.4 | Re-read the device folder | Category membership persists. |
| 2.5 | A wave that is the only member of its source category | Source category becomes empty (still listed); target gains the wave. |

## 3. Failure handling & regression

| # | Steps | Expected |
|---|-------|----------|
| 3.1 | Trigger an op error (e.g. rename to a name that sanitizes to an existing one) | "Operation failed" dialog with a clear message; **OK** dismisses; list unchanged. |
| 3.2 | Drag a wave from the list onto a **pad** (kit selected) | Still assigns the wave to the pad (unchanged behaviour). |
| 3.3 | Drag-from-OS-filesystem `.wav` onto a pad | Still imports + assigns (unchanged). |
| 3.4 | Search filtering, category expand/collapse, "Used in:" navigation, category rename | All still work. |
| 3.5 | Selected wave detail (bottom panel) while renaming/moving the selected wave | Detail view stays in sync. |

## 4. Suggested unit tests (if a `desktopTest` source set is added)

`WaveListsHolder`:
- `withRenamedWave` updates the name in all three structures and re-sorts the by-name views.
- `withMovedWaveToCategory` removes from old category and inserts (sorted) into target; flat by-name
  list unchanged; no-op for unknown wave/category.

`DeviceManager` (with a temp device folder fixture):
- `renameWave`: rejects duplicate/blank; renames `.wav` on disk; updates PRM + index files; path
  field updated; rolls back the `.wav` rename if metadata writing fails; allows a case-only rename;
  commits in-memory state only after disk writes succeed.
- `moveWaveToCategory`: updates `tagRef`; no-op when already in target; rewrites PRM + index files;
  commits in-memory state only after disk writes succeed.
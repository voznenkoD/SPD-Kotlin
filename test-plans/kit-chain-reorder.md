# Test Plan: Reorder kit chain via drag-and-drop

Feature spec: `features/kit-chain-reorder.md`
Code under test:
- `viewmodel/SystemViewModel.kt` — `moveKitInChain(chainKey, from, to)`
- `ui/components/system/KitChainView.kt` — `KitChainList` drag-to-reorder
- `ui/screens/SystemScreen.kt` — wires `onMoveInChain`

## Scope
UI drag interaction + immutable reorder of `KitChain.kitRefs`. No model/parser
changes. Manual run is primary; the reorder math in `moveKitInChain` is pure and
could be unit-tested if a test source set is added (none exists today).

## Manual Test Cases

### TC1 — Basic reorder
1. `./gradlew desktopRun`, open a device folder, go to **System** screen, Kit Chain panel.
2. Select a chain tab (e.g. `A`) that has several kits.
3. **Long-press** a row, drag it up/down, release on a new position.
- [ ] During drag: the dragged row follows the cursor (raised) and the row under it shows the orange drop-target border / accent background.
- [ ] On release: the chain order reflects the move.
- [ ] The left-hand position numbers (`01`, `02`, …) renumber sequentially after the move.

### TC2 — Active chain only
1. Note the order of chain `B`.
2. Reorder chain `A`.
3. Switch to chain `B`.
- [ ] Chain `B` order is unchanged — only `A` was modified.

### TC3 — Persistence
1. Reorder a chain.
2. File → **Save**, then reopen the folder (or re-read the device).
- [ ] The new order persists after save/reload.

### TC4 — Missing-ref rows ("No Kit")
1. Find/produce a chain whose `kitRefs` includes an index with no matching kit.
- [ ] That entry renders as a row showing `No Kit` (it is NOT hidden).
- [ ] The `No Kit` row is itself draggable and can be reordered like any other row.
- [ ] Position numbering stays continuous across `No Kit` rows.

### TC5 — Edge cases
- [ ] Dragging a row and dropping it back on its original position leaves order unchanged (no-op; `from == to` guarded in `moveKitInChain`).
- [ ] Dragging the first row to the last position (and vice-versa) works and clamps within bounds.
- [ ] A chain with a single entry: long-press drag does nothing harmful.

### TC6 — No regression
- [ ] Tab switching between chains still works.
- [ ] Static rendering (no drag) is unchanged: `NN   kit#   name subName` format intact.

## Regression Checks
- [ ] `./gradlew compileKotlinDesktop` succeeds.
- [ ] Main Kit list drag-reorder (`KitListView`) still works (shared pattern, untouched).

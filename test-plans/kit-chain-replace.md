# Test Plan: Replace kit in kit chain via per-row dropdown

Code under test:
- `viewmodel/SystemViewModel.kt` — `setKitInChain(chainKey, index, newRef)`
- `ui/components/system/KitChainView.kt` — per-row `DropdownSelector`
- `ui/components/common/DropDownMenu.kt` — `DropdownSelector` now supports optional `label`, fill width (`width = null` + `modifier`)
- `ui/screens/SystemScreen.kt` — wires `onReplaceInChain`

## Scope
UI dropdown to set/replace the kit at each chain slot, plus a `No Kit` (-1) option.
Reuses the existing persistence path (`updateKitChains` → `updateSystemConfig`).

## Manual Test Cases

### TC1 — Replace a kit
1. `./gradlew desktopRun`, open a device, go to **System** → Kit Chain, select a chain tab.
2. Click a row's dropdown; the list shows `No Kit` plus all kits as `NNN  name sub`.
3. Pick a different kit.
- [ ] The row updates to show the chosen kit (number + name).
- [ ] Only that slot changes; the chain length/positions are otherwise unchanged.

### TC2 — Set to "No Kit"
1. Open a row dropdown and choose **No Kit**.
- [ ] The row shows `No Kit` with no kit number.
- [ ] Save → reload: the slot remains `No Kit` (persisted as the -1 convention; round-trips).

### TC3 — Assign a kit to a previously-empty slot
1. On a `No Kit` row, open the dropdown and choose a real kit.
- [ ] The slot now shows that kit and persists after save.

### TC4 — Active chain only
- [ ] Replacing a kit in chain A does not affect chains B–H.

### TC5 — Coexistence with drag-reorder
- [ ] Long-press the **position number** (left) and drag → row reorders (unchanged behavior).
- [ ] A normal click on the dropdown opens the menu (does not start a drag).

### TC6 — Many kits
- [ ] With a large kit count, the dropdown menu scrolls and all kits are selectable.

### TC7 — No regression to other dropdowns
- [ ] Other `DropdownSelector` usages (Setup, Pad, FX, Visual Control) still render their labels and selections normally (label-optional change is backward compatible).

## Regression Checks
- [ ] `./gradlew compileKotlinDesktop` succeeds.
- [ ] Kit chain drag-reorder still works.
# Test Plan: Duplicate Kit

Feature spec: `features/duplicate-kit.md`

## Scope
Right-click "Duplicate" in the Kits list deep-copies a kit, appends it to the end of the list,
selects + scrolls to it, applies the `+2` naming rule, and enforces the 99-kit cap via a reusable
"Maximum of kit numbers has been reached" dialog.

## Manual Test Cases

### 1. Duplicate menu item visible
1. Open a device folder, go to the Kits tab.
2. Right-click any kit row.
3. **Expect:** context menu shows "Copy", "Duplicate" (and "Paste" only when a kit was copied).

### 2. Basic duplicate appends to end
1. Note the current number of kits (N) and the selected kit's name (e.g. `SNARE`).
2. Right-click that kit → "Duplicate".
3. **Expect:**
   - A new kit appears as the **last** row (number N+1).
   - Its name is `SNARE2`; all other params (subName, tempo, volume, padLink, fx1, fx2, pads) match
     the source. Open the new kit and the source side-by-side to confirm pads/fx are identical.
   - The new kit is **selected** (highlighted) and the list has **scrolled** to it.

### 3. Name truncation at 8-char limit
1. Duplicate a kit whose name is 8 chars (e.g. `LONGNAME`).
2. **Expect:** new name is `LONGNAM2` (source truncated to 7 chars + `2`).
3. Duplicate a kit whose name is 7 chars (e.g. `SNAREXX`).
4. **Expect:** new name is `SNAREXX2` (8 chars, no truncation).

### 4. Repeated duplication appends another "2"
1. Duplicate the `SNARE2` kit produced in case 2.
2. **Expect:** new name is `SNARE22` (a `2` is always appended; truncate if it would exceed 8 chars,
   e.g. a `SNARE222`-length case truncates the source portion). No uniqueness enforcement — duplicate
   names are allowed.

### 5. Existing indices / KitChain references stay valid
1. Before duplicating, note a kit referenced in a Kit Chain (System screen) and a kit's list number.
2. Duplicate an arbitrary kit (not the last one).
3. **Expect:** numbers of all pre-existing kits are unchanged (append-only); any Kit Chain references
   still point to the same kits.

### 6. Kit-limit cap (99) blocks duplication
1. Reach 99 kits (duplicate repeatedly, or load a device already near the cap).
2. With exactly 99 kits, right-click a kit → "Duplicate".
3. **Expect:**
   - No new kit is added (count stays 99).
   - A dialog appears titled **"Maximum of kit numbers has been reached"** with an OK button.
   - Clicking OK dismisses it; the previous selection is unchanged.

### 7. Persistence (optional, save flow)
1. Duplicate a kit, then save the device (existing save flow).
2. Reload the folder.
3. **Expect:** the duplicated kit persists with its `+2` name and copied parameters.

## Suggested Automated Tests (JUnit, desktopTest)

- `DeviceManager.duplicateKit`:
  - appends a copy at the end and returns `kits.lastIndex`.
  - returns `null` when `sourceIndex` is out of range.
  - returns `null` and does **not** mutate the list when `kits.size == MAX_KITS` (99).
  - the appended kit equals the source except for `name`.
  - pre-existing kit indices are unchanged after the call.
- `MainViewModel.duplicateKit` naming rule (extract/verify `duplicateName` behavior):
  - `"SNARE"` -> `"SNARE2"`, `"LONGNAME"` (8) -> `"LONGNAM2"`, `"SNARE2"` -> `"SNARE22"`.
  - result is always <= 8 chars and always ends in `2`.
- `MainViewModel.duplicateKit` sets `selectedKitIndex` to the new index on success, and raises
  `kitLimitReached` (without changing selection) when the cap is hit; `clearKitLimitReached` resets it.

## Regression Checks
- Copy/Paste kit still works and is unaffected by the new menu item.
- Move-kit drag-and-drop and existing selection/scroll behavior on the Kits list still work.
- `./gradlew compileKotlinDesktop` succeeds.
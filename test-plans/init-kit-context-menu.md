# Test Plan: Init Kit (context-menu)

## Scope
Verifies the "Init kit" kit-list context-menu action: it resets the right-clicked kit in
place to the neutral defaults defined in `KitInitTemplate`, guarded by a confirmation dialog.

## Preconditions
- App built (`./gradlew desktopRun`).
- A device folder loaded so the Kits list is populated.

## Manual test cases

### 1. Menu item presence
1. Switch to the **Kits** tab.
2. Right-click any kit row.
3. **Expect:** context menu shows `Copy`, (`Paste` if a kit was copied), `Duplicate`, **`Init kit`**.

### 2. Confirmation dialog — cancel
1. Right-click kit #3 → **Init kit**.
2. **Expect:** dialog titled "Initialize kit?" mentioning "Kit 3", with **Initialize** / **Cancel**.
3. Click **Cancel** (or click outside).
4. **Expect:** dialog closes; kit #3 unchanged (name, pads, tempo, FX all as before).

### 3. Confirmation dialog — confirm resets in place
1. Pick a kit with a custom name and assigned pad waves; note its list position N.
2. Right-click it → **Init kit** → **Initialize**.
3. **Expect:**
   - Kit stays at the **same position N** (kit number unchanged; not appended to the end).
   - Name becomes **`INIT`**, sub-name empty.
   - Tempo **154.0**, level/volume **100**.
   - All 15 pads empty (no main/sub wave); MIDI note numbers run **60..74** across PAD_1..FS_2.
   - Pad link is **PAD_3 ↔ FS_1**.
   - FX1 and FX2 are **on**, type 0, params 0.
   - The kit becomes the selected kit (detail view reflects the reset).

### 4. KitChain references stay valid
1. In **System**, point a KitChain slot at kit N.
2. Init kit N (case 3).
3. **Expect:** the KitChain slot still references position N (no shift/corruption), now showing the INIT kit.

### 5. Persistence semantics
1. Init a kit, then **do not** save.
2. Re-open the same device folder (Choose folder).
3. **Expect:** original kit returns (init was in-memory only).
4. Repeat, this time **Save**, then reopen.
5. **Expect:** the INIT kit persists.

### 6. Defaults are source-defined, not file-based
1. Confirm the app initializes a kit correctly even if `tmp/init-kit.spd` is absent/renamed.
2. **Expect:** Init kit still works (values come from `KitInitTemplate`, not the .spd).

## Automated test suggestions (JUnit, desktopTest)
- `KitInitTemplate.create()` returns a `Kit` with: `name == "INIT"`, `subName == ""`,
  `tempo == 154.0`, `volume == 100`, `padLink == (PAD_3, FS_1)`, 15 pads, all pads with no
  main/sub wave, and MIDI note numbers 60..74 in `PadNumber` order.
- Round-trip: `KitInitTemplate.create().toRaw()` produces a `KitPrm` whose `Nm*` decode back to
  "INIT", `Tempo == 1540`, `Level == 100`, `Fx1Sw == 1`, `Fx2Sw == 1`, and every `PadPrm.Wv == -1`.
- `DeviceManager.initKit(index)` replaces only the kit at `index` (others unchanged) and keeps
  `device.kits.size` constant.
- `MainViewModel.requestInitKit` sets `initKitConfirm`; `confirmInitKit` clears it, resets the kit,
  and selects `index`; `clearInitKitConfirm` cancels without mutating the kit.
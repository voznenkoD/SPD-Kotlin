## Feature: Choose/Change Kit FX Type

### Summary
Add a dropdown selector to the Kit FX1/FX2 view that lets the user change the effect type (e.g., from THRU to REVERB). Currently the FX type is shown as read-only text — this feature makes it editable.

### Acceptance Criteria
- [ ] KitFXView shows a `DropdownSelector` for FXType instead of static text
- [ ] Selecting a new FX type replaces the current FxEffect with a new instance of the chosen type, initialized with default parameters (all zeros)
- [ ] The FX enable/disable switch (`SyncSwitch`) is preserved when changing types
- [ ] After changing the FX type, the parameter editor updates to show the new type's editable parameters
- [ ] Changes persist via the existing `KitViewModel.updateFx1/updateFx2` flow and save correctly

### Affected Layers
- **Model:** No changes — `KitFX`, `FxEffect`, `FXType` already exist
- **DeviceManager:** No changes — `updateKit()` already handles full KitFX replacement
- **ViewModel:** No changes — `updateFx1(fx: KitFX)` / `updateFx2(fx: KitFX)` already accept complete KitFX objects
- **Components:** Modify `KitFxView.kt` — replace the FX type text label with a `DropdownSelector<FXType>` that, on selection, creates a new `KitFX(sw, FxEffect.fromValues(newType.value, List(20) { 0 }))` and calls `onFxChange`
- **Screen:** No changes
- **Navigation:** No

### Mapping Reference
`Map_Kit.md` — Fx1Type / Fx2Type fields

### Notes
- FX switch stays in its current state (ON/OFF) when changing type

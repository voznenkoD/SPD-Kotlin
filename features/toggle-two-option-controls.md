## Feature: Toggle Switch For Two-Option Controls

### Summary
Replace all `ButtonRow` calls that have exactly 2 enum entries with a `SwitchWithLabel`-style toggle, and generalize `SwitchWithLabel` to accept custom label strings so both the existing `SyncSwitch` toggles and the new 2-option enums share the same component. The switch shows option labels on each side (off-label on left, on-label on right).

### Acceptance Criteria

**Component**
- [ ] `SwitchWithLabel` is generalized: the existing `(label, syncSwitch, onValueChange)` signature still works with defaults so all 8 existing call sites compile unchanged.
- [ ] A new generic overload handles any 2-entry `Enum<T>`: `ToggleSwitchWithLabel(label: String, selectedItem: T, offItem: T, onItem: T, onItemSelected: (T) -> Unit, offLabel: String = offItem.name, onLabel: String = onItem.name)`.
- [ ] The switch displays the label above, with the off-label text on the left of the switch and the on-label text on the right of the switch (e.g. `[Soft]  ◯═══  [Hard]`). The currently-selected side is visually emphasized (e.g. bolder or primary color).
- [ ] The existing `SwitchWithLabel` for `SyncSwitch` is updated to match the same layout: `OFF` on left, `ON` on right of the switch, label above.

**Replacements (21 call sites with 2-entry enums)**
- [ ] `SyncSwitch` (OFF/ON) ButtonRow calls → `ToggleSwitchWithLabel` with `offLabel = "OFF"`, `onLabel = "ON"`. Affects: PadModeView (:44), FilterView (:53), StereoDelayView (:37), FlangerView (:24), SlicerView (:36), StepPhaserView (:34, :43), StepFlangerView (:25, :34), PhaserView (:34).
- [ ] `DelayType` (NORMAL/PAN) → `offLabel = "Normal"`, `onLabel = "Pan"`. Affects: SyncDelayView (:25), StereoDelayView (:28), DelayEffectView (:35).
- [ ] `Knee` (SOFT/HARD) → `offLabel = "Soft"`, `onLabel = "Hard"`. Affects: CompressorView (:71).
- [ ] `WahMode` (LPF/BPF) → `offLabel = "LPF"`, `onLabel = "BPF"`. Affects: TouchWahView (:24).
- [ ] `Polarity` (DOWN/UP) → `offLabel = "Down"`, `onLabel = "Up"`. Affects: TouchWahView (:33), RingModView (:25).
- [ ] `TrigType` (SHOT/ALT) → `offLabel = "Shot"`, `onLabel = "Alt"`. Affects: PadModeView (:35).
- [ ] `PolyMono` (MONO/POLY) → `offLabel = "Mono"`, `onLabel = "Poly"`. Affects: PadModeView (:53).
- [ ] `ExternalControl` (OFF/ON) → defaults. Affects: MidiParamsView (:40).
- [ ] `SLoopMode` (MANUAL/AUTO) → `offLabel = "Manual"`, `onLabel = "Auto"`. Affects: SLoopEffectView (:34).
- [ ] `SLoopTiming` (FIRST_HALF/SECOND_HALF) → `offLabel = "1st Half"`, `onLabel = "2nd Half"`. Affects: SLoopEffectView (:83).
- [ ] All existing `SwitchWithLabel` call sites (8 total in PadDetailsScreen, SetupMidiView, SetupGeneralView) continue working unchanged.
- [ ] `ButtonRow` itself is **not deleted** — still serves 3+-entry enums.

**Display format**
- [ ] The label line shows `"$label: ${selectedItem.name}"` (e.g. `"Knee: SOFT"`), using the enum's `.name` property.

### Affected Layers
- **Model:** No changes.
- **DeviceManager:** No changes.
- **ViewModel:** No changes.
- **Components:**
  - **Modified:** `SwitchWithLabel.kt` — update layout to show off/on labels on each side of the switch. Add a generic `ToggleSwitchWithLabel` composable for 2-entry enums.
  - **Modified (call-site swaps):** ~15 files with 2-entry ButtonRow calls → `ToggleSwitchWithLabel`.
  - **Preserved:** `ButtonRow.kt` unchanged.
- **Screen:** No changes.
- **Navigation:** No new screen.

### Key Design Decisions (confirmed by user)
- **Label format:** `"$label: ${selectedItem.name}"` — uses enum `.name` (e.g. `"Knee: SOFT"`), not custom label strings.
- **Visual layout:** Two labels on each side of the switch — off-label on the left, on-label on the right, with the active side visually emphasized.
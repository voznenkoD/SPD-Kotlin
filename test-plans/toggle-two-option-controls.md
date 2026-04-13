## Test Plan: Toggle Switch For Two-Option Controls

Covers the feature specified in `features/toggle-two-option-controls.md`.

---

### 1. Component: ToggleSwitchWithLabel

- **T1.1** Label displays `"$label: ${selectedItem.name}"` above the switch.
- **T1.2** Off-label text appears left of the switch, on-label text appears right.
- **T1.3** When `selectedItem == offItem`, the off-label is bold/primary; on-label is muted.
- **T1.4** When `selectedItem == onItem`, the on-label is bold/primary; off-label is muted.
- **T1.5** Toggling the switch calls `onItemSelected` with the correct enum value.
- **T1.6** Custom `offLabel`/`onLabel` strings override the default `enum.name`.

### 2. Component: SwitchWithLabel (backward compatibility)

- **T2.1** Existing signature `(label, syncSwitch, onValueChange: (Boolean))` still compiles and works.
- **T2.2** Shows OFF/ON labels on each side. Toggling calls `onValueChange(true/false)`.

### 3. Manual UI smoke tests

Run `./gradlew desktopRun` against a real device folder.

- **M3.1 PadModeView** — Trigger Type shows "Shot / Alt" toggle. Dynamics shows "OFF / ON". PolyMono shows "Mono / Poly". Template (4 entries) and Loop (5 entries) remain as ButtonRows.
- **M3.2 MidiParamsView** — External Control shows "OFF / ON" toggle.
- **M3.3 CompressorView** — Knee shows "Soft / Hard" toggle. Ratio remains as ButtonRow.
- **M3.4 TouchWahView** — Wah Mode shows "LPF / BPF" toggle. Polarity shows "Down / Up" toggle.
- **M3.5 RingModView** — Polarity shows "Down / Up" toggle.
- **M3.6 FX SyncSwitch toggles** — FilterView, StereoDelayView, FlangerView, PhaserView, SlicerView, StepPhaserView (×2), StepFlangerView (×2) all show "OFF / ON" toggles for their sync switches.
- **M3.7 StereoDelayView** — DelayType shows "Normal / Pan" toggle.
- **M3.8 SyncDelayView** — DelayType shows "Normal / Pan" toggle.
- **M3.9 Master FX DelayEffectView** — DelayType shows "Normal / Pan" toggle.
- **M3.10 Master FX SLoopEffectView** — Mode shows "Manual / Auto" toggle. Timing shows "1st Half / 2nd Half" toggle.
- **M3.11 Setup views** — Existing SwitchWithLabel calls (Tempo Indication, Pad Lock, Local Control, Soft Thru, USB MIDI Thru, MIDI PC/CC Control, Tempo Sync) still render correctly with OFF/ON labels on each side.
- **M3.12 Value persistence** — Toggle a switch, save device, reload. Value persists.

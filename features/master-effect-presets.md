# Feature: Master-Effect Presets

## Summary
Link each master effect's **Preset** selector to its parameters: selecting a preset applies that
preset's documented structural values; editing a structural parameter away from the selected
preset's values resets the Preset to index 0. Applies to FILTER, DELAY, S.LOOP (FX excluded).

## Decisions (resolved at requirements gate)
- Deviation literally sets the preset field to **index 0** (first enum: SIMPLE_LPF / STEREO_NORMAL /
  MANUAL). No synthetic "Custom" state, no re-matching against other presets.
- A preset's "values" = **only the structural switches** documented per preset in
  `documentation/master-fx-presets.md`. Continuous params are never part of a preset.
- Scope: **Filter, Delay, S.Loop**. FX is untouched.

## Defining (structural) values per preset — the only things a preset sets / watches

**FILTER** — defining set: `{type, rateSync}`
| Preset (value) | FilterType | rateSync |
|---|---|---|
| SIMPLE_LPF (0) | LOW_PASS | OFF |
| SIMPLE_BPF (1) | BAND_PASS | OFF |
| SIMPLE_HPF (2) | HIGH_PASS | OFF |
| LPF_SYNC_MOD (3) | LOW_PASS | ON |
| BPF_SYNC_MOD (4) | BAND_PASS | ON |
| HPF_SYNC_MOD (5) | HIGH_PASS | ON |

**DELAY** — defining set: `{type, syncSW, delayTime-note}`
| Preset | DelayType | syncSW | note (DelayTimeEnum) |
|---|---|---|---|
| STEREO_NORMAL (0) | NORMAL | OFF | — (numeric ms) |
| STEREO_SYNC (1) | NORMAL | ON | (synced; note not pinned) |
| PAN_QUARTER (2) | PAN | ON | QUARTER (1/4, idx 7) |
| PAN_DOTTED_EIGHT (3) | PAN | ON | EIGHTH_DOTTED (1/8., idx 6) |
| PAN_DOUBLE (4) | PAN | ON | EIGHTH_DOTTED (1/8., idx 6) + double (not modeled) |

**S.LOOP** — defining set: `{mode, rateSync, rate-note}`
| Preset | mode | rateSync | note (SLoopRateEnum) |
|---|---|---|---|
| MANUAL (0) | MANUAL | OFF | (knob) |
| AUTO_QUARTER (1) | AUTO | ON | QUARTER (1/4, idx 3) |
| AUTO_EIGHT (2) | AUTO | ON | EIGHTH (1/8, idx 6) |
| AUTO_SIXTEEN (3) | AUTO | ON | SIXTEENTH (1/16, idx 8) |
| AUTO_FREERUN (4) | AUTO | OFF | (free numeric) |

Continuous params (filter slope / mod-rate-value / mod-depth / LFO; delay tap-time / low-cut /
high-cut / direct-level; s.loop timing) are **not** part of any preset — editing them never changes
the Preset.

## Acceptance Criteria
- [ ] Selecting a preset sets that effect's structural params to the table values above and leaves
      all continuous params unchanged. Sync transitions convert the rate representation correctly
      (note ↔ numeric), mirroring the existing sync-switch behavior.
- [ ] Editing a **structural** control so the effect's defining values no longer equal the
      **currently-selected** preset's defining values sets the Preset to index 0. Editing it back to
      match does not.
- [ ] Editing a **continuous** control never changes the Preset.
- [ ] Behavior is identical for Filter, Delay, S.Loop. FX is untouched.
- [ ] Round-trips through `MEfctPrm` (save/load) unchanged; preset persisted as its int value.

## Affected Layers
- **Model:** add pure, tested logic to `FilterEffect` / `DelayEffect` / `SLoopEffect` (or their
  preset enums): `applyPreset(preset)` returning a copy with defining values set, and a single
  source-of-truth map of preset → defining values used to evaluate deviation. No `MEfctPrm` change.
- **DeviceManager:** none new — reuse existing master-effect update path.
- **ViewModel:** existing master-FX update flow (no new pattern).
- **Components:** `FilterEffectView` / `DelayEffectView` / `SLoopEffectView` — Preset control calls
  `applyPreset`; structural controls additionally apply the deviation→0 rule via the model helper.
- **Screen / Navigation:** none.

## Mapping Reference
`Map_System.md` (master FX) + `documentation/master-fx-presets.md`.

## Known limitation
`PAN_DOUBLE` and `PAN_DOTTED_EIGHT` have identical modeled defining values (doubling isn't a stored
parameter), so applying either yields the same params and deviation can't distinguish them.

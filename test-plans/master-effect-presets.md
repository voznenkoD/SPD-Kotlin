# Test Plan: Master-Effect Presets

## Scope
Selecting a master-effect preset (Filter/Delay/S.Loop) applies its structural values; editing a
structural control away from the selected preset snaps the Preset to index 0; editing continuous
controls never changes the Preset. FX is untouched.

## Preconditions
- App running (`./gradlew desktopRun`), a device loaded, System screen → Master Effect panel.

## Manual cases

### FILTER
1. Select preset **BPF+SYNC MOD**. Expect: Type=BAND PASS, sync switch ON. Continuous controls
   (slope, mod depth, LFO) unchanged from before.
2. Select **SIMPLE HPF**. Expect: Type=HIGH PASS, sync OFF.
3. With **SIMPLE HPF** selected, change Type→LOW PASS. Expect: Preset snaps to index 0 (SIMPLE LPF).
4. With a preset selected, change only Slope / Mod Depth / LFO Wave. Expect: Preset unchanged.
5. Toggle the sync switch. Expect: Preset snaps to 0 unless the new (type,sync) still equals the
   selected preset's values.

### DELAY
1. Select **PAN (quarter)**. Expect: Type=Pan, sync ON, Delay Time note = 1/4.
2. Select **PAN (dotted 8th)**. Expect: Type=Pan, sync ON, note = 1/8.
3. Select **STEREO NORMAL**. Expect: Type=Normal, sync OFF, numeric ms delay time field shown.
4. On **PAN (quarter)**, change Delay Time note → 1/8. Expect: Preset snaps to STEREO NORMAL (0).
5. On any preset, change Tap Time / Direct Level / Low Cut / High Cut. Expect: Preset unchanged.
6. Note: **PAN DOUBLE** applies the same params as PAN (dotted 8th) — selecting it shows Pan/ON/1/8
   (doubling isn't a modeled parameter); this is the documented limitation.

### S.LOOP
1. Select **AUTO (sixteenth)**. Expect: Mode=Auto, sync ON, Rate note = 1/16.
2. Select **MANUAL**. Expect: Mode=Manual.
3. Select **AUTO FREERUN**. Expect: Mode=Auto, sync OFF, numeric rate field.
4. On **AUTO (quarter)**, change Mode→Manual. Expect: Preset snaps to MANUAL (0).
5. Change Timing (1st/2nd half). Expect: Preset unchanged (continuous).

### Persistence
1. Pick presets across the three effects, **Save**, reopen the folder. Expect: params and the
   persisted preset index reload unchanged (round-trip through MEfctPrm).

### FX untouched
1. Confirm the FX tab behaves exactly as before (no preset-linking changes).

## Automated test suggestions (JUnit, desktopTest) — pure model, no UI
- `FilterEffect.applyPreset(p)` sets (type,rateSync) per the table for all 6 presets and leaves
  slope/modDepth/lfoWave unchanged.
- `DelayEffect.applyPreset(PAN_QUARTER)` → type=PAN, syncSW=ON, delayTime=EnumTime(QUARTER);
  `applyPreset(STEREO_NORMAL)` → IntTime preserved; tapTime/cuts/directLevel unchanged.
- `SLoopEffect.applyPreset(AUTO_EIGHT)` → mode=AUTO, rateSync=ON, rate=EnumRate(EIGHTH).
- `reconcilePreset()`:
  - returns `this` when the effect still matches its selected preset;
  - returns `copy(preset = first)` when a structural value differs;
  - is unaffected by continuous-only differences.
- Round-trip: for every preset, `applyPreset(p)` then `reconcilePreset()` keeps preset == p.
- `PAN_DOUBLE` and `PAN_DOTTED_EIGHT` both match the same Pan/ON/1/8 effect (documented limitation).

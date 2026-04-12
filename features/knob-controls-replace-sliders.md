## Feature: Knob Controls Replace Sliders

### Summary
Replace all horizontal `Slider`-based controls (`SliderWithLabel`, `IntStepSliderWithLabel`, `PadPanSlider`, `ClickPanSlider`) with rotary knob controls that display a colored arc following the value. Two knob modes: **unipolar** (arc fills from ~7 o'clock clockwise to ~5 o'clock) and **bipolar** (arc fills from 12 o'clock toward either direction). Value text is click-to-edit for precise input.

### Acceptance Criteria

**Knob component**
- [ ] New `KnobControl` composable in `ui/components/common/KnobControl.kt` that draws a circular knob via `Canvas` with: a background track arc (~270°, from 7 o'clock to 5 o'clock), a colored fill arc representing the current value, and a small indicator dot/line at the current position.
- [ ] **Unipolar mode** — fill arc starts at 7 o'clock (left-bottom, ~225° or -135° from top) and sweeps clockwise proportional to value within the range. Used for all parameters including those with negative-only ranges (e.g. compressor threshold `-80..0`) — the arc simply maps min→max to left→right regardless of sign.
- [ ] **Bipolar mode** — fill arc starts at 12 o'clock (top center) and sweeps clockwise for positive values, counter-clockwise for negative values. Used for pan and any control whose semantic zero is in the center of the range (EQ gain dB, tone, bottom, etc.).
- [ ] Dragging **vertically** on the knob changes the value (drag up = increase, drag down = decrease).
- [ ] Integer-step knobs (replacing `IntStepSliderWithLabel`) snap to whole values during and after drag.
- [ ] **Label** is displayed **above** the knob.
- [ ] **Value** is displayed **below** the knob. Clicking the value opens an inline `TextField` for precise numeric input. Pressing Enter or losing focus commits the value (clamped to the valid range). Pressing Escape cancels editing and reverts to the previous value.
- [ ] Knob graphic size is **64×64 dp**. Total component height including label + value is roughly ~100–110 dp.
- [ ] Filled arc and indicator use the theme's **primary color** for both unipolar and bipolar modes. Unfilled arc track is a muted gray.

**Drop-in replacements**
- [ ] `SliderWithLabel` call sites are replaced with `KnobControl` — unipolar for all ranges except those explicitly needing bipolar (see below).
- [ ] `IntStepSliderWithLabel` call sites are replaced with `KnobControl` (integer step, unipolar).
- [ ] `PadPanSlider` call sites are replaced with `KnobControl` (bipolar), preserving the internal `-15..+15` mapping and `PadPan` display text (L15, L14…C…R14, R15).
- [ ] `ClickPanSlider` call sites are replaced with `KnobControl` (bipolar), same `-15..+15` logic and `ClickPan` display text.
- [ ] `DryWetMixSlider` is left **as-is** for now (dual Dry/Wet display doesn't map to a single knob).
- [ ] All existing `onValueChange` / `onPanChange` callbacks and value types are preserved — only the visual representation changes.

**Bipolar mode usage** — apply bipolar mode to these specific parameters (semantic zero at center):
- Pan controls (`PadPanSlider`, `ClickPanSlider`)
- EQ gains: Low/Mid/High Gain (dB) ranges like `-12f..12f`, `-15f..15f`
- Effect parameters: Tone (`-50f..50f`), Bottom (`-50f..50f`)
- All other controls (including compressor threshold `-80f..0f`, attack `1f..1000f`, etc.) use **unipolar**.

**Layout optimisation**
- [ ] Where multiple knobs appear together (FX views, SoundSection, AudioView, PadSetupView, etc.), arrange them in a **`FlowRow`** (or wrapping row) instead of a vertical `Column`. This lets 2–4 knobs sit side-by-side per row and uses horizontal space efficiently.
- [ ] The wrapping is responsive — if the container is narrow, knobs wrap to the next line automatically.
- [ ] Views that only have 1–2 controls can stay in a `Column` — no forced grid.

### Affected Layers
- **Model:** No changes.
- **DeviceManager:** No changes.
- **ViewModel:** No changes.
- **Components:**
  - **New:** `ui/components/common/KnobControl.kt` — shared knob composable.
    - Parameters: `label: String`, `value: Float`, `onValueChange: (Float) -> Unit`, `valueRange: ClosedFloatingPointRange<Float>`, `mode: KnobMode` (enum: `Unipolar`, `Bipolar`), `steps: Int? = null` (null = continuous), `valueDisplay: String? = null` (override for formatted display like `PadPan.toString()`, defaults to `value.toInt().toString()`).
  - **Modified (call-site swaps):** All files currently calling `SliderWithLabel`, `IntStepSliderWithLabel`, `PadPanSlider`, or `ClickPanSlider`:
    - `SoundSection.kt` — Volume (unipolar 0–100), Pan (bipolar via PadPan)
    - `KitScreen.kt` — Pan (unipolar 0–100), Volume (unipolar 0–100) *(note: the KitScreen "Pan" at 0–100 is not a true L/R pan, keep unipolar)*
    - `AudioView.kt` — Audio/USB/Sub Out Volume (unipolar 0–100), Low/Mid/High Gain dB (bipolar -12..12)
    - `ClickView.kt` — Pan (bipolar), Level (unipolar 0–100)
    - `CompressorView.kt` — Threshold (unipolar -80..0), Attack (unipolar 1–1000), Release (unipolar 1–1000), Makeup Gain (unipolar 0–30), Ratio/Knee (keep as-is if they are dropdowns)
    - `ChorusView.kt` — Rate, Pre-Delay, Direct Level (unipolar 0–100)
    - `DistortionView.kt` — Drive (unipolar 0–100), Bottom (bipolar -50..50), Tone (bipolar -50..50), Effect Level (unipolar 0–100)
    - `PhaserView.kt` — all effect parameters (unipolar unless bipolar range)
    - `ReverbView.kt` — all effect parameters (unipolar)
    - `TapeEchoView.kt` — all parameters (unipolar)
    - `EqView.kt` — Low/Mid/High Gain (bipolar -12..12 or similar), frequencies (unipolar)
    - `RingModView.kt` — Low/High Gain (bipolar -15..15), Level (unipolar 0–100)
    - `FilterView.kt`, `FlangerView.kt`, and any other FX views — swap accordingly
    - `PadSetupView.kt` — Sensitivity (unipolar 1–32), Threshold (unipolar 0–31), Scan Time, Retrig Cancel, Mask Time, Xtalk Cancel (all unipolar)
    - `SetupMidiView.kt` — MIDI CC values (unipolar 0–127)
    - Also swap layouts from `Column` to `FlowRow` where multiple knobs appear.
  - **Preserved:** `DryWetMixSlider` unchanged. Old slider components can be kept but will have zero usages (or deleted if preferred).
- **Screen:** No structural screen changes. Layout improvements happen within components.
- **Navigation:** No new screen.

### Mapping Reference
`Map_Pad.md` (pan), `Map_Kit.md` (volume), `Map_System.md` (audio levels, EQ gains, click pan).

### Key Design Decisions (confirmed by user)
- **Interaction:** Vertical drag only (up = increase, down = decrease). No circular drag.
- **Knob size:** 64×64 dp for the graphic.
- **Negative-only ranges (e.g. -80..0):** Unipolar — the arc just maps min→max. Bipolar is reserved for controls with semantic center-zero.
- **Arc color:** Same primary color for both unipolar and bipolar. The arc direction is the visual cue, not color.
- **Value display:** Label above, click-to-edit value below. Enter commits (clamped), Escape reverts.
- **Layout:** Optimise with `FlowRow` wrapping for multi-knob views.
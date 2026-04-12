## Test Plan: Knob Controls Replace Sliders

Covers the feature specified in `features/knob-controls-replace-sliders.md`. The change replaces all horizontal sliders with rotary knob controls across ~25 files, adds a new `KnobControl` composable, and re-flows multi-knob layouts into `FlowRow`.

---

### 1. Unit: KnobControl rendering logic

Pure computation tests — no UI needed.

- **T1.1 Unipolar fraction** — value at min → fraction 0, value at max → fraction 1, value at midpoint → fraction 0.5.
- **T1.2 Bipolar arc** — for range `-15f..15f`: value 0 → arc at center (12 o'clock), value 15 → full clockwise, value -15 → full counter-clockwise.
- **T1.3 Bipolar with asymmetric range** — for range `-64f..63f`: value 0 → center is at fraction `64/127`, arc sweeps from that center.
- **T1.4 Step snapping** — range `0f..100f` with steps=100: drag that lands at 42.7 snaps to 42.0.
- **T1.5 commitEdit clamping** — typed value "150" with range `0f..100f` → clamped to 100. Typed "-5" → clamped to 0. Typed "abc" → no-op (value unchanged).

---

### 2. Composable: KnobControl interaction

Use `createComposeRule()`.

- **T2.1 vertical drag up increases value** — render knob at value 50 (range 0–100). Simulate drag up 125px (~half range). Assert `onValueChange` was called with value > 50.
- **T2.2 vertical drag down decreases value** — same setup, drag down. Assert value < 50.
- **T2.3 drag clamps at max** — start at 95, drag up aggressively. Assert value does not exceed 100.
- **T2.4 drag clamps at min** — start at 5, drag down aggressively. Assert value does not go below 0.
- **T2.5 click value opens editor** — click the value text. Assert `BasicTextField` appears with the current value pre-filled.
- **T2.6 Enter commits edited value** — type "42", press Enter. Assert `onValueChange(42f)` was called.
- **T2.7 Escape cancels edit** — enter edit mode, type "99", press Escape. Assert `onValueChange` was NOT called and the display reverts to the original value.
- **T2.8 focus loss commits** — enter edit mode, type "30", click outside. Assert value committed.

---

### 3. Composable: KnobControl visual modes

Screenshot/golden-image tests (optional) or semantic assertions.

- **T3.1 Unipolar at zero** — range `0f..100f`, value 0. The filled arc should be absent (or negligibly small). The indicator dot should be at the 7 o'clock position.
- **T3.2 Unipolar at max** — value 100. The filled arc should span the full 270°. The indicator dot at 5 o'clock.
- **T3.3 Bipolar at zero** — range `-15f..15f`, value 0. No filled arc from center. Indicator at 12 o'clock.
- **T3.4 Bipolar positive** — value 10. Filled arc from 12 o'clock clockwise. Indicator past 12 o'clock toward 3 o'clock.
- **T3.5 Bipolar negative** — value -10. Filled arc from 12 o'clock counter-clockwise. Indicator past 12 o'clock toward 9 o'clock.
- **T3.6 Label and value display** — label text "Volume" appears above, value text "50" appears below.
- **T3.7 Custom valueDisplay** — `valueDisplay = "L5"` overrides the numeric display.

---

### 4. Integration: SliderWithLabel / IntStepSliderWithLabel delegation

- **T4.1 SliderWithLabel renders a knob** — render `SliderWithLabel(label="Vol", value=50f, valueRange=0f..100f, ...)`. Assert a Canvas is present (no Slider).
- **T4.2 SliderWithLabel bipolar flag** — render with `bipolar = true`. Assert the bipolar arc behavior (center-zero fill).
- **T4.3 IntStepSliderWithLabel integer snapping** — render with range `0..10`, step `1`. Drag to approximate 3.7. Assert callback receives `3` (integer snap).
- **T4.4 PadPanSlider bipolar + custom display** — render with `PadPan(5)`. Assert value display shows "R5" (PadPan.toString()), not "5".
- **T4.5 ClickPanSlider bipolar + custom display** — same pattern as T4.4 but with `ClickPan`.

---

### 5. Layout: FlowRow wrapping

- **T5.1 multiple knobs wrap** — render 4 knobs in a FlowRow at a container width of 200dp (fits ~2 knobs at 80dp each). Assert the third knob is rendered below the first two.
- **T5.2 wide container fits all** — 400dp container. All 4 knobs on one row.

---

### 6. Manual UI smoke tests

Run `./gradlew desktopRun` against a real device folder.

- **M6.1 Kit screen** — Open a kit. Verify Tempo and Volume appear as knobs side by side (not full-width sliders). Drag each knob vertically to change values. Click the value text to enter a precise number.

- **M6.2 Pad details** — Select a pad. SoundSection shows Volume and Pan as knobs. Pan knob uses bipolar mode (fill from center). Verify PadPan display text (L15…C…R15).

- **M6.3 FX views** — Switch to each FX type (Compressor, Distortion, Chorus, EQ, Reverb, Tape Echo, Phaser, Flanger, Filter, RingMod, Pitchshift, StepPhaser, StepFlanger, SyncDelay, StereoDelay, Isolator, FiltDrive, Slicer, TouchWah). Verify:
  - All parameters appear as knobs (no horizontal sliders).
  - Knobs are arranged in FlowRow (side by side, wrapping as needed).
  - DropdownSelectors and ButtonRows are NOT inside FlowRow.
  - EQ gains, tone, pan-like parameters use bipolar mode (fill from center).
  - Threshold (-80..0), attack, release, levels use unipolar mode (fill from left).
  - DryWetMixSlider (RingMod balance) is unchanged (still a horizontal slider).

- **M6.4 System Audio** — Open System screen. Audio volume knobs (Audio In, USB In, Sub Out) appear in a row. System EQ gains are bipolar knobs. Dropdowns remain as dropdowns.

- **M6.5 System Click** — Click pan knob is bipolar. Level knob is unipolar.

- **M6.6 Setup Pads** — Sensitivity, Threshold, Scan Time, Retrig Cancel, Mask Time, Xtalk Cancel appear as unipolar knobs in rows.

- **M6.7 Setup MIDI** — MIDI CC knobs (0–127 range) appear as unipolar knobs.

- **M6.8 Setup General** — LCD Contrast and Brightness appear as unipolar knobs side by side.

- **M6.9 Click-to-edit** — On any knob, click the value text. Verify a text field appears. Type a number and press Enter — value updates. Type an out-of-range number — value is clamped. Press Escape — edit cancels.

- **M6.10 Drag precision** — On a large-range knob (e.g. Attack 1–1000), verify drag sensitivity feels usable. On a small-range knob (e.g. Tape Distortion 0–5), verify fine control is possible.

- **M6.11 Value persistence** — Change a knob value via drag or click-to-edit. Save the device. Reload. Verify the value persists correctly.
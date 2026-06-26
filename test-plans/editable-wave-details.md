# Test Plan: Editable Wave Details

Feature file: `features/editable-wave-details.md`
Implementation:
- `ui/screens/WaveDetailsScreen.kt` (knobs + layout + marker wiring)
- `ui/components/wave/WaveParamKnobs.kt` (the five knobs)
- `ui/waveform/WaveFormWithGridDisplay.kt` (Start/End markers + drag)
- `viewmodel/MainViewModel.kt` (`updateSelectedWave`)
- `ui/screens/MainScreen.kt` (callback wiring)
- `ui/theme/DesignTokens.kt` (`ColorAccentGreen`)

## Setup
1. `./gradlew desktopRun`, open a folder with an SPD device export.
2. Select a wave (Waves tab → click a wave). The WaveDetails panel shows the waveform + knobs.

## Manual Test Cases

### 1. Read-only fields
- [ ] Number, Name, Path are shown and NOT editable (plain text).

### 2. Tempo knob (raw = BPM×10)
- [ ] Tempo knob shows BPM with one decimal (e.g. raw 1200 → "120.0").
- [ ] Drag changes value within 20.0–250.0; cannot exceed bounds.
- [ ] Click the value, type e.g. `145.5`, Enter → shows 145.5; reselecting the wave keeps it (memory).
- [ ] Verify stored raw is BPM×10 (after Save, the .spd Tempo = round(bpm×10)).

### 3. Beat / Measure knobs
- [ ] Beat snaps to integers 1–16.
- [ ] Measure snaps to integers 1–10.
- [ ] Typed values out of range clamp to the nearest bound.

### 4. Start / End knobs
- [ ] Start range is 0…End; cannot exceed End.
- [ ] End range is Start…totalSamples; cannot exceed total samples or go below Start.
- [ ] Editing Start above End is prevented (clamped to End); editing End below Start is prevented.

### 5. Waveform markers
- [ ] A GREEN vertical line marks Start, an ORANGE vertical line marks End, both thicker than the red playhead.
- [ ] Marker positions match knob values (move a knob → marker moves).
- [ ] Markers stay correct after zoom (+/-).

### 6. Draggable markers
- [ ] Drag the green marker → Start updates (knob + value follow); cannot pass the End marker.
- [ ] Drag the orange marker → End updates; cannot go below Start.
- [ ] Grabbing near a marker picks the nearest one.
- [ ] Dragging is smooth and does not get cancelled mid-drag by the value updates.

### 7. Live update + no auto-save
- [ ] Each change is reflected immediately in the panel (knob + marker + value).
- [ ] Changes are NOT written to disk automatically — only an explicit Save persists them.
  - Verify: edit a parameter, do NOT save, inspect the `WAVE/PRM/**.spd` file → unchanged.
  - Then Save (existing save action) → file reflects the new values.
- [ ] The detached bottom panel (if undocked) edits the same wave and stays in sync.

### 8. Playback unaffected
- [ ] Play/Stop and the red playhead still work; playhead animates independently of the Start/End markers.

### 9. Edge cases
- [ ] Empty/zero-length wave (totalSamples 0): no markers drawn, no crash, knobs still render.
- [ ] Locale: typing tempo with a comma (e.g. `120,0`) is accepted (parsed as 120.0).

### 10. Robustness fixes from review
- [ ] **No crash on short/missing audio:** select a wave whose stored Start/End exceed the decoded sample count (or a shorter replaced WAV); turning/typing the Start/End knobs must NOT crash (ranges are clamped to a non-empty range).
- [ ] **Out-of-range params don't mis-draw:** a wave with tempo `0` (or beat/measure `0`) shows the knob clamped to its min, not a broken arc; the stored value is only changed if you actually edit it.
- [ ] **Drag threshold preserves scroll:** zoom in (+) so the waveform is wider than the panel; dragging on empty waveform area (>12px from either marker) pans/scrolls the waveform instead of moving a marker. Dragging within ~12px of a marker moves that marker.
- [ ] **Coincident markers:** when Start == End, dragging right from that point moves End (widens), dragging left moves Start.
- [ ] **No re-decode lag:** dragging a marker or spinning a knob is smooth — the WAV is not re-decoded per tick (decode is keyed on the file path only).

## Notes
- `totalSamples = waveformData.samples.size`; markers at `canvasWidth × value/totalSamples`.
- `updateSelectedWave` updates the device wave in memory and refreshes the selected-wave StateFlow; it never calls `saveDevice()`.
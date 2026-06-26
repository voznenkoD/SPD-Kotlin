## Feature: Editable Wave Details

### Summary
Make the WaveDetails screen's wave parameters editable via knobs (Tempo, Beat, Measure, Start, End),
with Start/End also drawn as colored, draggable markers over the waveform alongside the playhead.
Number/Name/Path stay read-only. Edits are kept in memory immediately; persistence happens only
through the existing explicit central save (`DeviceManager.saveDevice()`) — no auto-save.

### Acceptance Criteria
- [ ] **Tempo** — `KnobControl`, range **20.0–250.0 BPM**, displayed with one decimal. Stored raw = `round(BPM × 10)`; display = `raw / 10`. (e.g. raw 1200 ⇄ 120.0 BPM.)
- [ ] **Beat** — `KnobControl`, integer range **1–16**.
- [ ] **Measure** — `KnobControl`, integer range **1–10**.
- [ ] **Start** — `KnobControl`, integer samples, range `0 … end`; enforce `start ≤ end`.
- [ ] **End** — `KnobControl`, integer samples, range `start … totalSamples` where `totalSamples = waveformData.samples.size`.
- [ ] **Start/End markers over the waveform** (`DisplayWaveformWithGrid`): vertical lines at `canvasWidth × (value / totalSamples)`, **thicker than the 2px red playhead** (≈4px), **Start = green**, **End = orange**. The red playhead remains.
- [ ] **Markers are draggable**: dragging the green marker updates Start, the orange marker updates End (x→sample = `round((dragX / canvasWidth) × totalSamples)`), clamped so `0 ≤ start ≤ end ≤ totalSamples`. Knob and marker stay in sync (both edit the same value).
- [ ] **Number, Name, Path** remain read-only text.
- [ ] On every parameter change (knob drag, knob value entry, or marker drag) the Wave is updated **in memory immediately**; the visible/selected wave reflects the change live.
- [ ] **No auto-save.** Changes persist to disk only when the user invokes the existing explicit save (which calls `DeviceManager.saveDevice()`).
- [ ] Grid/layout reworked for good readability following the UI theme & `/ui-guidelines`: knobs laid out in a row/grid with consistent heights, `Spacing`, and theme colors (`ColorAccentOrange`, etc.); waveform retains full width above.

### Affected Layers
- **Model:** none — `Wave` already has `tempo, beat, measure, start, end`.
- **DeviceManager:** reuse `updateWave(waveNumber, transform)`; no new persistence path. `saveDevice()` stays the only writer, invoked explicitly by the user.
- **ViewModel:** `MainViewModel` — add `updateSelectedWave(transform: (Wave) -> Wave)` that (a) updates the device wave via `updateWave`, and (b) refreshes `_selectedWave` so the screen reflects the edit. Does NOT call `saveDevice()`.
- **Components:**
  - Reuse `KnobControl` (label, value: Float, onValueChange, valueRange, steps, valueDisplay, parseInput).
  - Extend `DisplayWaveformWithGrid` to accept `start`, `end`, `totalSamples`, and `onStartChange`/`onEndChange` callbacks; draw green/orange markers and handle drag hit-testing on/near a marker.
  - Optionally a `WaveParamKnobs` row component to host the five knobs.
- **Screen:** `WaveDetailsScreen` — replace read-only Tempo/Beat/Measure/Start/End text with knobs; accept an `onWaveChange: (Wave) -> Unit` callback (wired in `MainScreen` to `mainViewModel.updateSelectedWave`); reflow the grid (waveform full-width on top; knob row(s) below; keep zoom/play controls).
- **Navigation:** none.

### Mapping Reference
`Map_Kit.md` (tempo BPM context). Wave params persist as `WAVE/PRM/<folder>/<file>.spd` via `wave.toRaw()` inside `saveDevice()`.

### Decisions (resolved)
1. Tempo stored raw = BPM × 10; knob in BPM 20.0–250.0, one-decimal display.
2. Beat = knob, integer 1–16.
3. Start/End = knobs **and** draggable markers on the waveform.
4. Persistence only on explicit central save; edits otherwise live in memory (no auto-save / no debounce).

### Notes for build
- `selectedWave` is currently a snapshot `StateFlow<Wave?>`; it MUST be refreshed when an edit occurs, otherwise the UI/knobs won't reflect changes. Hence `updateSelectedWave`.
- `totalSamples = waveformData.samples.size`; start/end are sample indices in that same domain.
- Marker stroke ≈ 4px (playhead is 2px). Start green, End orange (use theme greens/oranges; `ColorAccentOrange` for End is available — pick a green consistent with the theme for Start).
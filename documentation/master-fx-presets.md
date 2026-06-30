# Roland SPD-SX — Master Effect Reference

Reference for the SPD-SX's four master effects (**FILTER**, **DELAY**, **S.LOOP**, **FX**), their selectable TYPEs ("
presets"), and the parameters each TYPE configures.

Source: *Roland SPD-SX Effect Guide* (© 2011 Roland Corporation). Parameter values are edited under **MSTR FX EDIT** (
quick menu in the MASTER EFFECT screen).

---

## How "presets" work

Selecting a master-effect **TYPE** behaves like the pad **TEMPLATE** parameter: it sets the underlying parameters to
suitable values for that type. The differences Roland defines explicitly between TYPEs are the **structural switches** —
filter shape, sync on/off, note division, pan/double, mode. Continuous parameters (cutoff, resonance, feedback, levels,
depths) are left as live-tweakable starting points and are mapped to the two real-time control knobs.

- Only **one** master effect can be active at a time.
- Master effects apply to **all sounds in the current kit**, not a single pad.
- `[CONTROL 1]` and `[CONTROL 2]` knobs vary the active effect in real time.

> **Note on exact values:** Roland's Effect Guide documents parameter *ranges* and what each TYPE *does*, but does **not
** publish a grid of the exact numeric value each preset writes into every continuous parameter. Columns below reflect
> the structural values the manual defines; continuous values are shaped via the CONTROL knobs.

---

## FILTER

Applies a filter effect. The six TYPEs differ by **filter shape** (LPF / BPF / HPF) and **whether tempo-synced
modulation is on**.

| TYPE (preset) | Filter shape | RATE SYNC SW | Modulation   | Description                                   |
|---------------|--------------|--------------|--------------|-----------------------------------------------|
| SIMPLE LPF    | LOW PASS     | OFF          | none         | Passes only the low-frequency range           |
| SIMPLE BPF    | BAND PASS    | OFF          | none         | Passes only a specific frequency range        |
| SIMPLE HPF    | HIGH PASS    | OFF          | none         | Passes only the high-frequency range          |
| LPF+SYNC MOD  | LOW PASS     | ON           | tempo-synced | Low-pass with modulation synced to kit tempo  |
| BPF+SYNC MOD  | BAND PASS    | ON           | tempo-synced | Band-pass with modulation synced to kit tempo |
| HPF+SYNC MOD  | HIGH PASS    | ON           | tempo-synced | High-pass with modulation synced to kit tempo |

**Editable parameters (MSTR FX EDIT):**

| Parameter    | Value range                    | Explanation                                                    |
|--------------|--------------------------------|----------------------------------------------------------------|
| TYPE         | LOW PASS, BAND PASS, HIGH PASS | Filter type (set by preset)                                    |
| SLOPE        | -12, -24, -36 dB               | Damping per octave: -36 extremely steep, -24 steep, -12 gentle |
| RATE SYNC SW | OFF, ON                        | ON = MOD RATE specified as a note (synced to tempo)            |
| MOD RATE     | 0–100, or note                 | Rate of modulation                                             |
| MOD DEPTH    | 0–100                          | Depth of modulation                                            |
| LFO WAVE     | TRI, SINE, SAW, SQR            | Modulation waveform: triangle, sine, sawtooth, square          |

**Knob mapping:** CONTROL 1 = cutoff frequency · CONTROL 2 = filter resonance level

---

## DELAY

Applies a delay effect. The five TYPEs differ by **sync on/off**, **note division**, and **pan / doubling**.

| TYPE (preset)    | Delay TYPE | SYNC SW | Note division      | Description                                     |
|------------------|------------|---------|--------------------|-------------------------------------------------|
| STEREO NORMAL    | NORMAL     | OFF     | — (ms-based)       | A stereo delay                                  |
| STEREO SYNC      | NORMAL     | ON      | tempo-synced       | Delay synced to current kit tempo               |
| PAN (quarter)    | PAN        | ON      | quarter note       | Tap delay, quarter-note delay panned L/R        |
| PAN (dotted 8th) | PAN        | ON      | dotted eighth note | Tap delay, dotted eighth-note delay panned L/R  |
| PAN DOUBLE       | PAN        | ON      | dotted eighth note | Dotted eighth-note delay panned L/R and doubled |

**Editable parameters (MSTR FX EDIT):**

| Parameter    | Value range        | Explanation                                           |
|--------------|--------------------|-------------------------------------------------------|
| TYPE         | NORMAL, PAN        | NORMAL = simple delay; PAN = delay time panned to L/R |
| SYNC SW      | OFF, ON            | ON = delay time specified as a note value             |
| TIME         | 0–1300 ms, or note | The time by which the sound is delayed                |
| TAP TIME     | 0–100%             | L-channel delay relative to R-channel (taken as 100%) |
| LOW CUT      | FLAT–800 Hz        | Low-cut frequency; FLAT = not applied                 |
| HIGH CUT     | 700 Hz–FLAT        | High-cut frequency; FLAT = not applied                |
| DIRECT LEVEL | 0–100              | Volume of the direct sound                            |

**Knob mapping:** CONTROL 1 = delay volume · CONTROL 2 = feedback (proportion fed back to input)

---

## S.LOOP (Short Looper)

Loops a short interval of the input sound. While sound plays, pressing `[S.LOOP]` captures the sound at that moment as a
short loop. TYPEs differ by **MODE (manual vs auto)** and the **RATE note division** for the auto variants.

| TYPE (preset)    | MODE   | RATE            | Description                                             |
|------------------|--------|-----------------|---------------------------------------------------------|
| MANUAL           | MANUAL | set by knob     | Short loop turned on/off manually                       |
| AUTO (quarter)   | AUTO   | quarter note    | Loop turns on/off within a quarter note                 |
| AUTO (eighth)    | AUTO   | eighth note     | Loop turns on/off within an eighth note                 |
| AUTO (sixteenth) | AUTO   | sixteenth note  | Loop turns on/off within a sixteenth note               |
| AUTO FREERUN     | AUTO   | free (RATE-set) | Loop turns on/off within the interval specified by RATE |

**Editable parameters (MSTR FX EDIT):**

| Parameter    | Value range        | Explanation                                                      |
|--------------|--------------------|------------------------------------------------------------------|
| MODE         | MANUAL, AUTO       | MANUAL = on/off manually; AUTO = on/off automatically            |
| RATE SYNC SW | OFF, ON            | ON = rate specified as a note                                    |
| RATE         | 0–127, or note     | Rate at which the short loop repeats                             |
| TIMING       | 1ST HALF, 2ND HALF | When MODE = AUTO: loop first or second half of the RATE interval |

**Knob mapping:** CONTROL 1 = short-loop rate · CONTROL 2 = short-loop volume

---

## FX (assignable slot)

The `[FX]` button applies the effect of a selected TYPE drawn from the full effect palette. For each FX TYPE, the same
parameters as the **Kit Effects** are available (see Kit Effect List in the Effect Guide). Parameters mapped to CONTROL
1/CONTROL 2 are not shown in the detailed-settings screen.

| TYPE         | Description                                 | CONTROL 1             | CONTROL 2              |
|--------------|---------------------------------------------|-----------------------|------------------------|
| THRU         | No effect applied                           | —                     | —                      |
| STEREO DELAY | A stereo delay                              | Delay volume          | Feedback amount        |
| SYNC DELAY   | Delay synced to kit tempo                   | Delay volume          | Feedback amount        |
| TAPE ECHO    | Classic tape echo (RE-201 style)            | Tape speed            | Echo repetition amount |
| CHORUS       | Chorus                                      | Chorus volume         | Modulation depth       |
| FLANGER      | Stereo flanger (jet-like resonance)         | Modulation rate       | Modulation depth       |
| STEP FLANGER | Flanger pitch changes in steps              | Modulation rate       | Pitch-change rate      |
| PHASER       | Stereo phaser                               | Modulation rate       | Modulation depth       |
| STEP PHASER  | Phaser changes in steps                     | Modulation rate       | Stepwise-change rate   |
| EQUALIZER    | Tonal shaping                               | —                     | —                      |
| COMPRESSOR   | Evens out volume (limits loud, boosts soft) | —                     | —                      |
| FILTER       | Filter effect                               | Cutoff frequency      | Cutoff change rate     |
| FILT+DRIVE   | Low-pass filter + overdrive                 | Cutoff frequency      | Overdrive amount       |
| ISOLATOR     | EQ that drastically cuts frequency bands    | Low-range boost/cut   | High-range boost/cut   |
| TOUCH WAH    | Wah driven by performance volume            | Filter sensitivity    | Wah center frequency   |
| DISTORTION   | Distortion with long sustain                | Direct/effect balance | —                      |
| RING MOD     | AM modulation → bell-like sounds            | Modulation depth      | Modulation frequency   |
| PITCH SHIFT  | Pitch shifter                               | Pitch-shift amount    | Feedback proportion    |
| VIBRATO      | Vibrato                                     | Vibrato rate          | Vibrato depth          |
| REVERB       | Reverberation                               | Reverb volume         | —                      |
| SLICER       | Rhythmic gating of the sound                | Cut rate              | —                      |

---

## Note values (for SYNC parameters)

When a parameter's `RATE SYNC SW` (or `SYNC SW`) is **ON**, the value is set as a note and synced to the current kit
tempo. Available note values:

Thirty-second · Sixteenth · Eighth-note triplet · Dotted sixteenth · Eighth · Quarter-note triplet · Dotted eighth ·
Quarter · Half-note triplet · Dotted quarter · Half · Dotted half · Whole

> When `SYNC SW` is OFF, the relevant parameter takes a numerical value instead. Available note values differ by effect
> type. For delay time set as a note, there is an upper limit equal to the maximum numerical delay time — slowing the
> tempo past that point will not lengthen the delay further.

---

*Reference compiled from the Roland SPD-SX Effect Guide (© 2011 Roland Corporation). For full per-type kit-effect
parameters, see the Kit Effect List in Roland's Effect Guide PDF (downloadable from Roland support — search model "
SPD-SX").*

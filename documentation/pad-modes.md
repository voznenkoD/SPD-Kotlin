# Roland SPD-SX — Pad Mode / Template Reference

Reference for how each pad plays its wave: the **TEMPLATE** "presets" (SINGLE / PHRASE / LOOP), the **MODE** parameters
they configure, and the related **MAIN** parameters on the KIT PAD screen.

Source: *Roland SPD-SX Owner's Manual* (© 2011 Roland Corporation), pp. 44–45. Settings live under **MENU → KIT PAD →
MODE** (and **MAIN**).

---

## How "presets" work

The pad play type is a parameter called **TEMPLATE**. Selecting a template sets the **MODE** parameters to suitable
values — i.e. it's a shortcut that configures the underlying switches, which you can then adjust individually.

- **SINGLE** — for sounds used to play single notes, such as percussion.
- **PHRASE** — for phrases that have a tempo.
- **LOOP** — for waves you want to sound repeatedly.

> **Note on exact values:** The original SPD-SX manual documents the MODE *parameters* and the *templates* separately;
> it does **not** print a grid of the exact value each template assigns to each parameter. The table below reflects
> standard SPD-SX behaviour. The **LOOP** and **TRIG TYPE** rows are the ones that genuinely define the difference between
> the three modes; **DYNAMICS** and **POLY/MONO** are sensible defaults you'll usually adjust per sample.

---

## TEMPLATE → MODE parameter values

| Parameter | SINGLE | PHRASE | LOOP                   |
|-----------|--------|--------|------------------------|
| LOOP      | OFF    | OFF    | ON                     |
| TRIG TYPE | SHOT   | ALT    | SHOT (ALT also common) |
| DYNAMICS  | ON     | OFF    | OFF                    |
| POLY/MONO | POLY   | MONO   | MONO                   |

---

## MODE parameters (full ranges)

Set under **MENU → KIT PAD → MODE**.

| Parameter | Value range          | Explanation                                                                            |
|-----------|----------------------|----------------------------------------------------------------------------------------|
| TEMPLATE  | SINGLE, PHRASE, LOOP | Master selector; sets the MODE parameters to suitable values                           |
| LOOP      | OFF, ON, x2, x4, x8  | Number of repeats. ON = repeats continuously; x2/x4/x8 = fixed finite loop counts      |
| TRIG TYPE | SHOT, ALT            | SHOT = wave plays each time you hit the pad; ALT = alternately plays or stops each hit |
| DYNAMICS  | OFF, ON              | ON = volume follows playing velocity; OFF = constant volume each hit                   |
| POLY/MONO | MONO, POLY           | MONO = each new hit silences the previous sound; POLY = hits layer on previous sound   |

**Template behaviour at a glance:**

- **SINGLE** — plays once from start to end, then stops. One hit = one full playback; hitting again retriggers from the
  start. Velocity-sensitive one-shots, great for percussion, vocal stabs, SFX.
- **PHRASE** — alternating hits start and stop the wave (toggle). Useful for backing parts where you want variable
  length each time.
- **LOOP** — plays repeatedly until hit again (or stops after a fixed x2/x4/x8 count). Best for ambient beds and rhythm
  parts running under a section/song.

---

## Related MAIN parameters (KIT PAD → MAIN)

These apply across all three templates and are set per pad.

| Parameter  | Value range    | Explanation                                                                                                                         |
|------------|----------------|-------------------------------------------------------------------------------------------------------------------------------------|
| WAVE       | 00001–10000    | Selects the wave played by the pad                                                                                                  |
| VOLUME     | 0–100          | Pad volume (also settable from the top screen)                                                                                      |
| PAN        | L15–CENTER–R15 | Stereo position (left/right balance)                                                                                                |
| MUTE GROUP | OFF, 1–9       | Pads sharing a number cut each other off; only the most recently played pad in the group is heard                                   |
| TEMPO SYNC | OFF, ON        | ON = wave playback speed follows performance tempo (matters most for PHRASE / LOOP). Drastic speed changes may affect sound quality |

---

## SUB wave (second wave per pad)

A pad can play two waves simultaneously (WAVE + SUB WAVE). SUB parameters (KIT PAD → SUB):

| Parameter | Value range    | Explanation                                |
|-----------|----------------|--------------------------------------------|
| SUB       | 00001–10000    | Selects the second wave sounded by the pad |
| VOLUME    | 0–100          | Volume of the sub wave                     |
| PAN       | L15–CENTER–R15 | Stereo position of the sub wave            |

---

## Voice / polyphony note

The SPD-SX can play **eight simultaneous stereo samples (16 voices)**. Once the maximum is reached, it "robs" a voice
from another currently playing sound to play the new one — which may cut off the first sound you triggered (e.g. a click
or backing track). Use **MONO** mode where appropriate to conserve voices and keep low-end sounds (like kicks) clean.

---

*Reference compiled from the Roland SPD-SX Owner's Manual (© 2011 Roland Corporation), KIT PAD section pp. 44–45. The
TEMPLATE→parameter mapping reflects standard device behaviour; LOOP and TRIG TYPE are the manual-defined distinctions,
DYNAMICS and POLY/MONO are typical defaults adjusted per sample.*

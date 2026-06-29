# Test Plan: Pad Type Presets

Feature spec: `features/pad-type-presets.md`

This project has no automated test source set, so this is a manual test plan plus suggested unit
tests should a `desktopTest` source set be added later.

## Preconditions
- Launch the app: `./gradlew desktopRun`.
- Open a folder containing a valid SPD device export.
- Select a kit, select a pad, open the pad **Mode** section (`PadModeView`) showing Template / Loop /
  Trigger Type / Dynamics / PolyMono controls.

## 1. Preset application per template

| # | Steps | Expected |
|---|-------|----------|
| 1.1 | Set Template = **Single** | Loop=Off, Trigger Type=Shot, Dynamics=On, PolyMono=Poly. |
| 1.2 | Set Template = **Phrase** | Loop=Off, Trigger Type=Alt, Dynamics=Off, PolyMono=Mono. |
| 1.3 | Set Template = **Loop** | Loop=On, Trigger Type=Alt, Dynamics=Off, PolyMono=Mono. |
| 1.4 | All controls update together when the template is clicked | One visible update; no half-applied state. |

## 2. NONE behaviour & manual edits

| # | Steps | Expected |
|---|-------|----------|
| 2.1 | From any state, set Template = **NONE** | Only the template changes; Loop/TrigType/Dynamics/PolyMono are unchanged. |
| 2.2 | Apply a preset (e.g. Single), then manually change Dynamics to Off | The manual change sticks; it is NOT reverted until the template is changed again. |
| 2.3 | After a manual tweak, click the SAME template again | No-op — the manual tweak is preserved (re-selecting the current template does not re-apply the preset). |
| 2.4 | Load a pad with Loop=x4, then click the LOOP template (already on LOOP) | No-op — the x4 multiplier is preserved (idempotent re-selection). |

## 3. Persistence & isolation

| # | Steps | Expected |
|---|-------|----------|
| 3.1 | Apply a preset, File → Save, reopen the folder | Pad mode params persisted as set by the preset. |
| 3.2 | Apply a preset on pad A, then inspect pad B | Pad B is unaffected (per-pad edit). |
| 3.3 | Apply a preset, do NOT save, reopen | Original pad mode restored (in-memory until save — consistent with other pad edits). |

## 4. Suggested unit test (if a `desktopTest` source set is added)

`PadMode.withTemplate(template)`:
- SINGLE → (OFF, SHOT, ON, POLY) + template SINGLE.
- PHRASE → (OFF, ALT, OFF, MONO) + template PHRASE.
- LOOP → (ON, ALT, OFF, MONO) + template LOOP.
- NONE → only `template` changes; loop/trigType/dynamics/polyMono equal the receiver's values.
- Pure function: does not mutate the receiver (returns a new instance).
## Feature: Pad Type Presets

### Summary
When the pad's Template switch (Single / Phrase / Loop) changes, automatically set Loop, Trigger Type, Dynamics, and Poly/Mono to a fixed preset for that type, in the same single update.

### Acceptance Criteria
- [ ] Selecting **Single** sets: Loop=Off, TrigType=Shot, Dynamics=On, PolyMono=Poly (+ template=Single).
- [ ] Selecting **Phrase** sets: Loop=Off, TrigType=Alt, Dynamics=Off, PolyMono=Mono.
- [ ] Selecting **Loop** sets: Loop=On, TrigType=Alt, Dynamics=Off, PolyMono=Mono.
- [ ] All four params + template update in one `onPadModeChange` call (UI reflects all immediately).
- [ ] Selecting **NONE** changes only the template; the other four params are left unchanged.
- [ ] The individual Loop / TrigType / Dynamics / PolyMono controls remain manually editable after a preset is applied (a later manual tweak is not overwritten until the template is changed again).
- [ ] Change is in-memory via `updatePadMode`/`updatePad`, persisted on the existing File → Save.

### Affected Layers
- **Model:** add a pure mapping `PadMode.withTemplate(template)` returning a `PadMode` with the derived Loop/TrigType/Dynamics/PolyMono per the table below; `NONE` returns the current mode with only `template` changed. Uses existing enums; no new fields.
- **DeviceManager:** none new.
- **ViewModel (PadViewModel):** none new (existing `updatePadMode`).
- **Components:** `PadModeView` — the Template selector's `onItemSelected` calls `onPadModeChange(padMode.withTemplate(newTemplate))`.
- **Screen / Navigation:** none.

### Preset mapping
| Template | Loop | TrigType | Dynamics | PolyMono |
|----------|------|----------|----------|----------|
| SINGLE   | OFF  | SHOT     | ON       | POLY     |
| PHRASE   | OFF  | ALT      | OFF      | MONO     |
| LOOP     | ON   | ALT      | OFF      | MONO     |
| NONE     | (unchanged) | (unchanged) | (unchanged) | (unchanged) |

### Mapping Reference
`Map_Pad.md` → Mode (Template, Loop, TrigType, Dynamics, Poly/Mono).

### Decisions (resolved)
- NONE leaves the four params unchanged (only template changes).
- Presets overwrite current values on template change; individual controls stay manually editable afterward.
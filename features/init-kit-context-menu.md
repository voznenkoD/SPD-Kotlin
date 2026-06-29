# Feature: Init Kit (context-menu)

## Summary
Add an **"Init kit"** item to the kit-list right-click context menu that resets the
clicked kit (in place, keeping its list position / kit number) to a set of neutral
default values defined in source code. A confirmation dialog guards the overwrite.

## Acceptance Criteria
- [ ] Right-clicking a kit in the kit list shows an **"Init kit"** item (alongside Copy / Paste / Duplicate).
- [ ] Choosing it opens a **confirmation dialog** ("Initialize kit N? This replaces its current settings.") with confirm/cancel.
- [ ] On confirm, the kit **at that same index** is replaced with the init defaults — index and KitChain references stay valid; on cancel nothing changes.
- [ ] Init defaults are defined in a **dedicated source object** (e.g. `KitInitTemplate`), not read from any `.spd` file at runtime, with named constants/comments making the parameter↔model relationship clear.
- [ ] Changes are in-memory only (persist on the user's existing Save), consistent with Duplicate/Paste.

## Init default values (neutral, modeled on tmp/init-kit.spd)
- **Name:** `"INIT"` · **Sub-name:** empty
- **Tempo:** `154.0` · **Volume/Level:** `100`
- **Pad link:** `PAD_3 ↔ FS_1` (from the example's LinkPad0=2 / LinkPad1=13)
- **FX1:** on, type 0, all params 0 · **FX2:** on, type 0, all params 0 (Fx2Asgn 0)
- **15 pads** (PAD_1…FS_2): all empty (no main/sub wave), `NoteNum` 60…74 incrementing,
  with the example's empty-pad defaults (WvLevel 100, WvPan 15, PlayMode 0, TrigType 0,
  Dynamics 1, VoiceAsgn 1, GateTime -1, etc.). No references to waves 314/315.

## Affected Layers
- **Model:** new `KitInitTemplate` object (under `model/kit/`) producing a default `Kit` from named constants.
- **DeviceManager:** `initKit(index)` (or reuse `updateKit(index, …)`) to reset in place.
- **ViewModel:** `MainViewModel` — `requestInitKit(index)` + `_initKitConfirm` StateFlow + `confirmInitKit()` / `clearInitKitConfirm()` (mirrors the existing wave-delete-confirm pattern).
- **Components:** `KitListView` new `onInitKit` callback + menu item; a small confirm dialog (reuse existing dialog style); thread `onInitKit` through `ListsScreen`.
- **Screen:** `MainScreen` wires the new callbacks; no new screen/navigation.

## Mapping Reference
`Map_Kit.md` (kit-level params, FX, pad link, per-pad PadPrm).

## Decisions (resolved)
- Reset the right-clicked kit **in place** (not append).
- **Neutral** defaults (structure preserved, no waves assigned, name "INIT").
- **Confirmation dialog** before overwriting.

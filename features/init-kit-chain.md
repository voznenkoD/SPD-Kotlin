## Feature: Init Kit Chain Button

### Summary
Add an **Init** button next to the selected kit chain's title in the Kit Chain panel that, after a confirmation dialog, resets all 20 slots of the **currently selected** chain (A–H) to "No Kit" (`-1`), leaving the chain name intact.

### Acceptance Criteria
- [ ] In `KitChainView`, the selected chain's name title row shows an **Init** button to its right (label/title on the left, button on the right).
- [ ] Clicking **Init** opens a confirmation `AlertDialog` ("Initialize kit chain?" with explanatory text and Initialize / Cancel).
- [ ] Confirming sets every one of the selected chain's 20 `kitRefs` to `-1` (No Kit); the chain `name` is unchanged; other chains (A–H) are untouched.
- [ ] After confirm, the panel immediately reflects all slots as "No Kit"; the change is in-memory via `updateSystemConfig` (persisted on the existing File → Save, consistent with all other system edits).
- [ ] Cancel/dismiss makes no change.
- [ ] Button matches the design system (compact `Button`/`TextButton` with theme tokens, `Heights.button`).

### Affected Layers
- **Model:** none (uses existing `KitChain.kitRefs`, `-1` = No Kit).
- **DeviceManager:** none new (existing `updateSystemConfig`).
- **ViewModel (SystemViewModel):** add `initializeKitChain(chainKey: Char)` → sets that chain's `kitRefs` to `List(20) { -1 }`, keeps name, delegates to `updateKitChains`.
- **Components:** `KitChainView` — wrap the chain-name title in a `Row` with the Init button; add confirmation dialog (local `remember` state, mirroring the `DeleteWaveConfirmDialog` pattern); add an `onInitChain: (Char) -> Unit` callback param.
- **Screen:** `SystemScreen` — pass `onInitChain = systemViewModel::initializeKitChain` to `KitChainView`.
- **Navigation:** none.

### Mapping Reference
`Map_System.md` (Kit Chain parameters).

### Decisions (resolved)
- Init resets only the currently selected chain (A–H tab), not all 8.
- The chain name is preserved; only the 20 kit slots become "No Kit".

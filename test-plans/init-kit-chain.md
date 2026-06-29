# Test Plan: Init Kit Chain Button

Feature spec: `features/init-kit-chain.md`

This project has no automated test source set, so this is a manual test plan plus suggested unit
tests should a `desktopTest` source set be added later.

## Preconditions
- Launch the app: `./gradlew desktopRun`.
- Open a folder containing a valid SPD device export (`SYSTEM/`, `KIT/`, `WAVE/`).
- Go to the **System** screen and open the **Kit Chain** panel (right region).
- Make sure the selected chain (a tab A–H) has several slots pointing at real kits.

## 1. Button presence & placement

| # | Steps | Expected |
|---|-------|----------|
| 1.1 | Look at the selected chain's title row | The chain name is on the left; an **Init** button is on the right of the same row. |
| 1.2 | Switch between tabs A–H | The Init button stays present for each chain; the title shows that chain's name. |

## 2. Confirmation dialog

| # | Steps | Expected |
|---|-------|----------|
| 2.1 | Click **Init** | A confirmation dialog "Initialize kit chain?" appears, naming the chain (e.g. "chain B …"), with **Initialize** and **Cancel**. |
| 2.2 | Click **Cancel** (or click outside) | Dialog closes; the chain is unchanged. |
| 2.3 | Click **Init** then **Initialize** | Dialog closes; every slot of the selected chain now shows "No Kit". |

## 3. Initialization behaviour

| # | Steps | Expected |
|---|-------|----------|
| 3.1 | After confirming on chain X | All 20 slots of chain X read "No Kit"; the chain **name is unchanged**. |
| 3.2 | Switch to another chain (Y) | Chain Y is **unaffected** (its kit assignments remain). |
| 3.3 | Edit a slot back to a real kit after init | Works normally (existing dropdown behaviour intact). |

## 4. Persistence

| # | Steps | Expected |
|---|-------|----------|
| 4.1 | After init, File → Save, then reopen the folder | The initialized chain's slots are all "No Kit"; the name persisted; other chains intact. |
| 4.2 | After init, do NOT save, reopen the folder | Original chain is restored (init was in-memory only until save — consistent with other system edits). |

## 5. Suggested unit test (if a `desktopTest` source set is added)

`SystemViewModel.initializeKitChain(chainKey)`:
- Sets every `kitRefs` entry of the target chain to `-1`, preserves `name`, preserves slot count.
- Leaves all other chains' `kitRefs` unchanged.
- No-op when the chain key is absent or no device/config is loaded.
- Calls through `updateKitChains` → `deviceManager.updateSystemConfig` (in-memory update).
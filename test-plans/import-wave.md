# Test Plan: Import Wave From .wav File

Manual test plan for the **Import Wave…** feature defined in `features/import-wave.md`. All tests run against a real device folder via `./gradlew desktopRun`.

---

## Pre-requisites

1. A working SPD-Pro export folder with at least `SYSTEM/`, `KIT/`, and `WAVE/PRM/`, `WAVE/DATA/` subfolders, loaded into the app via **Select Folder**.
2. A set of prepared sample `.wav` files for the various test cases (see [Sample WAV matrix](#sample-wav-matrix) below). Use `ffmpeg` to generate deterministic fixtures:
   ```bash
   # 16-bit / 44.1 kHz mono — valid
   ffmpeg -f lavfi -i "sine=frequency=440:duration=1" -ac 1 -ar 44100 -acodec pcm_s16le valid_mono.wav

   # 16-bit / 44.1 kHz stereo — valid
   ffmpeg -f lavfi -i "sine=frequency=440:duration=1" -ac 2 -ar 44100 -acodec pcm_s16le valid_stereo.wav

   # 8-bit / 44.1 kHz — invalid bit depth
   ffmpeg -f lavfi -i "sine=frequency=440:duration=1" -ac 1 -ar 44100 -acodec pcm_u8 invalid_8bit.wav

   # 16-bit / 48 kHz — invalid sample rate
   ffmpeg -f lavfi -i "sine=frequency=440:duration=1" -ac 1 -ar 48000 -acodec pcm_s16le invalid_48khz.wav

   # 24-bit / 44.1 kHz — invalid bit depth
   ffmpeg -f lavfi -i "sine=frequency=440:duration=1" -ac 1 -ar 44100 -acodec pcm_s24le invalid_24bit.wav

   # 16-bit / 44.1 kHz 4-channel — invalid channel count
   ffmpeg -f lavfi -i "sine=frequency=440:duration=1" -ac 4 -ar 44100 -acodec pcm_s16le invalid_4ch.wav

   # IEEE float / 44.1 kHz — non-PCM
   ffmpeg -f lavfi -i "sine=frequency=440:duration=1" -ac 1 -ar 44100 -acodec pcm_f32le invalid_float.wav

   # Filename with long / unicode / punctuation characters
   ffmpeg -f lavfi -i "sine=frequency=440:duration=1" -ac 1 -ar 44100 -acodec pcm_s16le "Crash Cymbal #1 (élégant).wav"
   ```
3. A **backup copy** of your device folder before running destructive tests (some cases create folders and write files; there is no in-app undo).
4. Familiarity with the on-disk layout so you can verify files after each test:
   - `WAVE/PRM/<NN>/<NN>.spd` — per-wave XML metadata
   - `WAVE/DATA/<NN>/<filename>.wav` — audio payload
   - `SYSTEM/tag_list.spd` — category list
   - `SYSTEM/wavelist_name.spd`, `wavelist_tagname.spd`, `wavelist_tagnum.spd` — index files

### Sample WAV matrix

| Name                        | Rate    | Bits | Channels | Encoding | Expected |
|-----------------------------|---------|------|----------|----------|----------|
| `valid_mono.wav`            | 44100   | 16   | 1        | PCM      | Accept   |
| `valid_stereo.wav`          | 44100   | 16   | 2        | PCM      | Accept   |
| `invalid_8bit.wav`          | 44100   | 8    | 1        | PCM      | Reject   |
| `invalid_24bit.wav`         | 44100   | 24   | 1        | PCM      | Reject   |
| `invalid_48khz.wav`         | 48000   | 16   | 1        | PCM      | Reject   |
| `invalid_4ch.wav`           | 44100   | 16   | 4        | PCM      | Reject   |
| `invalid_float.wav`         | 44100   | 32   | 1        | Float    | Reject   |

---

## 1. Happy-path tests

### 1.1 Import valid mono wave into Default category
**Setup:** Note the highest existing wave number `N` in `wavelist_name.spd`. Use `BY_CATEGORY_NAME` view.

**Steps:**
1. Right-click any wave in the list → **Import Wave…**.
2. In the dialog, confirm `Default` is pre-selected → click **Continue**.
3. In the system file picker, navigate to `valid_mono.wav` → **Open**.

**Expected:**
- No error dialog appears.
- The new wave `valid_mono` appears in the list immediately (no tab switch or reload needed).
- In `BY_CATEGORY_NAME`, it appears inside the `Default` group at its alphabetical position (`v` — likely near the end alphabetically).
- In `BY_CATEGORY_NUM`, it appears at the end of the `Default` group.
- In `BY_NAME`, it appears at its alphabetical position in the flat list.
- On disk:
  - A new file exists at `WAVE/PRM/<folder>/<file>.spd` where `folder = (N / 100)` zero-padded and `file = (N % 100)` zero-padded (the "next slot" after `N`).
  - A copy of `valid_mono.wav` exists at `WAVE/DATA/<folder>/valid_mono.wav`.
  - The new `.spd` contains `<Path>folder/valid_mono.wav</Path>` and `<Tag>0</Tag>`; `Nm0..Nm11` encode `valid_mono`.
  - `SYSTEM/tag_list.spd` is updated (timestamp refreshes) even though its content may be unchanged.
  - `SYSTEM/wavelist_name.spd`, `wavelist_tagname.spd`, `wavelist_tagnum.spd` all include the new wave number.

### 1.2 Import valid stereo wave into non-default category
**Setup:** Ensure a non-Default category exists (rename one if needed). Use `BY_CATEGORY_NUM` view.

**Steps:**
1. Right-click the header of a non-Default category → **Import Wave…**.
2. Select that category in the dropdown → **Continue**.
3. Pick `valid_stereo.wav`.

**Expected:**
- The wave appears inside the selected category (not Default).
- The written `.spd`'s `<Tag>` value equals the selected `Category.order` field.
- In `BY_CATEGORY_NUM`, the wave is appended at the **end** of that category's list.
- In `BY_CATEGORY_NAME`, the wave appears at its alphabetical slot within that category.
- `WAVE/DATA/<folder>/valid_stereo.wav` exists and byte-matches the source.

### 1.3 Import from BY_NAME view
**Steps:**
1. Switch to `BY_NAME` sorting mode.
2. Right-click any wave → **Import Wave…** → pick `Default` → select `valid_mono.wav` (rename it on disk first if already imported).

**Expected:** Same as 1.1 — demonstrates the context menu item is reachable from all three sorting modes.

### 1.4 Context menu on category header
**Steps:**
1. In `BY_CATEGORY_NAME`, right-click a category header (not a wave row).
2. Verify the menu shows both **Rename Category…** and **Import Wave…**.
3. Click **Import Wave…** → the pre-selected category in the dialog is the one right-clicked? *(Note: per current implementation, the dialog always pre-selects `Default`; if you prefer context-aware default, file a follow-up.)*

**Expected:** Menu contains both items; clicking **Import Wave…** opens the dialog.

---

## 2. Validation / error tests

Each of these cases must show a clear error dialog and **must not** write anything to disk. After each case, verify that no new files appear under `WAVE/PRM/` or `WAVE/DATA/` and no entries are added to `wavelist_name.spd`.

### 2.1 Wrong bit depth — 8-bit
Import `invalid_8bit.wav`.
**Expected:** Error dialog text contains "16-bit" and mentions the actual depth (`8-bit`). No files written.

### 2.2 Wrong bit depth — 24-bit
Import `invalid_24bit.wav`.
**Expected:** Error dialog, "24-bit" reported. No files written.

### 2.3 Wrong sample rate — 48 kHz
Import `invalid_48khz.wav`.
**Expected:** Error dialog contains "44.1 kHz" and the offending rate (`48000 Hz`). No files written.

### 2.4 Too many channels
Import `invalid_4ch.wav`.
**Expected:** Error dialog states "mono or stereo" and reports `4 channels`. No files written.

### 2.5 Non-PCM encoding
Import `invalid_float.wav`.
**Expected:** Error dialog says "Only PCM-encoded WAV files are supported". No files written.

### 2.6 Not a real .wav file
Rename any `.mp3` or `.txt` to `fake.wav` and try to import it.
**Expected:** Error dialog (from `AudioSystem.UnsupportedAudioFileException`). No files written.

### 2.7 File renamed to lose extension
Pick a file without a `.wav` extension. Note: the file chooser filter should hide it; if reachable via "Show All", the validator rejects it with "Only .wav files are supported".

---

## 3. Name collision / sanitization tests

### 3.1 Exact-name collision (existing wave)
**Setup:** Identify an existing wave name, e.g. `Kick01`.
**Steps:** Create or rename a `.wav` file on disk to `Kick01.wav` and try to import it.
**Expected:** Error dialog text: "A wave named 'Kick01' already exists. Please rename the source file and try again." No files written.

### 3.2 Collision via truncation
**Setup:** Identify an existing 12-char wave name, e.g. `LongSnareNam`.
**Steps:** Create `LongSnareName1.wav` (14 chars stem). The sanitizer truncates to 12 → `LongSnareNam`, which collides.
**Expected:** Same error as 3.1 for the truncated name. No files written.

### 3.3 Sanitization of special characters
**Steps:** Import the file `Crash Cymbal #1 (élégant).wav`.
**Expected:**
- Import succeeds.
- Derived wave name is sanitized: every non-`[A-Za-z0-9 _-]` char becomes `_`, then truncated to 12 chars. For the above input the sanitized stem (before truncation) is `Crash Cymbal __1 _l_gant_` — truncated to 12 → `Crash Cymbal`.
- On-disk filename under `WAVE/DATA/<folder>/` uses the **untruncated** sanitized form + `.wav` (so longer than the displayed wave name is OK).
- The `<Path>` field in the new PRM `.spd` matches the on-disk filename exactly.

### 3.4 Empty sanitized name
**Steps:** Create a file named `!!!.wav` (stem sanitizes to `___`, which is non-blank but useless). Import it.
**Expected:** Accepts `___` as the wave name (it's not blank, just placeholder underscores). Document this as acceptable UX; a follow-up story can improve the prompt to allow user-provided names.

### 3.5 Leading/trailing whitespace
**Steps:** Create a file named `  Snare Hit  .wav` with leading/trailing spaces in the stem.
**Expected:** Sanitized + trimmed to `Snare Hit` (our `sanitizeWaveName` trims the result). Import succeeds if no collision.

### 3.6 Source filename equals existing on-disk filename
**Setup:** Pre-populate `WAVE/DATA/<folder>/Snare.wav` manually (or via a prior import). Try to import a different `.wav` also named `Snare.wav` — the wave name collision check might not catch it if the in-memory `Snare` wave does not exist, but the file on disk does.
**Expected:** Error dialog "A file named 'Snare.wav' already exists in WAVE/DATA/<folder>." No `.spd` metadata is written; no partial state.

---

## 4. Slot allocation tests

These tests require being close to folder boundaries; consider using a scratch device folder with many pre-created waves.

### 4.1 Normal slot increment
**Setup:** Device has waves 1..25.
**Steps:** Import a valid wave.
**Expected:** New wave gets number 26 → `WAVE/PRM/00/25.spd` and `WAVE/DATA/00/<name>.wav`.

### 4.2 Folder boundary crossover (00 → 01)
**Setup:** Populate waves up to number 100 (the slot `00/99`).
**Steps:** Import a valid wave.
**Expected:**
- New wave gets number 101.
- `WAVE/PRM/01/` directory is created (was absent before).
- `WAVE/DATA/01/` directory is created.
- The new `.spd` lands at `WAVE/PRM/01/00.spd` with `<Path>01/<name>.wav</Path>`.
- Import succeeds without error.

### 4.3 Folder boundary crossover (higher)
**Setup:** Populate waves up to number 500 (slot `04/99`).
**Steps:** Import.
**Expected:** New folder `05` is created under both PRM and DATA; new wave lands at `05/00`.

### 4.4 Hard cap at 09/99
**Setup:** Populate waves up to number 1000 (slot `09/99`).
**Steps:** Try to import one more.
**Expected:** Error dialog "Wave library is full (max 1000 waves)." No files written, no memory mutation.

### 4.5 Non-contiguous wave numbers
**Setup:** If the device has "holes" in wave numbering (e.g. 1, 2, 5, 7 — gaps at 3, 4, 6) — this can happen with hand-edited folders.
**Steps:** Import a wave.
**Expected:** New wave gets number `maxExisting + 1 = 8`, not `3` (the first hole). Document this: the current implementation intentionally avoids hole-filling for simplicity. Holes in numbering remain; the corresponding `.spd` slots on disk stay empty.

---

## 5. UI state / re-render tests

### 5.1 Immediate visual update
**Steps:** Import a valid wave and verify without clicking anything else that the new entry is visible in the current view. This specifically guards the "no tab switch required" criterion.
**Expected:** Wave appears within ~100 ms of the file chooser closing.

### 5.2 Sort correctness in BY_CATEGORY_NAME
**Steps:** Into the `Default` category, import three valid waves in this order: `Zeta`, `Alpha`, `Mango`.
**Expected:** After each import, the waves appear inside the `Default` group in alphabetical order: `Alpha`, `Mango`, `Zeta`, interleaved with any pre-existing waves in correct positions.

### 5.3 Sort correctness in BY_CATEGORY_NUM
**Steps:** Same three imports.
**Expected:** In `BY_CATEGORY_NUM`, the three new waves appear at the **end** of the `Default` group, in the order they were imported (`Zeta`, `Alpha`, `Mango`) — preserving numerical order (each new import has the next wave number).

### 5.4 Sort correctness in BY_NAME (flat)
**Steps:** Same three imports; switch to `BY_NAME`.
**Expected:** The three new waves each appear at their correct alphabetical slot in the global flat list.

### 5.5 Expand/collapse state preserved
**Setup:** Collapse all categories except `Default`.
**Steps:** Import into `Default`.
**Expected:** Other categories remain collapsed; `Default` remains expanded; new wave visible inside it.

### 5.6 Error dialog dismiss
**Steps:** Trigger a validation error (any case from §2). When the dialog appears, click **OK**.
**Expected:** Dialog closes; `importError` state is cleared; a subsequent valid import works normally (verifies `clearImportError()` path).

### 5.7 Dialog cancel paths
**Steps:**
  a. Open **Import Wave…** → click **Cancel** on the category dialog.
  b. Open **Import Wave…** → **Continue** → close the OS file picker (X / Cancel).
**Expected:** Both paths leave the app in a clean state; no error dialog; no files written.

---

## 6. Persistence tests

### 6.1 On-disk index consistency
**Steps:** Import one valid wave. Open `SYSTEM/wavelist_name.spd`, `wavelist_tagname.spd`, `wavelist_tagnum.spd`, and `tag_list.spd` in a text editor.
**Expected:**
- The new wave's number is present in all three `wavelist_*.spd` files.
- In `wavelist_tagname.spd` it appears inside the chosen category's `<WvList>` block at the alphabetical slot.
- In `wavelist_tagnum.spd` it appears at the end of the chosen category's `<WvList>` block.
- `tag_list.spd` content is structurally unchanged (no category rename), but file was rewritten.

### 6.2 Survives app restart
**Steps:** Import a valid wave. Quit the app. Relaunch and reopen the same folder.
**Expected:** The imported wave is present in the list with the same name and category assignment — confirms that in-memory updates were successfully persisted to disk rather than lost.

### 6.3 Batch save still works for metadata
**Steps:** Import a wave. Without closing the app, change a kit pad to point at the new wave. Click the app's global **Save**. Quit and reopen.
**Expected:** Kit edit is persisted, wave is still present — confirms the "persist on import, batch everything else on Save" pattern.

### 6.4 Import followed by category rename
**Steps:** Import into `Default`. Then rename `Default` to `Rock`.
**Expected:** Imported wave moves with the category. Because category rename is batched (not persisted immediately), the on-disk `tag_list.spd` still reads `Default` until a global Save, but in-memory UI shows `Rock`.

### 6.5 Import followed by app crash (destructive — optional)
**Setup:** Import a wave. **Before** anything else, `kill -9` the Java process.
**Expected:** On restart, the imported wave is still present — its `.spd`, `.wav`, and all wave list index files were persisted synchronously during import.

---

## 7. Edge cases and stretch tests

### 7.1 No device loaded
**Steps:** Launch the app, do not load a folder, verify that the wave list shows the placeholder (no right-click target). The import path is unreachable in this state by design — no test needed.

### 7.2 Blank root path (manual inject)
Not reachable via UI; the `rootPath.isBlank()` guard is a defensive check inside `DeviceManager.importWave`. Skip unless the loader is being refactored.

### 7.3 Very large .wav file (>100 MB)
**Steps:** Import a large valid 16-bit / 44.1 kHz file.
**Expected:** Import succeeds; copy may take a few seconds (no progress indicator in current impl — log as UX improvement if blocking). UI thread should not freeze for long since `copyTo` is invoked from Swing's EDT via the context-menu callback — watch for jank and file a follow-up if noticeable.

### 7.4 Source file on different volume / network drive
**Steps:** Pick a `.wav` from an external drive or network share.
**Expected:** `sourceFile.copyTo(...)` handles the cross-volume copy transparently.

### 7.5 Read-only device folder
**Steps:** Remove write permission from `WAVE/PRM/` and attempt an import.
**Expected:** Error dialog "Failed to write wave files: ..." with the IO exception message. No partial state.

### 7.6 Disk full
Hard to reproduce; simulate with a tmpfs of known size if needed. Expect "Failed to write wave files" with "No space left on device" message.

### 7.7 Category list empty
**Setup:** Pathological case where `tag_list.spd` has zero categories.
**Expected:** The category dropdown would be empty and **Continue** is disabled (selection blank). Verify the dialog does not crash. Consider a follow-up to pre-seed `Default` if the list is empty.

### 7.8 Rapid double-click on Continue
**Steps:** Spam-click the **Continue** button on the category dialog.
**Expected:** Only one file picker opens; subsequent clicks are no-ops (state already transitioned to `Idle` after dialog closed).

---

## Suggestions for additional tests (not in-scope for this release)

- **Unit test `WavValidator`** with a small set of header-only fixtures (byte arrays, not files) covering every branch.
- **Unit test `WaveListsHolder.withAddedWave`** to verify alphabetical insertion, fallback-to-first-category behavior, and that map iteration order is deterministic.
- **Parameterized test for slot allocation** — given a list of existing wave numbers, assert the computed `folderStr/fileStr` for the next slot.
- **Snapshot test for generated `.spd` XML** — compare against a golden file to detect unintended changes in `WvPrm` serialization (e.g. field order, whitespace).
- **UI test with Compose testing framework** — drive the right-click → dialog → file picker flow with a fake `onImportWave` callback, asserting the error dialog shows up when the callback returns an error.
- **Fuzz filename sanitization** with a corpus of messy strings (emoji, CJK, RTL, control chars) to ensure no crashes.

---

## Regression sanity checks

After all import tests, run the existing smoke tests to ensure nothing unrelated broke:

- Category rename in `BY_CATEGORY_NAME` still re-sorts alphabetically (feature: `sort-categories-by-name`).
- Kit drag-and-drop in the kit list still works.
- Right-click "Used in:" wave references still navigate to the correct kit.
- Global **Save** still writes all files without error.
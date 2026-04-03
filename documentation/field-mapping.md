# Domain ↔ Raw Field Mapping Reference

Centralized reference for all field transformations between domain models and raw XML-mapped classes.

**Legend:**
- **Direct** — value copied as-is, no transformation
- **`.value`** — extract the `value` property from an enum/sealed class
- Arrows show direction: Read (raw→domain) and Write (domain→raw)

---

## Kit (Domain) ↔ KitPrm (Raw)

| Domain field | Raw field(s) | Read (raw→domain) | Write (domain→raw) | Possible values |
|---|---|---|---|---|
| `name: String` | `Nm0`–`Nm7` | `decodeName(intArray)` | `encodeName(str)` padded to 8 with trailing zeros | ASCII string, up to 8 chars |
| `subName: String` | `SubNm0`–`SubNm15` | `decodeName(intArray)` | `encodeName(str)` padded to 16 with trailing zeros | ASCII string, up to 16 chars |
| `tempo: Double` | `Tempo: Int` | `raw / 10.0` | `(domain * 10).toInt()` | 20.0–260.0 (raw: 200–2600) |
| `volume: Int` | `Level: Int` | Direct | Direct | 0–127 |
| `padLink.first: PadNumber` | `LinkPad0: Int` | `PadNumber.fromValue(raw)` | `.value` | See PadNumber |
| `padLink.second: PadNumber` | `LinkPad1: Int` | `PadNumber.fromValue(raw)` | `.value` | See PadNumber |
| `fx1: KitFX` | `Fx1Sw`, `Fx1Type`, `Fx1Prm0`–`Fx1Prm19` | `KitFX.fromValues(sw, type, prmList)` | Unpack to switch + type + 20 individual fields | switch: 0/1, type+params: FX-dependent |
| `fx2: KitFX` | `Fx2Sw`, `Fx2Type`, `Fx2Prm0`–`Fx2Prm19` | `KitFX.fromValues(sw, type, prmList)` | Unpack to switch + type + 20 individual fields | switch: 0/1, type+params: FX-dependent |
| `pads: Map<PadNumber, Pad>` | `PadPrm: List<PadPrm>` | Index → `PadNumber.fromValue(index)` as key | Iterate PadNumber entries (0–14) to ordered list | Up to 15 pads |

---

## Pad (Domain) ↔ PadPrm (Raw)

### Sound Fields

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `main.wave: Int` | `Wv: Int` | **`raw + 1`** | **`domain - 1`** | Domain: 1-based; Raw: 0-based |
| `main.volume: Int` | `WvLevel: Int` | Direct | Direct | 0–127 |
| `main.pan: PadPan` | `WvPan: Int` | **`PadPan(raw - 15)`** | **`domain.value + 15`** | Domain: -15..15; Raw: 0..30 |
| `sub.wave: Int` | `SubWv: Int` | **`raw + 1`** | **`domain - 1`** | Domain: 1-based; Raw: 0-based |
| `sub.volume: Int` | `SubWvLevel: Int` | Direct | Direct | 0–127 |
| `sub.pan: PadPan` | `SubWvPan: Int` | **`PadPan(raw - 15)`** | **`domain.value + 15`** | Domain: -15..15; Raw: 0..30 |

### Mode Fields

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `padMode.template` | `PlayMode: Int` | `PadTemplate.fromValue(raw)` | `.value` | NONE=-1, SINGLE=0, PHRASE=1, LOOP=2 |
| `padMode.loop` | `Loop: Int` | `PadLoop.fromValue(raw)` | `.value` | OFF=0, ON=1, X2=2, X4=3, X8=4 |
| `padMode.trigType` | `TrigType: Int` | `TrigType.fromValue(raw)` | `.value` | SHOT=0, ALT=1 |
| `padMode.dynamics` | `Dynamics: Int` | `SyncSwitch.fromValue(raw)` | `.value` | OFF=0, ON=1 |
| `padMode.voiceAssign` | `VoiceAsgn: Int` | `PolyMono.fromValue(raw)` | `.value` | MONO=0, POLY=1 |

### Output / Group Fields

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `output` | `OutAsgn: Int` | `PadOutput.fromValue(raw)` | `.value` | MASTER_OUT=0, KIT_FX1=1, KIT_FX2=2, SUB_OUT=3, PHONES_ONLY=4 |
| `muteGroup` | `MuteGrp: Int` | `MuteGroup.fromValue(raw)` | `Off→-1`, `Group(n)→n` | Off=-1, Group 0–9 |
| `tempoSync` | `TempoSync: Int` | `SyncSwitch.fromValue(raw)` | `.value` | OFF=0, ON=1 |

### MIDI Fields

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `midiParams.padCh` | `PadMidiCh: Int` | `PadCH.fromValue(raw)` | `Global→-1`, `Channel(n)→n` | Global=-1, Channel 1–16 |
| `midiParams.noteNum` | `NoteNum: Int` | `MidiNote.fromValue(raw)` | `Off→-1`, `Channel(n)→n` | Off=-1, Note 0–127 |
| `midiParams.midiCtrl` | `MidiCtrl: Int` | `ExternalControl.fromValue(raw)` | `.value` | OFF=0, ON=1 |
| `midiParams.gateTime` | `GateTime: Int` | `Gate.fromValue(raw)` | `Off→-1`, `Alt→0`, `GateVal(n)→n` | Off=-1, Alt=0, GateVal 1–16 |

---

## SetupConfig (Domain) ↔ SetupPrm (Raw)

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `lcdContrast: Int` | `lcdContrast` | Direct | Direct | 1–10 |
| `lcdBrightness: Int` | `lcdBright` | Direct | Direct | 0–10 |
| `padIndication` | `padIllumi` | `PadIndication.fromValue(raw)` | `.value` | OFF=0, DYNAMIC=1, STATE=2, ALL_ON=3 |
| `tempoIndication` | `tempoIndi` | `SyncSwitch.fromValue(raw)` | `.value` | OFF=0, ON=1 |
| `fs1Polarity` | `fs1Porality` | `FootSwitchPolarity.fromValue(raw)` | `.value` | NORMAL=0, INVERSE=1 |
| `fs2Polarity` | `fs2Porality` | `FootSwitchPolarity.fromValue(raw)` | `.value` | NORMAL=0, INVERSE=1 |
| `midiCh: Int` | `midiCh` | Direct | Direct | 0–16 |
| `midiSync` | `midiSync` | `MidiSync.fromValue(raw)` | `.value` | OFF=0, AUTO=1 |
| `localCtrl` | `localCtrl` | `SyncSwitch.fromValue(raw)` | `.value` | OFF=0, ON=1 |
| `softThru` | `softThru` | `SyncSwitch.fromValue(raw)` | `.value` | OFF=0, ON=1 |
| `midiPCCtrl` | `midiPCCtrl` | `SyncSwitch.fromValue(raw)` | `.value` | OFF=0, ON=1 |
| `midiCCCtrl` | `midiCCCtrl` | `SyncSwitch.fromValue(raw)` | `.value` | OFF=0, ON=1 |
| `midiFxSelCc: Int` | `mefctCCSel` | Direct | Direct | 0–95 |
| `mstrFxCtrl1Cc: Int` | `mefctCCKnob1` | Direct | Direct | 0–95 |
| `mstrFxCtrl2Cc: Int` | `mefctCCKnob2` | Direct | Direct | 0–95 |
| `usbMIDIThru` | `usbMIDIThru` | `SyncSwitch.fromValue(raw)` | `.value` | OFF=0, ON=1 |
| `padLock` | `padLock` | `SyncSwitch.fromValue(raw)` | `.value` | OFF=0, ON=1 |
| `autoPowerOff` | `autoPowerOff` | `AutoOff.fromValue(raw)` | `.value` | OFF=0, FOUR_HRS=1 |
| `dispMode` | `dispMode` | `DispMode.fromValue(raw)` | `.value` | SUBNAME=0, LEVEL=1 |
| `multiView: Int` | `multiView` | Direct | Direct | Int |
| `usbDevMode` | `usbDevMode` | `UsbMidiMode.fromValue(raw)` | `.value` | WAVEMGR=0, AUDIO_MIDI=1 |
| `startupKit: Int` | `startupKit` | Direct | Direct | Int |
| `intPads: List<IntPad>` | `intPads: Array<IntPad>` | Per element (see below) | Per element (see below) | 9 internal pads |
| `extPads: List<ExtPad>` | `extPads: Array<ExtPad>` | Per element (see below) | Per element (see below) | 4 external pads |

### IntPad (Domain) ↔ IntPad (Raw)

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `sens: Int` | `sens` | Direct | Direct | Int |
| `threshold: Int` | `threshold` | Direct | Direct | Int |
| `curve: VeloCurve` | `curve` | `VeloCurve.fromValue(raw)` | `.value` | LINEAR=0, EXP1=1, EXP2=2, LOG1=3, LOG2=4, SPLINE=5, LOUD1=6, LOUD2=7 |

### ExtPad (Domain) ↔ ExtPad (Raw)

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `inputMode` | `inputMode` | `InputMode.fromValue(raw)` | `.value` | HEAD_RIM=0, TRIG_X2=1 |
| `padType` | `padType` | `TrigType.fromValue(raw)` | `.value` | 33 trigger types (KD_7=0 … RT30H_TM=32) |
| `sens: Int` | `sens` | Direct | Direct | Int |
| `threshold: Int` | `threshold` | Direct | Direct | Int |
| `curve: VeloCurve` | `curve` | `VeloCurve.fromValue(raw)` | `.value` | LINEAR=0 … LOUD2=7 |
| `scanTime: Int` | `scanTime` | Direct | Direct | Int |
| `retrigCxl: Int` | `retrigCxl` | Direct | Direct | Int |
| `maskTime: Int` | `maskTime` | Direct | Direct | Int |
| `xtalkCxl: Int` | `xtalkCxl` | Direct | Direct | Int |
| `rimAdjust: Int` | `rimAdjust` | Direct | Direct | Int |
| `rimGain: Int` | `rimGain` | Direct | Direct | Int |
| `noiseCxl: Int` | `noiseCxl` | Direct | Direct | Int |

---

## SystemConfig (Domain) ↔ SysPrm + KitChainPrm + MEfctPrm (Raw)

`SystemConfig` is assembled from three raw objects during read and must be decomposed back into three when writing.

### ClickConfig → SysPrm fields

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `clickConfig.soundGroup` | `clickSoundGroup` | `SoundGroup.fromValue(raw)` | `.value` | PRESET=0, USER=1 |
| `clickConfig.clickSound` | `clickSound` | `ClickSound.fromValue(raw)` | `.value` | ELECTRONIC=0, BEEP=1, PULSE=2, SWEEP=3, OLD_STYLE=4, DRUM_STICKS=5, SNARE=6, CLAVES=7, COWBELL=8, SHAKER=9 |
| `clickConfig.clickWave: Int` | `clickWave` | Direct | Direct | Int (wave number) |
| `clickConfig.interval` | `clickInterval` | `Interval.fromValue(raw)` | `.value` | ONE_FOURTH=0, ONE_EIGHTH=1, ONE_TWELFTH=2 |
| `clickConfig.pan` | `clickPan` | **`ClickPan(raw - 15)`** | **`domain.value + 15`** | Domain: -15..15; Raw: 0..30 |
| `clickConfig.assign` | `clickAssign` | `Output.fromValue(raw)` | `.value` | MASTER=0, SUB=1, PHONES=2 |
| `clickConfig.level: Int` | `clickLevel` | Direct | Direct | 0–127 |

### SystemAudioConfig → SysPrm fields

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `systemAudioConfig.audioInVolume: Int` | `audioInputLevel` | Direct | Direct | Int |
| `systemAudioConfig.usbInVolume: Int` | `usbDAudioInputLevel` | Direct | Direct | Int |
| `systemAudioConfig.subOutVolume: Int` | `subOutLevel` | Direct | Direct | Int |
| `systemAudioConfig.systemGain` | `systemGain` | `SystemGain.fromValue(raw)` | `.value` | ZERO=0 (0dB), SIX_DB=1 (6dB), TWELVE_DB=2 (12dB) |
| `systemAudioConfig.audioInputAssign` | `audioInputAssign` | `Output.fromValue(raw)` | `.value` | MASTER=0, SUB=1, PHONES=2 |
| `systemAudioConfig.fx2Assign` | `fx2Assign` | `FxOutput.fromValue(raw)` | `.value` | MASTER=0, SUB=1 |

### SystemEQ → MEfctPrm fields

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `systemAudioConfig.eq.lowGain: Float` | `meqLoGain: Int` | **`raw - 12` → Float** | **`(domain + 12).toInt()`** | Domain: -12.0..12.0; Raw: 0..24 |
| `systemAudioConfig.eq.midFreq` | `meqMidFreq: Int` | `EqFreq.fromIndex(raw)` | `.index` | 28 values: HZ_20=0 … HZ_10000=27 |
| `systemAudioConfig.eq.midGain: Float` | `meqMidGain: Int` | **`raw - 12` → Float** | **`(domain + 12).toInt()`** | Domain: -12.0..12.0; Raw: 0..24 |
| `systemAudioConfig.eq.hiGain: Float` | `meqHiGain: Int` | **`raw - 12` → Float** | **`(domain + 12).toInt()`** | Domain: -12.0..12.0; Raw: 0..24 |

### PadFsControls → SysPrm fields

| Domain field | Raw fields | Read | Write | Possible values |
|---|---|---|---|---|
| `padFsControls: Map<PadNumber, PadFsControl>` | `padCtrlPad1`–`padCtrlPad9`, `padCtrlExt1`–`padCtrlExt4`, `padCtrlFS1`, `padCtrlFS2` | 15-element IntArray → Map by PadNumber | Extract `.value` per PadNumber index back to 15 fields | OFF=0, KIT_INC=1, KIT_DEC=2, CLICK=3, TAP_TEMPO=4, ALL_SOUND_OFF=5, FX1_ON_OFF=6, FX2_ON_OFF=7, PAD_CHECK=8 |

### KitChains → KitChainPrm

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `kitChains: Map<Char, KitChain>` | `KitChainPrm.kitChains: Array<KitChain>` | Array index → char key: 0→'A', 1→'B', …, 7→'H' | Char key → array index: 'A'→0, …, 'H'→7 | 8 chains max |
| `kitChain.name: String` | `nm0`–`nm9` | `decodeName(intArray)` | `encodeName(str)` padded to 10 with trailing zeros | ASCII, up to 10 chars |
| `kitChain.kits: List<Int>` | `stp0`–`stp19` | Collect 20 step fields into list | Unpack list to 20 individual fields | Up to 20 kit refs |

### VisualControl → SysPrm fields

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `visualControl.controlOnly` | `vLinkControlOnly` | `SyncSwitch.fromValue(raw)` | `.value` | OFF=0, ON=1 |
| `visualControl.mode` | `vLinkMode` | `VControlMode.fromValue(raw)` | `.value` | MVC=0, V_LINK=1 |
| `visualControl.bank` | `vLinkBank` | `Bank.fromValue(raw)` | `Off→-1`, `BankNumber(n)→n` | Off=-1, BankNumber 0–127 |
| `visualControl.ch: Int` | `vLinkChannel` | Direct | Direct | Int |
| `visualControl.knob1CC` | `vLinkKnob1CC` | `KnobCC.fromValue(raw)` | `Off→0`, `KnobCcNumber(n)→n` | Off=0, KnobCcNumber 1–127 |
| `visualControl.knob2CC` | `vLinkKnob2CC` | `KnobCC.fromValue(raw)` | `Off→0`, `KnobCcNumber(n)→n` | Off=0, KnobCcNumber 1–127 |

### MasterEffectConfig → MEfctPrm fields

| Domain field | Raw fields | Read | Write | Possible values |
|---|---|---|---|---|
| `masterEffectConfig.filter` | `fltrPreset`, `fltrType`, `fltrPrm0`–`fltrPrm19` | `FilterEffect.fromValues(preset, type, params)` | Unpack to preset + type + 20 params | FilterPreset: SIMPLE_LFP=0, SIMPLE_BPF=1, SIMPLE_HPF=2, LPF_SYNC_MOD=3, BPF_SYNC_MOD=4, HPF_SYNC_MOD=5 |
| `masterEffectConfig.delay` | `dlyPreset`, `drType`, `drPrm0`–`drPrm19` | `DelayEffect.fromValues(preset, type, params)` | Unpack to preset + type + 20 params | DelayPreset: STEREO_NORMAL=0, STEREO_SYNC=1, PAN_QUARTER=2, PAN_DOTTED_EIGHT=3, PAN_DOUBLE=4 |
| `masterEffectConfig.sloop` | `sloopPreset`, `spType`, `spPrm0`–`spPrm19` | `SLoopEffect.fromValues(preset, type, params)` | Unpack to preset + type + 20 params | SLoopPreset: MANUAL=0, AUTO_QUARTER=1, AUTO_EIGHT=2, AUTO_SIXTEEN=3, AUTO_FREERUN=4 |
| `masterEffectConfig.fx` | `fxType`, `fxPrm0`–`fxPrm19` | `FxEffect.fromValues(type, params)` | Unpack to type + 20 params | FXType: THRU=0, STEREO_DLY=1, … SLICER=20 (see FXType enum) |

---

## Wave (Domain) ↔ WvPrm (Raw) + Coordinate

| Domain field | Raw field | Read | Write | Possible values |
|---|---|---|---|---|
| `number: Int` | Derived from `Coordinate` | **`folder * 100 + file + 1`** | **`folder = (num-1)/100`, `file = (num-1)%100`** | 1-based wave index |
| `name: String` | `Nm0`–`Nm9` (via `waveName()`) | `decodeName(intArray)` | `encodeName(str)` padded to 10 with trailing zeros | ASCII, up to 10 chars |
| `path: String` | `path` | Direct | Direct | File path string |
| `tagRef: Int` | `tag` | Direct | Direct | 0-indexed category ref |
| `tempo: Int` | `tempo` | Direct | Direct | Int |
| `beat: Int` | `beat` | Direct | Direct | Int |
| `measure: Int` | `measure` | Direct | Direct | Int |
| `start: Int` | `start` | Direct | Direct | Int |
| `end: Int` | `end` | Direct | Direct | Int |

---

## WaveListsHolder (Domain) ↔ TagList + WvListSortby* (Raw)

| Domain field | Raw class | Read | Write |
|---|---|---|---|
| `categories: Map<Category, Int>` | `TagList` (list of `TagPrm`) | `decodeName(tagName())` per tag → Category + order | `encodeName(name)` padded to 12 + order → `TagPrm` |
| `wavesByName: List<Wave>` | `WvListSortbyName` (`List<Int>`) | Map wave numbers to Wave objects | Extract wave numbers to `List<Int>` |
| `wavesByNamePerCategory` | `WvListSortbyNameTag` (`List<WvList>`) | Per-category wave lists | Pack back to `List<WvList>` |
| `wavesByNumPerCategory` | `WvListSortbyNumTag` (`List<WvList>`) | Per-category wave lists | Pack back to `List<WvList>` |

---

## Name Encoding Summary

Names are stored as individual `Int` fields (ASCII codes). Zeros act as null terminators.

| Context | Raw fields | Char count | Example |
|---|---|---|---|
| Kit name | `Nm0`–`Nm7` | 8 | `KitPrm` |
| Kit sub-name | `SubNm0`–`SubNm15` | 16 | `KitPrm` |
| Kit chain name | `nm0`–`nm9` | 10 | `KitChain` |
| Wave name | `Nm0`–`Nm9` (read via `waveName()`) | 10 | `WvPrm` |
| Tag/category name | `Nm0`–`Nm11` | 12 | `TagPrm` |

**Read:** `decodeName(intArray)` — skip zeros, map codes to chars, join to String.
**Write:** `encodeName(string)` — map chars to codes. Must pad with trailing zeros to the expected field count.

---

## Enum Quick Reference

### Shared / Reused Enums

| Enum | Values | Used by |
|---|---|---|
| `SyncSwitch` | OFF=0, ON=1 | tempoSync, dynamics, localCtrl, softThru, midiPCCtrl, midiCCCtrl, usbMIDIThru, padLock, tempoIndication, vLinkControlOnly |
| `PadNumber` | PAD_1=0 … PAD_9=8, TRIG_1=9 … TRIG_4=12, FS_1=13, FS_2=14 | padLink, pad map keys, padFsControls |
| `Output` | MASTER=0, SUB=1, PHONES=2 | clickAssign, audioInputAssign |

### Sealed Classes with Sentinel Values

| Class | Subtype | Raw value |
|---|---|---|
| `MuteGroup` | `Off` | -1 |
| `MuteGroup` | `Group(n)` | 0–9 |
| `PadCH` | `Global` | -1 |
| `PadCH` | `Channel(n)` | 1–16 |
| `MidiNote` | `Off` | -1 |
| `MidiNote` | `Channel(n)` | 0–127 |
| `Gate` | `Off` | -1 |
| `Gate` | `Alt` | 0 |
| `Gate` | `GateVal(n)` | 1–16 |
| `Bank` | `Off` | -1 |
| `Bank` | `BankNumber(n)` | 0–127 |
| `KnobCC` | `Off` | 0 |
| `KnobCC` | `KnobCcNumber(n)` | 1–127 |

### Non-Obvious Offset Transformations

| Field | Domain range | Raw range | Read formula | Write formula |
|---|---|---|---|---|
| Sound wave | 1-based | 0-based | `raw + 1` | `domain - 1` |
| PadPan | -15..15 | 0..30 | `raw - 15` | `domain + 15` |
| ClickPan | -15..15 | 0..30 | `raw - 15` | `domain + 15` |
| EQ gains | -12.0..12.0 | 0..24 | `(raw - 12).toFloat()` | `(domain + 12).toInt()` |
| Tempo | 20.0–260.0 | 200–2600 | `raw / 10.0` | `(domain * 10).toInt()` |

### FXType (21 effect types for kit/master FX)

| Name | Value |
|---|---|
| THRU | 0 |
| STEREO_DLY | 1 |
| SYNC_DELAY | 2 |
| TAPE_ECHO | 3 |
| CHORUS | 4 |
| FLANGER | 5 |
| STEP_FLNGR | 6 |
| PHASER | 7 |
| STEP_PHASR | 8 |
| EQ | 9 |
| COMPRESSOR | 10 |
| FILTER | 11 |
| FILT_DRIVE | 12 |
| ISOLATOR | 13 |
| TOUCH_WAH | 14 |
| DISTORTION | 15 |
| RINGMOD | 16 |
| PITCHSHIFT | 17 |
| VIBRATO | 18 |
| REVERB | 19 |
| SLICER | 20 |
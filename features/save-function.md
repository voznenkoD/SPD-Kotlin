# Save Function Requirements

## Goal

Implement `DeviceManager.saveDevice()` — the reverse of `readDevice()`. Convert the in-memory domain model (`Device`) back to raw XML-mapped structures and write them to the original SPD file tree.

---

## Scope

The save operation covers all domain entities currently loaded by `readDevice()`:

| Domain Model | Raw Model | Target File(s) |
|---|---|---|
| `SetupConfig` | `SetupPrm` (inside `Config`) | `SYSTEM/sysparam.spd` |
| `SystemConfig` | `SysPrm`, `KitChainPrm`, `MEfctPrm` (inside `Config`) | `SYSTEM/sysparam.spd` |
| `Kit` (list) | `KitPrm` | `KIT/KIT_000.spd`, `KIT_001.spd`, … |
| `Wave` (list) | `WvPrm` | `WAVE/PRM/{folder}/{file}.spd` |
| `WaveListsHolder` | `TagList`, `WvListSortbyName`, `WvListSortbyNameTag`, `WvListSortbyNumTag` | `SYSTEM/tag_list.spd`, `SYSTEM/wavelist_name.spd`, `SYSTEM/wavelist_tagname.spd`, `SYSTEM/wavelist_tagnum.spd` |

---

## 1. `toRaw()` Conversion Methods

Each domain model needs a reverse mapping method. These are the exact inverse of the existing `fromValues`/`fromRaw`/`fromValue` factories.

Use documentation/field-mapping.md file for details of the mappings.

## 2. XML Serialization

### 2.1 XmlParser Write Methods

Add serialization counterparts to existing parse methods in `XmlParser`:

- `writeKitFile(kitPrm: KitPrm, file: File)` — serialize single kit to XML
- `writeSystemConfig(config: Config, systemDir: File)` — serialize `Config` to `sysparam.spd` (must strip `<Root>` wrapper that was added during read)
- `writeSystemFile<T>(data: T, filename: String, systemDir: File)` — serialize `TagList`, `WvListSortby*` files
- `writeWaveFile(wvPrm: WvPrm, file: File)` — serialize single wave param file

Use the existing `XmlMapper` instance (Jackson) for serialization — `xmlMapper.writeValueAsString()` or `xmlMapper.writeValue(file, obj)`.

### 2.2 sysparam.spd Special Handling

During read, `sysparam.spd` content is wrapped in `<Root>...</Root>` before parsing:
```kotlin
val wrappedXml = "<Root>$xmlContent</Root>"
xmlMapper.readValue(wrappedXml, Config::class.java)
```

The save operation must produce the inverse: serialize `Config` to XML, then **strip the `<Root>` wrapper** before writing to disk. The file should contain the raw inner XML elements (SetupPrm, SysPrm, KitChainPrm, MEfctPrm) without a root wrapper.

---

## 3. File Write Strategy

### 3.1 Target Paths

Use documentation/device-folder-structure.md for folder structure.

### 3.2 Overwrite Existing Files

Save should overwrite the original files in place. The SPD device expects its files at exact paths — no temp files, no renames.

### 3.3 Write Only Changed Entities

To minimize I/O and reduce risk of corruption, consider tracking which entities were modified and only writing those files. As a first iteration, writing all files is acceptable.

---

## 4. DeviceManager.saveDevice() Orchestration

Implement the currently stubbed `saveDevice()` method:

```
1. Guard: return early if device is null or rootPath is empty
2. Convert domain → raw:
   a. SetupConfig.toRaw() → SetupPrm
   b. SystemConfig.toRaw() → (SysPrm, KitChainPrm, MEfctPrm)
   c. Assemble Config(SetupPrm, SysPrm, KitChainPrm, MEfctPrm)
   d. Kit.toRaw() for each kit → List<KitPrm>
   e. Wave.toRaw() for each wave → Map<Coordinate, WvPrm>
   f. WaveListsHolder.toRaw() → (TagList, WvListSortbyName, WvListSortbyNameTag, WvListSortbyNumTag)
3. Serialize and write:
   a. Write Config → SYSTEM/sysparam.spd (with Root wrapper removal)
   b. Write TagList → SYSTEM/tag_list.spd
   c. Write WvListSortbyName → SYSTEM/wavelist_name.spd
   d. Write WvListSortbyNameTag → SYSTEM/wavelist_tagname.spd
   e. Write WvListSortbyNumTag → SYSTEM/wavelist_tagnum.spd
   f. Write each KitPrm → KIT/KIT_{index:03d}.spd
   g. Write each WvPrm → WAVE/PRM/{folder}/{file}.spd
```

---

## 5. Critical Transformation Pitfalls

These are the non-obvious inversions that are easy to get wrong:

| # | Pitfall | Detail |
|---|---|---|
| 1 | **Wave number offset** | Domain `wave` is 1-based (`raw + 1`). When saving back: `raw = domain - 1`. Applies to `Sound.wave` → `Wv`/`SubWv`. |
| 2 | **Pan offset** | Domain `PadPan` stores -15..15. Raw is 0..30. Save: `raw = domain + 15`. Same for `ClickPan`. |
| 3 | **EQ gain offset** | Domain stores float gain. Raw is int with +12 offset. Save: `raw = (gain + 12).toInt()`. |
| 4 | **Name padding** | Kit names must pad to 8 ints, sub-names to 16, wave names to 11, tag names to 12, kit chain names to 10. Trailing zeros. |
| 5 | **Tempo encoding** | Domain is `Double` (e.g. 120.0). Raw is `Int` multiplied by 10 (e.g. 1200). |
| 6 | **sysparam.spd Root wrapper** | Must be stripped on write — file stores raw elements without `<Root>`. |
| 7 | **Wave coordinate math** | `waveNumber` → `folder = (num-1) / 100`, `file = (num-1) % 100`. File on disk is `{file}.spd`. |
| 8 | **Sealed class values** | `MuteGroup.Off` → -1, `PadCH.Global` → -1, `MidiNote.Off` → -1, `Gate.Off` → -1, `Gate.Alt` → 0. Each has distinct sentinel values. |
| 9 | **FX param arrays** | `KitFX` params are 20 individual fields (`Fx1Prm0`–`Fx1Prm19`). Must unpack list back to named fields. |
| 10 | **Kit chain key mapping** | Domain uses `Map<Char, KitChain>` ('A'–'H'). Raw uses array index (0–7). |

---

## 6. Testing Strategy

### 6.1 Round-Trip Test

The primary correctness validation: read a device folder, save it back, read again, and assert the domain models are identical.

```
readDevice(path) → Device A
saveDevice()
readDevice(path) → Device B
assert(A == B)
```

### 6.2 Unit Tests Per toRaw() Method

Each `toRaw()` conversion should have unit tests verifying the inverse of corresponding `fromValues`/`fromRaw`:

```
raw → fromValues() → domain → toRaw() → raw2
assert(raw == raw2)
```

Focus on edge cases from the pitfalls table: zero-padded names, boundary pan values (-15, 0, 15), wave number 1 (maps to raw 0), sealed class sentinel values.

### 6.3 XML Output Validation

Compare serialized XML against original files byte-for-byte or structurally (parse both and compare). Pay attention to:
- Element ordering (Jackson may reorder)
- Numeric formatting (no trailing decimals on ints)
- The `sysparam.spd` root wrapper stripping
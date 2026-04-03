# SPD Device Folder Structure

The Roland SPD-Pro exports its configuration as a folder tree. The root path is stored in `Device.rootPath` and all I/O is relative to it.

```
{rootPath}/
├── SYSTEM/
│   ├── sysparam.spd            System + Setup + KitChain + MasterFX config (single file)
│   ├── tag_list.spd            Wave category/tag definitions
│   ├── wavelist_name.spd       Wave indices sorted globally by name
│   ├── wavelist_tagname.spd    Wave indices per category, sorted by name
│   └── wavelist_tagnum.spd     Wave indices per category, sorted by number
│
├── KIT/
│   ├── KIT_000.spd             Kit 0 parameters
│   ├── KIT_001.spd             Kit 1 parameters
│   ├── ...                     (one file per kit, zero-padded 3-digit index)
│   └── KIT_NNN.spd
│
└── WAVE/
    └── PRM/
        ├── 0/
        │   ├── 00.spd          Wave param file
        │   ├── 01.spd
        │   └── ...
        ├── 1/
        │   ├── 00.spd
        │   └── ...
        ├── 2/
        │   └── ...
        └── N/
            └── NN.spd
```

## File Details

### SYSTEM/sysparam.spd

Contains four XML sections concatenated without a root element. During read, the parser wraps the content in `<Root>...</Root>` to make it valid XML, then deserializes to `Config` which holds:

| XML element | Raw class | Domain model |
|---|---|---|
| `<SetupPrm>` | `SetupPrm` | `SetupConfig` |
| `<SysPrm>` | `SysPrm` | `SystemConfig` (partial) |
| `<KitChainPrm>` | `KitChainPrm` | `SystemConfig.kitChains` |
| `<MEfctPrm>` | `MEfctPrm` | `SystemConfig.masterEffectConfig` + `SystemConfig.systemAudioConfig.eq` |

When writing back, the `<Root>` wrapper must be stripped — the file stores raw concatenated elements.

### SYSTEM/tag_list.spd

XML-serialized `TagList` containing a list of `TagPrm` entries. Each tag defines a wave category (name encoded as 12 ASCII ints + order field).

### SYSTEM/wavelist_*.spd

Pre-sorted wave index lists used by the device for quick lookup:

| File | Raw class | Content |
|---|---|---|
| `wavelist_name.spd` | `WvListSortbyName` | Flat `List<Int>` of wave numbers sorted by name |
| `wavelist_tagname.spd` | `WvListSortbyNameTag` | `List<WvList>` — one `WvList` per category, each sorted by name |
| `wavelist_tagnum.spd` | `WvListSortbyNumTag` | `List<WvList>` — one `WvList` per category, each sorted by number |

### KIT/KIT_{index}.spd

One file per kit. Index is zero-padded to 3 digits (`KIT_000.spd`, `KIT_001.spd`, ...). Each file is XML-serialized `KitPrm` containing kit-level params and an embedded `PadPrm` list (up to 15 pads).

### WAVE/PRM/{folder}/{file}.spd

Wave parameter files organized in a two-level folder hierarchy. Each file is XML-serialized `WvPrm`. The wave number is derived from the path via `Coordinate`:

```
wave number = folderNumber * 100 + fileNumber + 1
```

| Example path | Folder | File | Wave number |
|---|---|---|---|
| `WAVE/PRM/0/00.spd` | 0 | 0 | 1 |
| `WAVE/PRM/0/99.spd` | 0 | 99 | 100 |
| `WAVE/PRM/1/00.spd` | 1 | 0 | 101 |
| `WAVE/PRM/3/45.spd` | 3 | 45 | 346 |

Reverse (wave number to path):
```
folder = (waveNumber - 1) / 100
file   = (waveNumber - 1) % 100
path   = WAVE/PRM/{folder}/{file}.spd
```

## XML Format

All `.spd` files use Jackson XML serialization. The parser uses `XmlMapper` with the Kotlin module. Files are plain XML (no XML declaration header expected). Hidden files (starting with `.`) are skipped during reading.
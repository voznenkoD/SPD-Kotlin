## Feature: Wave Usage Indicator

### Summary
Build a precomputed wave-to-kit usage index when the device is loaded. Show a bold dot (●) next to each wave in the wave list that is used by at least one kit (in any pad's main or sub sound). On right-click, show a context menu listing the kit names the wave appears in — clicking a kit name selects that kit.

### Acceptance Criteria
- [ ] On device load, a `Map<Int, List<String>>` (wave number → list of kit names) is built by scanning all kits' pads' main and sub `Sound.wave` values
- [ ] This map is in-memory only (not persisted to disk), rebuilt each time the device is loaded
- [ ] The map is updated whenever kits change (pad wave assignments, kit reorder, copy/paste, wave removal)
- [ ] In all three wave list views (by name, by category name, by category num), waves that appear in the usage map display a bold "●" indicator before the wave name
- [ ] Right-clicking a wave in any wave list view shows a context menu with existing entries (if any) followed by a separator and entries like "Used in: KitName1", "Used in: KitName2" (one per kit)
- [ ] Clicking a "Used in: KitName" entry selects that kit (same as clicking the kit in the kit list)
- [ ] Waves not used in any kit show no dot and no "Used in" context menu entries
- [ ] Performance: the index is built once on load and updated incrementally — no per-frame scanning of all kits

### Affected Layers
- **Model:** Add a `waveUsage: Map<Int, List<String>>` computed property or helper. Built from `kits` list. Not serialized.
- **DeviceManager:** Recompute `waveUsage` whenever `device` is mutated (kit updates, pad updates, move kit, copy/paste). Add a `buildWaveUsageMap(kits: List<Kit>): Map<Int, List<String>>` helper.
- **ViewModel:** No new ViewModel — usage map is derived from `device` state. MainViewModel needs to expose a way for wave list context menu clicks to select a kit by name.
- **Components:** Modify wave list item rendering in `ListsScreen`/`WaveListByCategory`/`GenericListItemView` to show ● indicator and add `ContextMenuArea` with clickable kit names
- **Screen:** `ListsScreen.kt` — accept usage map and kit selection callback, pass to wave list components
- **Navigation:** No new screen needed

### Mapping Reference
`Map_Kit.md`, `Map_Pad.md` — wave assignment is via `Wv` and `SubWv` fields in pad parameters
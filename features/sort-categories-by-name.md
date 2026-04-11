## Feature: Sort Categories Alphabetically In Name View

### Summary
In the `BY_CATEGORY_NAME` wave list view, display category groups in alphabetical order by name (case-insensitive), with the `Default` category pinned to the top. This makes a category rename immediately bubble the category to its new alphabetical slot. `BY_CATEGORY_NUM` continues to follow the device's `Category.order` field.

### Acceptance Criteria
- [ ] In `BY_CATEGORY_NAME` view, category headers are rendered with `Default` first, then remaining categories in case-insensitive ascending alphabetical order by `Category.name`
- [ ] In `BY_CATEGORY_NUM` view, category headers remain in `Category.order` sequence (unchanged)
- [ ] Renaming a category in `BY_CATEGORY_NAME` immediately re-positions it to its new alphabetical slot without requiring a tab switch or any other user action
- [ ] If a category is renamed to/from `Default`, pinning behavior updates accordingly
- [ ] The renamed category keeps its expand/collapse state after re-sorting
- [ ] Waves within each category retain their existing per-category ordering (by name in name view, by number in num view)
- [ ] On save, `tag_list.spd` is still written in `Category.order` sequence — sorting is view-only and does not affect persistence

### Affected Layers
- **Model:** No changes to `WaveListsHolder` or `Category`. Sorting stays view-side so persistence semantics are untouched.
- **DeviceManager:** No changes — existing `renameCategory` already produces a new holder.
- **ViewModel:** No changes.
- **Components:** `ListsScreen.kt` — in the `BY_CATEGORY_NAME` branch, pass a view-sorted `LinkedHashMap` into `WaveListByCategory`, with entries ordered by: `Default` first, then remaining keys sorted by `name.lowercase()` ascending. `BY_CATEGORY_NUM` branch passes the map as-is.
- **Screen:** No changes to `MainScreen.kt`.
- **Navigation:** No new screen.

### Mapping Reference
`Map_System.md` (category/tag list lives in system config — `tag_list.spd`).

### Key Design Decisions (confirmed by user)
- Sort comparator: case-insensitive via `name.lowercase()` (not locale-aware `String.CASE_INSENSITIVE_ORDER`)
- `Default` category is always pinned to the top regardless of alphabetical position
- Sorting is applied only in `BY_CATEGORY_NAME` view; `BY_CATEGORY_NUM` stays on device `order` sequence
- Sorting is view-only — `tag_list.spd` persistence order (by `Category.order`) is unchanged
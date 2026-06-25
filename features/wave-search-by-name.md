## Feature: Wave Search by Name

### Summary
Add a search/filter text field to the **Waves** tab in `ListsScreen` that filters the
displayed wave list in real time by matching the typed text against wave names, across all
waves regardless of the active sorting mode (By Name, By Category (Name), By Category (Number)).

### Acceptance Criteria
- [ ] A search text field appears at the top of the Waves tab content (below the TabRow, above the wave list).
- [ ] Typing filters the visible waves to those whose `name` **contains** the query — case-insensitive, substring match anywhere in the name (NOT prefix-based).
- [ ] When matches are found, only the matching (filtered) waves are shown.
- [ ] Filtering works across **all** waves and respects the current sorting mode:
  - **By Name:** flat list filtered to matching waves.
  - **By Category (Name/Number):** each category shows only its matching waves. Categories with matches **auto-expand** while a query is active. Categories with zero matches remain **shown (empty)** — they are NOT hidden.
- [ ] A "✕" clear icon appears next to the search field. Clicking it clears the query and restores the full list, while keeping the currently selected wave selected/highlighted.
- [ ] When the query yields no matches at all, a subtle "No waves found" placeholder is shown.
- [ ] Empty query = full list (current behavior unchanged).
- [ ] The field only appears on the Waves tab, not the Kits tab. The query clears when switching tabs (does not persist).
- [ ] Existing behaviors preserved: wave selection, right-click context menu, drag-to-pad, auto-scroll to selected wave, category collapse/expand.
- [ ] Styling matches existing theme (ColorSurface/ColorBackground containers, ColorAccentOrange accent, ColorDivider border), consistent with the rename dialog TextField.

### Affected Layers
- **Model:** None.
- **DeviceManager:** None.
- **ViewModel:** Local Compose state (`remember { mutableStateOf("") }`) inside `ListsScreen`. Filtering is a pure derived computation over `waveListsHolder`. Reset the query when `selectedTab` changes away from the Waves tab.
- **Components:** Add a `WaveSearchField` composable (text field + clear "✕" icon). Reuse `WaveListByName` / `WaveListByCategory` with pre-filtered lists. Add a "No waves found" empty-state.
- **Screen:** `ListsScreen.kt` — insert the search field in the `selectedTab == 1` branch, above the `when (sortingMode)` content. Filter `wavesByName`, `wavesByNamePerCategory`, `wavesByNumPerCategory` by the query before passing them down. Auto-expand matching categories while a query is active.
- **Navigation:** No new screen.

### Mapping Reference
None — pure UI/UX feature, no device parameter changes.

### Decisions (resolved)
1. Auto-expand categories that contain matches while a query is active.
2. Query clears when switching tabs (no persistence).
3. Match by name only (not by wave number).
4. Categories with zero matches are shown empty (not hidden).
5. Substring/contains match anywhere in the name, case-insensitive (not prefix-based).
6. When several waves match, show only the filtered waves.
7. "✕" icon next to the field clears the search but keeps the currently selected wave.
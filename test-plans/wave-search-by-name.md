# Test Plan: Wave Search by Name

Feature file: `features/wave-search-by-name.md`
Implementation: `ui/components/lists/ListsScreen.kt`

## Setup
1. Launch app (`./gradlew desktopRun`) and open a folder with an SPD device export containing many waves across multiple categories.
2. Open the lists panel and select the **Waves** tab.

## Manual Test Cases

### 1. Search field visibility
- [ ] Search field appears at the top of the Waves tab (below the tab row, above the list).
- [ ] Switch to the **Kits** tab — search field is NOT shown.
- [ ] Placeholder text "Search waves by name" is visible when the field is empty.

### 2. Substring (contains) matching
- [ ] Type a fragment that appears in the MIDDLE of several wave names (not the start). Only waves whose name contains that fragment remain.
- [ ] Verify matching is case-insensitive (e.g. "snare" matches "Snare", "BIGSNARE").
- [ ] Verify a numeric wave number typed in does NOT match by number (name-only matching).

### 3. Sorting modes (right-click the Waves tab to change view)
- [ ] **By Name:** flat list shows only matching waves.
- [ ] **By Category (Name):** only matching waves shown per category; categories WITH matches auto-expand; categories with zero matches stay shown but empty (header only).
- [ ] **By Category (Number):** same behavior as above.

### 4. Clear button
- [ ] When text is present, a "✕" icon appears at the right of the field.
- [ ] Click "✕" → query clears, full list restored.
- [ ] Select a wave, type a query, then click "✕" → the previously selected wave is still selected/highlighted (and list auto-scrolls to it).

### 5. Empty state
- [ ] Type a query that matches nothing → "No waves found" placeholder shown (in every sorting mode).

### 6. Tab persistence
- [ ] Type a query, switch to Kits tab, switch back to Waves → query is cleared (empty field, full list).

### 7. Preserved behaviors (with and without an active query)
- [ ] Clicking a filtered wave selects it and opens its details.
- [ ] Right-click context menu on a filtered wave still works (used-in, import, delete, view modes).
- [ ] Drag-to-pad from a filtered wave still works.
- [ ] Manual category collapse/expand state is restored after clearing the query.
- [ ] Empty query = identical to original behavior (full list, manual expand state).

### 8. Edge cases surfaced in code review
- [ ] **No scroll jitter while typing:** select a wave, then type/continue typing in the search box — the list must NOT jump/scroll on each keystroke.
- [ ] **Manual collapse during search is local to the term:** with a query active, collapse a matching category; it stays collapsed while that exact query is unchanged. Typing more characters (new term) resets categories to auto (matches expanded).
- [ ] **Manual state restored on clear:** before searching, collapse category X; run a search, select a wave inside X, then clear the search — X must remain collapsed (persisted manual state untouched).
- [ ] **Rename during search:** with a query active, toggle a matching category, then rename it — the toggle state carries to the new name.
- [ ] **Empty results keep context menu:** right-click the "No waves found" area → "Import Wave…" and the view-mode entries are available.

## Suggested automated/unit coverage
The matching logic is a pure predicate `name.contains(query, ignoreCase = true)`. If unit tests are added, cover:
- contains-anywhere (not just prefix), case-insensitivity, blank query returns full list,
  category map filtering preserves all keys (empty categories retained) and order.
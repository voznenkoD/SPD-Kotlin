## Feature: Reorder kit chain via drag-and-drop

### Summary
Let the user reorder the kits within a kit chain (System screen → Kit Chain tab) by long-press dragging an entry up/down, mirroring the existing drag-to-reorder behavior of the main Kit list.

### Acceptance Criteria
- [ ] In `KitChainView`, the currently selected chain's entries can be **long-press dragged** to a new position; on release the chain's order updates to match.
- [ ] Visual feedback matches the kit list: the dragged row follows the cursor (translation + raised), and the drop-target row is highlighted (orange border / accent background).
- [ ] On drop, the new order is **persisted** through the existing `onUpdate(Map<Char, KitChain>)` → `updateKitChains` → `updateSystemConfig` path (survives Save).
- [ ] The displayed 1-based position numbers (`01`, `02`, …) renumber correctly after a reorder.
- [ ] Reordering affects **only the selected chain** (the chain under the active tab); other chains are unchanged.
- [ ] Every `kitRef` is shown as a row. A ref pointing to a missing kit displays the literal **`No Kit`** in place of the kit name (instead of being filtered out). This makes display order map 1:1 to `kitRefs` order.
- [ ] No regression to chain selection (tab switching) or the read-only rendering when not dragging.

### Affected Layers
- **Model:** none — `KitChain(name, kitRefs: List<Int>)` already immutable; reorder = new `kitRefs` order.
- **DeviceManager:** none new — reuse `updateSystemConfig`.
- **ViewModel:** `SystemViewModel` — add `moveKitInChain(chainKey: Char, from: Int, to: Int)` that rebuilds the chain's `kitRefs` (removeAt/add) and calls `updateKitChains`.
- **Components:** `KitChainView.kt` — replace the passive `GenericListView` with a `LazyColumn` + `itemsIndexed` over the selected chain's `kitRefs` (mapped to display rows including `No Kit` placeholders), carrying the drag gesture (`detectDragGesturesAfterLongPress`, `draggedIndex`/`dragOffsetY`/`targetIndex`) replicated from `KitListView`. Keep the existing row content format (`NN   kit#   name subName`), substituting `No Kit` when the ref is invalid.
- **Screen:** `SystemScreen.kt` — thread the move callback (chain key + from/to) to `KitChainView`.
- **Navigation:** none.

### Mapping Reference
`Map_System.md` (kit chains live in System config).

### Decisions
1. Long-press to start dragging (consistent with the Kit list).
2. Reordering applies to the active tab's chain only.
3. Missing-ref entries are shown as a `No Kit` row (not filtered), so all `kitRefs` are draggable and indices align 1:1.

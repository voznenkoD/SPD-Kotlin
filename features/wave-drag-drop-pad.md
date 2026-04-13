## Feature: Wave Drag-and-Drop to Pad

### Summary
Drag a wave from the wave list and drop it onto a pad's main or sub area to assign that wave to the pad in the currently selected kit. Uses long-press to initiate drag, floating label follows cursor, and PointerEventPass at MainScreen level for cross-panel tracking.

### Acceptance Criteria
- [ ] User initiates a drag from any wave row in the wave list (all three sort modes) via **long-press-and-drag** (matching the existing kit reorder gesture)
- [ ] While dragging, a **floating label** follows the cursor showing the wave being dragged (wave number + name)
- [ ] Each pad's main (top half) and sub (bottom half) areas act as drop targets — they **highlight** when the drag hovers over them (colored border or background tint)
- [ ] Dropping on the **top half** of a pad assigns the wave to `pad.main.wave`
- [ ] Dropping on the **bottom half** assigns to `pad.sub.wave`
- [ ] Assignment only works when a kit is selected — if no kit is selected, drop does nothing
- [ ] After a successful drop, the pad label updates immediately to show the new wave name
- [ ] Dropping outside any pad cancels the drag (no assignment)
- [ ] The drag does not interfere with existing wave list interactions (click to select, right-click context menu, scroll)
- [ ] FS pads (foot switches) are also valid drop targets

### Affected Layers
- **Model:** No changes
- **DeviceManager:** Reuses existing `updatePad` with `pad.copy(main = main.copy(wave = waveNumber))` pattern
- **ViewModel:** `MainViewModel`
  - `draggedWaveNumber: StateFlow<Int?>` — the wave being dragged (null when not dragging)
  - `draggedWaveName: StateFlow<String?>` — display name for the floating label
  - `dragPosition: StateFlow<Offset?>` — global cursor position during drag (for the floating label)
  - `startWaveDrag(waveNumber: Int, waveName: String)` — initiates drag state
  - `updateDragPosition(position: Offset)` — updates cursor position during drag
  - `cancelWaveDrag()` — clears drag state (drop outside target or drag cancelled)
  - `dropWaveOnPad(padNumber: PadNumber, isMain: Boolean)` — assigns the dragged wave to the pad's main/sub sound in the current kit, then clears drag state
- **Components:**
  - `WaveListItem` (ListsScreen.kt) — add `detectDragGesturesAfterLongPress` as drag source. On drag start, call `onStartWaveDrag(wave.number, wave.name)`. On drag move, call `onUpdateDragPosition(globalPosition)`. On drag end/cancel, call `onCancelWaveDrag()`. Thread these callbacks through ListsScreen → WaveListByName/WaveListByCategory → WaveListItem.
  - `PadItem` (KitPadsScreen.kt) — accept `isDragActive: Boolean` and `onDropWave: (PadNumber, Boolean) -> Unit`. When `isDragActive` is true, the main and sub halves become drop targets: detect pointer enter/hover to highlight, detect pointer release (drag end) to trigger drop. Use `pointerInput` to track whether the cursor is in the top or bottom half and show the appropriate highlight.
  - Thread `isDragActive` and `onDropWave` through `PadScreen`.
- **Screen:** `MainScreen.kt`
  - Collect `draggedWaveNumber`, `draggedWaveName`, `dragPosition` from VM
  - Pass drag callbacks (`onStartWaveDrag`, `onUpdateDragPosition`, `onCancelWaveDrag`) into ListsScreen
  - Pass `isDragActive` and `onDropWave` into PadScreen
  - Render a floating overlay `Box` at `dragPosition` showing the wave label when `draggedWaveNumber != null`. Use `Modifier.offset()` with the drag position. The overlay should be rendered last (on top of everything) in the MainScreen's root layout.
- **Navigation:** No new screen

### Cross-Panel Drag Implementation
The drag gesture originates in the wave list (right column) but needs to track position and detect drop in the pad grid (center column). Approach:
1. `WaveListItem` uses `detectDragGesturesAfterLongPress` to capture drag gestures
2. During `onDrag`, convert the drag position to **window-global coordinates** using `change.position` + layout coordinates
3. Store the global position in `MainViewModel.dragPosition`
4. `PadItem` uses `onGloballyPositioned` to report its global bounds to a shared layout-coordinate map (or checks containment at drop time)
5. On drag end in `WaveListItem`, call a VM method that checks which pad (if any) contains the final drag position, and performs the drop
6. Alternative simpler approach: PadItem registers its bounds via `LayoutCoordinates`, and MainViewModel's `dropWaveAtPosition(position: Offset)` iterates registered pad bounds to find the hit target

### Key Design Decisions (confirmed by user)
- **Drag trigger:** Long-press-and-drag (consistent with kit reorder)
- **Visual feedback:** Floating label following cursor showing wave number + name
- **Cross-panel tracking:** PointerEventPass at MainScreen level with shared ViewModel state

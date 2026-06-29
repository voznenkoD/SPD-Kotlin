# Feature: Duplicate Kit

## Summary
Add a "Duplicate" option to the kit-list right-click context menu that deep-copies the
selected kit, appends the copy to the end of the kit list, and selects/scrolls to it.
Enforces a maximum of 99 kits, showing a reusable "maximum reached" dialog when full.

## Acceptance Criteria
- [ ] Right-clicking a kit row in the Kits list shows a **"Duplicate"** menu item (alongside Copy / Paste).
- [ ] Selecting "Duplicate" creates a full copy of that kit (all fields: name, subName, tempo,
      volume, padLink, fx1, fx2, pads) and inserts it at the **end** of the kit list.
- [ ] The duplicate's name is the source name with **`2` appended**; if the result would exceed the
      8-char kit-name limit, the source is truncated to 7 chars so the final name fits in 8
      (e.g. `"SNARE" -> "SNARE2"`, `"LONGNAME" -> "LONGNAM2"`). A `2` is always appended even when
      the name already ends in `2` (e.g. `"SNARE2" -> "SNARE22"`). No uniqueness enforcement.
- [ ] After duplicating, the **new kit becomes selected** and the list **scrolls** to it.
- [ ] Existing kit indices are unchanged (append-only), so KitChain references remain valid.
- [ ] If the list already has **99 kits**, "Duplicate" does not add a kit and instead shows a dialog
      titled/messaged **"Maximum of kit numbers has been reached"**.
- [ ] The max-reached dialog is a **reusable composable** (usable by future kit-limit cases), with a
      single OK/dismiss button styled like the app's other AlertDialogs. (No create-new-kit action is
      built as part of this feature.)

## Affected Layers
- **Model:** none (reuse `Kit.copy()`).
- **DeviceManager:** add `duplicateKit(sourceIndex): Int?` that appends a copy and returns the new
  index, or `null` when the 99 cap is reached. Add a `MAX_KITS = 99` constant.
- **ViewModel (MainViewModel):** add `duplicateKit(kit)` that computes the `+2` name, calls
  DeviceManager, sets `selectedKitIndex` to the new kit on success; exposes a `kitLimitReached`
  state + a clear/dismiss handler to drive the dialog.
- **Components:** add `onDuplicateKit` callback + "Duplicate" `ContextMenuItem` to `KitListView`;
  add a `LaunchedEffect` scroll-to-`selectedKitIndex` (mirroring the wave list, which `KitListView`
  currently lacks). Add a reusable `KitLimitReachedDialog` composable.
- **Screen:** thread `onDuplicateKit` + dialog state through `MainScreen` -> `ListsScreen` -> `KitListView`.
- **Navigation:** no new screen.

## Mapping Reference
`Map_Kit.md` (kit data model).

## Resolved Decisions
- Create-new-kit action is **out of scope**; only ensure the limit dialog is reusable.
- Duplicate naming always appends `2` (allowing repeated `2`s and non-unique names); truncate the
  source to 7 chars when needed to stay within the 8-char kit-name limit.
- Max 99 kits; over-limit shows the reusable "Maximum of kit numbers has been reached" dialog.
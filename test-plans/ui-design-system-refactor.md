## Test Plan: UI Design System Refactor

Covers the feature specified in `features/ui-design-system-refactor.md`.

---

### 1. Foundation: DesignTokens + LocalScale

- **T1.1** `ColorAccentOrange` == `Color(0xFFC45C00)` (verify token values match guidelines)
- **T1.2** `LocalScale.current` returns 1.0f at 1280px window width
- **T1.3** `LocalScale.current` returns 0.75f at window widths <= 960px
- **T1.4** `LocalScale.current` returns 1.5f at window widths >= 1920px
- **T1.5** `Typography.bodySize` scales: at scale 1.0 == 13.sp, at scale 1.5 == 19.5.sp
- **T1.6** `Heights.control` scales: at scale 1.0 == 32.dp, at scale 0.75 == 24.dp

### 2. Manual UI smoke tests

Run `./gradlew desktopRun` against a real device folder.

**Color tokens**
- **M2.1** App background is light gray (#F0F0F0), not white
- **M2.2** List items use #E4E4E4 surface, not white
- **M2.3** Selected list items use dark gray (#5A5A5A) with yellow text (#F5C542)
- **M2.4** Primary action buttons (Select Folder) use orange (#C45C00) with white text
- **M2.5** Dividers/borders are #CCCCCC, not black or dark gray
- **M2.6** Knob arcs use orange accent, track uses divider gray
- **M2.7** ButtonRowCompact selected items use orange background + white text
- **M2.8** ToggleSwitchWithLabel active label uses orange color

**Typography**
- **M2.9** Section headers (e.g. "Compressor Settings") use 16sp SemiBold
- **M2.10** Control labels use 13sp Normal
- **M2.11** Knob value readouts use 12sp Monospace
- **M2.12** No hardcoded font sizes remain visible (everything scaled consistently)

**Responsive scaling**
- **M2.13** Resize window to ~900px wide — UI scales down (smaller text, compact controls), nothing overflows
- **M2.14** Resize window to ~1800px wide — UI scales up proportionally
- **M2.15** At all sizes, controls remain readable and aligned

**Component consistency**
- **M2.16** DropdownSelector — surface background, divider border, arrow in secondary color, rounded corners
- **M2.17** ButtonRowCompact — consistent height (32dp scaled), orange selected, surface unselected
- **M2.18** KnobControl — orange arc, divider track, label above in caption style, value below in mono style
- **M2.19** List items — consistent height (36dp scaled), surface background, divider borders
- **M2.20** Tabs — style matches (check active/inactive colors)

**Screens**
- **M2.21** MainScreen — background color, panel divider color
- **M2.22** PadScreen — pad colors match (selected = dark, unselected = surface, text = yellow on dark)
- **M2.23** All FX views — consistent title/label sizing, spacing, no hardcoded colors visible
- **M2.24** System screen — audio/click/visual control views use tokens
- **M2.25** Setup screen — pad setup, MIDI, general views use tokens

**No regressions**
- **M2.26** All controls still function (knobs, dropdowns, toggles, buttons, lists)
- **M2.27** Save/load device works correctly
- **M2.28** Drag-and-drop wave to pad still works with new colors
- **M2.29** Wave list highlight, scroll, auto-expand still work

## Feature: UI Design System Refactor

### Summary
Apply the UI guidelines from `.claude/skills/ui-guidelines/ui-guidelines.md` across the entire app: centralized design tokens, responsive scaling via CompositionLocal, and refactor all components/views to use tokens instead of hardcoded values. Replace Material3 theming with custom tokens.

### Source of Truth
The complete design spec is in `.claude/skills/ui-guidelines/ui-guidelines.md`. All color values, shape values, typography sizes, spacing constants, control specifications, and layout rules are defined there. **Follow that file exactly.**

### Phase 1 — Foundation

- [ ] Create `ui/theme/DesignTokens.kt` with ALL tokens from the guidelines:
  - Colors: `ColorBackground`, `ColorSurface`, `ColorSurfaceSelected`, `ColorSurfaceHover`, `ColorDivider`, `ColorAccentOrange`, `ColorAccentYellow`, `ColorTextPrimary`, `ColorTextSecondary`, `ColorTextOnDark`, `ColorTextOnAccent`, `ColorTextDisabled`
  - Shapes: `ShapeSmall`, `ShapeDefault`, `ShapeCard`
  - Typography: `TypographyTitle`, `TypographyBody`, `TypographyLabel`, `TypographyCaption`, `TypographyMono`
  - Spacing: `SpaceXS`, `SpaceS`, `SpaceM`, `SpaceL`, `SpaceXL`, `SpaceXXL`
  - Heights: `HeightControl`, `HeightButton`, `HeightListItem`, `HeightSectionBar`
- [ ] Create `LocalScale` CompositionLocal in App.kt — derive `scaleFactor` from window width using `baseWidth = 1280f`, clamped to `0.75f..1.5f`
- [ ] Provide `LocalScale` at the top level via `CompositionLocalProvider`
- [ ] Create scaled accessor functions/properties that multiply token values by `LocalScale.current` (e.g. `scaledHeightControl`, `scaledBody`, etc.)
- [ ] Remove Material3 theme wrapping — replace with custom token usage throughout

### Phase 2 — Common Components

Refactor each common component to use design tokens. Replace ALL hardcoded `Color.*`, `fontSize = N.sp`, padding values, and shapes:

- [ ] `KnobControl.kt` — `ColorAccentOrange` for arc/indicator, `ColorDivider` for track, `TypographyLabel` for label, `TypographyMono` for value, scaled knob size
- [ ] `DropdownSelector.kt` — `ColorSurface` background, `ColorDivider` border, `ColorSurfaceHover` hovered item, `ShapeDefault`, `HeightControl`, `TypographyBody` for value, `TypographyLabel` for label, `ColorTextSecondary` for arrow
- [ ] `ButtonRowCompact.kt` — `ColorAccentOrange`/`ColorTextOnAccent` for selected, `ColorSurface`/`ColorTextPrimary` for unselected, `ShapeDefault`, `HeightButton`
- [ ] `SwitchWithLabel.kt` / `ToggleSwitchWithLabel` — `ColorAccentOrange` track on, `ColorDivider` track off, `TypographyLabel`
- [ ] `GenericListItemView.kt` — `ColorSurface` default background, `ShapeDefault`, `HeightListItem`, `TypographyBody`
- [ ] `GenericListView.kt` — pass through correctly
- [ ] `SelectFolderButton.kt` — use button tokens

### Phase 3 — Screens and Views

- [ ] `MainScreen.kt` — `ColorBackground` root background, `ColorDivider` panel separator
- [ ] `ListsScreen.kt` — tab styling: `ColorSurfaceSelected` active tab, `ColorTextOnDark` active text, `ColorAccentOrange` indicator, `HeightListItem` tab height. Wave list item highlight uses `ColorSurfaceSelected`
- [ ] `KitPadsScreen.kt` / `PadItem` — use `ColorSurfaceSelected` for selected pad, `ColorSurface` for unselected, `ColorTextOnDark` for selected text, `ColorAccentOrange` for divider, `TypographyBody` for wave names
- [ ] `KitScreen.kt` — replace hardcoded sizes/colors
- [ ] `PadDetailsScreen.kt` — token-based styling
- [ ] `SoundSection.kt` — token-based
- [ ] All FX views (19 files) — replace `fontSize = 18.sp` titles with `TypographyTitle`, `16.sp` labels with `TypographyLabel`, padding with spacing tokens
- [ ] `AudioView.kt`, `ClickView.kt` — replace hardcoded colors/sizes
- [ ] `PadSetupView.kt`, `SetupMidiView.kt`, `SetupGeneralView.kt` — replace hardcoded values
- [ ] `SystemScreen.kt`, `SetupScreen.kt` — token-based backgrounds and layouts
- [ ] `WaveDetailsScreen.kt` — token-based

### Affected Layers
- **Model:** No changes
- **DeviceManager:** No changes
- **ViewModel:** No changes
- **Components:** ALL common components + ALL view components (30+ files)
- **Screen:** ALL screens
- **Navigation:** No changes — just theme wrapping in App.kt

### Key Design Decisions (confirmed by user)
- **All three phases** in one implementation pass
- **Replace Material3 theme** entirely with custom design tokens — do not overlay
- **Responsive scaling** implemented now via `LocalScale` CompositionLocal, derived from window width

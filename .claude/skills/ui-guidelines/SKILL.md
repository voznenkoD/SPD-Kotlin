# SPD-Manager UI Design Guidelines

Material-inspired desktop design system for the SPD-Manager Compose Desktop app.
All new and redesigned components must follow these guidelines. When in doubt,
match existing compliant components rather than inventing new patterns.

---

## Color Palette

### Backgrounds
| Token | Value | Usage |
|-------|-------|-------|
| `ColorBackground` | `#F0F0F0` | App background, default surface |
| `ColorSurface` | `#E4E4E4` | Cards, panels, list items |
| `ColorSurfaceSelected` | `#5A5A5A` | Selected list item, active tab background |
| `ColorSurfaceHover` | `#D6D6D6` | Hovered row or button |
| `ColorDivider` | `#CCCCCC` | Dividers, borders, separators |

### Accent
| Token | Value | Usage |
|-------|-------|-------|
| `ColorAccentOrange` | `#C45C00` | Primary action buttons, active indicators |
| `ColorAccentYellow` | `#C49A00` | Secondary accent, highlights, badges |

### Text
| Token | Value | Usage |
|-------|-------|-------|
| `ColorTextPrimary` | `#1A1A1A` | Default text on light backgrounds |
| `ColorTextSecondary` | `#5A5A5A` | Labels, captions, hints on light backgrounds |
| `ColorTextOnDark` | `#F5C542` | Text on dark/selected backgrounds (yellow) |
| `ColorTextOnAccent` | `#FFFFFF` | Text on orange accent buttons |
| `ColorTextDisabled` | `#AAAAAA` | Disabled control labels |

### Rule — Text on Background
- Light background (`ColorBackground`, `ColorSurface`) → use `ColorTextPrimary` or `ColorTextSecondary`
- Dark background (`ColorSurfaceSelected`) → use `ColorTextOnDark` (yellow)
- Accent background (`ColorAccentOrange`) → use `ColorTextOnAccent` (white)
- Never use dark text on dark backgrounds, never use light text on light backgrounds

---

## Shape

```kotlin
val ShapeSmall   = RoundedCornerShape(1.dp)   // chips, tags, badges
val ShapeDefault = RoundedCornerShape(2.dp)   // buttons, inputs, dropdowns, list items
val ShapeCard    = RoundedCornerShape(4.dp)   // panels, cards, section
```

- Never use fully sharp corners (`0.dp`) on interactive elements
- Never use pill shapes (`50%`) — this is not a mobile UI
- All interactive elements use `ShapeDefault` unless specified otherwise

---

## Typography

Single font family throughout: **Inter** (or system sans-serif fallback).

```kotlin
val TypographyTitle    = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
val TypographyBody     = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal)
val TypographyLabel    = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal)
val TypographyCaption  = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Normal)
val TypographyMono     = TextStyle(fontSize = 12.sp, fontFamily = FontFamily.Monospace)
```

| Token | Size | Weight | Usage |
|-------|------|--------|-------|
| `TypographyTitle` | 16sp | SemiBold | Screen titles, section headers |
| `TypographyBody` | 13sp | Normal | Control labels, list item text |
| `TypographyLabel` | 12sp | Normal | Input field labels, slider labels |
| `TypographyCaption` | 11sp | Normal | Hints, units, secondary info |
| `TypographyMono` | 12sp | Normal | Values, numeric readouts |

### Rules
- Use only the tokens above — never hardcode `fontSize` values
- Never mix font families within a single view
- Labels above controls use `TypographyLabel`
- Values displayed next to sliders use `TypographyMono`
- Do not use italic in UI chrome — reserve for placeholder text only

---

## Spacing & Sizing

### Base Unit
All spacing is a multiple of `4.dp`.

```kotlin
val SpaceXS  = 1.dp
val SpaceS   = 2.dp
val SpaceM   = 4.dp
val SpaceL   = 6.dp
val SpaceXL  = 8.dp
val SpaceXXL = 16.dp
```

### Element Heights
All interactive elements use a unified height to maintain visual rhythm:

```kotlin
val HeightControl    = 32.dp   // sliders, dropdowns, text fields, switches
val HeightButton     = 32.dp   // all buttons
val HeightListItem   = 36.dp   // list rows, tab rows
val HeightSectionBar = 28.dp   // collapsible section headers
```

- Never mix element heights within the same horizontal row
- Use `HeightControl` as the reference height for all compact rows
- Vertical padding inside a control: `SpaceM` (8.dp) top and bottom

### Element Widths
- Controls within a row use `Modifier.weight()` for proportional sizing
- Minimum interactive width: `80.dp`
- Label column in a form layout: fixed `120.dp`, value column fills remaining space
- Dropdown minimum width: `100.dp`

---

## Controls

### Buttons
```kotlin
Button(
    shape = ShapeDefault,
    colors = ButtonDefaults.buttonColors(
        containerColor = ColorAccentOrange,
        contentColor = ColorTextOnAccent
    ),
    modifier = Modifier.height(HeightButton)
)
```
- Primary action: `ColorAccentOrange` background, white text
- Secondary action: `ColorSurface` background, `ColorTextPrimary` text, `ColorDivider` border
- Destructive: `ColorAccentOrange` at 70% opacity
- Disabled: `ColorSurface` background, `ColorTextDisabled` text

### Text Fields & Inputs
```kotlin
TextField(
    shape = ShapeDefault,
    colors = TextFieldDefaults.colors(
        unfocusedContainerColor = ColorSurface,
        focusedContainerColor = ColorBackground,
        focusedIndicatorColor = ColorAccentOrange,
        unfocusedIndicatorColor = ColorDivider
    ),
    modifier = Modifier.height(HeightControl)
)
```
- Focused border: `ColorAccentOrange` (2.dp)
- Unfocused border: `ColorDivider` (1.dp)
- No floating label — use a static label above the field

### Dropdowns
- Same height and shape as text fields (`HeightControl`, `ShapeDefault`)
- Arrow icon: `ColorTextSecondary`
- Selected value text: `ColorTextPrimary`, `TypographyBody`
- Dropdown menu background: `ColorSurface`
- Hovered item: `ColorSurfaceHover`

### Sliders
- Track color: `ColorDivider` (inactive), `ColorAccentOrange` (active)
- Thumb color: `ColorAccentOrange`
- Thumb size: `14.dp` diameter
- Always pair with a value readout using `TypographyMono` to the right
- Always pair with a label using `TypographyLabel` to the left
- Row height: `HeightControl`

### Switches / Toggles
- Track on: `ColorAccentOrange`
- Track off: `ColorDivider`
- Thumb: `ColorBackground`
- Height: `HeightControl` row (switch itself is vertically centered)

### List Items
```kotlin
Row(
    modifier = Modifier
        .height(HeightListItem)
        .fillMaxWidth()
        .background(
            if (selected) ColorSurfaceSelected else ColorSurface,
            shape = ShapeDefault
        )
        .padding(horizontal = SpaceL)
)
```
- Default background: `ColorSurface`
- Selected background: `ColorSurfaceSelected`
- Default text: `ColorTextPrimary`, `TypographyBody`
- Selected text: `ColorTextOnDark` (yellow), `TypographyBody`
- Vertical divider between items: `ColorDivider`, `0.5.dp`

### Tabs
- Active tab background: `ColorSurfaceSelected`
- Active tab text: `ColorTextOnDark` (yellow)
- Inactive tab text: `ColorTextSecondary`
- Tab indicator underline: `ColorAccentOrange`, `2.dp`
- Tab height: `HeightListItem`

### Section Headers (collapsible)
- Background: `ColorSurface`
- Text: `TypographyTitle`, `ColorTextPrimary`
- Height: `HeightSectionBar`
- Expand/collapse icon: `ColorTextSecondary`
- Bottom border: `ColorDivider`

---

## Layout Principles

### Rows
- Related controls placed in a `Row` with `Modifier.weight()` — never hardcode widths
  inside rows unless a minimum width is necessary
- Horizontal padding between row items: `SpaceM` (8.dp)
- All items in a row share the same height (`HeightControl`)

### Panels & Cards
- Background: `ColorSurface`
- Shape: `ShapeCard`
- Internal padding: `SpaceL` (12.dp) all sides
- Elevation: none — use background color contrast instead of shadows

### Screen Layout
- Screen background: `ColorBackground`
- Content padding: `SpaceXL` (16.dp)
- List panel (left): fixed or `weight(0.3f)`
- Detail panel (right): fills remaining space `weight(0.7f)`
- Panel separator: `ColorDivider`, `1.dp` vertical line

---

## Responsive Scaling (Resizable Window)

### Principles
- The app window is resizable — UI must reflow gracefully from ~900px to ~1800px wide
- Use `weight()` everywhere instead of fixed widths for major layout regions
- Font sizes and small element sizes scale with a `scaleFactor` derived from window width

### Scale Factor
```kotlin
// Compute in App.kt or a top-level CompositionLocal
val baseWidth = 1280f  // design baseline width in px
val scaleFactor = (windowWidth / baseWidth).coerceIn(0.75f, 1.5f)
```

### Scaled Tokens
Apply `scaleFactor` to spacing and typography when window size changes:

```kotlin
val scaledBody    = (13 * scaleFactor).sp
val scaledLabel   = (12 * scaleFactor).sp
val scaledCaption = (11 * scaleFactor).sp

val scaledHeightControl  = (32 * scaleFactor).dp
val scaledHeightListItem = (36 * scaleFactor).dp
val scaledSpaceM = (8 * scaleFactor).dp
val scaledSpaceL = (12 * scaleFactor).dp
```

Expose via `CompositionLocal`:
```kotlin
val LocalScale = compositionLocalOf { 1f }

// In App.kt
CompositionLocalProvider(LocalScale provides scaleFactor) {
    App()
}

// In any component
val scale = LocalScale.current
Modifier.height((32 * scale).dp)
```

### Scaling Rules
- **Scale:** font sizes, control heights, spacing, icon sizes, thumb sizes
- **Do not scale:** corner radii (keep `ShapeDefault` fixed at `4.dp`),
  border widths (keep at `1.dp` / `2.dp`), divider widths
- **Relative positions must be preserved** — elements placed side-by-side at baseline
  width must remain side-by-side at all sizes within the `coerceIn` range
- Minimum readable font size: `10.sp` — never scale below this
- At very small window sizes (`< 900px`), consider hiding the list panel and showing
  only the detail panel with a back button

---

## Do / Don't Summary

| ✅ Do | ❌ Don't |
|-------|---------|
| Use color tokens | Hardcode hex values in components |
| Use typography tokens | Hardcode `fontSize` values |
| Use `Modifier.weight()` in rows | Use fixed widths inside rows |
| Use `ShapeDefault` on all interactive elements | Use sharp or pill corners |
| Dark text on light, yellow on dark | Dark text on dark backgrounds |
| Scale fonts and heights with `scaleFactor` | Use fixed `dp` for heights in scaled components |
| Uniform `HeightControl` for all row elements | Mix different heights in one row |
| `TypographyMono` for numeric values | Regular body font for value readouts |

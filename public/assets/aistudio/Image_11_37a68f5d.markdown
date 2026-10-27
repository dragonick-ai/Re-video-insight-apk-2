---
name: ReVideo Minimal Studio
colors:
  surface: '#111317'
  surface-dim: '#111317'
  surface-bright: '#37393e'
  surface-container-lowest: '#0c0e12'
  surface-container-low: '#1a1c20'
  surface-container: '#1e2024'
  surface-container-high: '#282a2e'
  surface-container-highest: '#333539'
  on-surface: '#e2e2e8'
  on-surface-variant: '#c7c4d7'
  inverse-surface: '#e2e2e8'
  inverse-on-surface: '#2f3035'
  outline: '#908fa0'
  outline-variant: '#464554'
  surface-tint: '#c0c1ff'
  primary: '#c0c1ff'
  on-primary: '#1000a9'
  primary-container: '#8083ff'
  on-primary-container: '#0d0096'
  inverse-primary: '#494bd6'
  secondary: '#bdc2ff'
  on-secondary: '#131e8c'
  secondary-container: '#2f3aa3'
  on-secondary-container: '#a8afff'
  tertiary: '#c3c0ff'
  on-tertiary: '#1d00a5'
  tertiary-container: '#8582ff'
  on-tertiary-container: '#180092'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#e1e0ff'
  primary-fixed-dim: '#c0c1ff'
  on-primary-fixed: '#07006c'
  on-primary-fixed-variant: '#2f2ebe'
  secondary-fixed: '#e0e0ff'
  secondary-fixed-dim: '#bdc2ff'
  on-secondary-fixed: '#000767'
  on-secondary-fixed-variant: '#2f3aa3'
  tertiary-fixed: '#e2dfff'
  tertiary-fixed-dim: '#c3c0ff'
  on-tertiary-fixed: '#0f0069'
  on-tertiary-fixed-variant: '#3323cc'
  background: '#111317'
  on-background: '#e2e2e8'
  surface-variant: '#333539'
typography:
  headline-lg:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Inter
    fontSize: 26px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.015em
  headline-md:
    fontFamily: Inter
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: 0em
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
    letterSpacing: 0em
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
    letterSpacing: 0em
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
    letterSpacing: 0.01em
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
    letterSpacing: 0.03em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 0.75rem
  gutter-desktop: 1rem
  margin: 1rem
  margin-tablet: 1.5rem
  margin-desktop: 2rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 0.75rem
  space-lg: 1rem
  space-xl: 1.5rem
---

## Brand & Style
The design system targets creative directors, digital artists, and video professionals operating an AI-driven video-to-video workflow on Android devices. The aesthetic is clean, technical, and unobtrusive—positioning the software as a serious creative utility rather than an experimental toy.

Key characteristics:
- **Quiet Utility:** Elimination of extraneous visual noise, neon halos, and non-functional embellishments. The UI recedes so dynamic video content takes focal priority.
- **Tonal Precision:** Architectural layering of dark matte surfaces with precise 1px hairline separation.
- **Android Native Integrity:** Strict compliance with Android's 48dp minimum touch target boundaries paired with compact, high-density informational grouping.

## Colors
The palette is built around deep, non-emissive charcoal planes complemented by a measured indigo accent.

### Color Tokens & Roles
- **Canvas Base (`#0f1115`):** The primary root surface. A deep, non-reflective neutral charcoal base.
- **Surface Level 1 (`#17191f`):** Containers, studio toolbars, and inactive track regions.
- **Surface Level 2 (`#1f222a`):** Interactive card backgrounds, inspect panels, and dialog surfaces.
- **Surface Level 3 (`#282c37`):** Hover states, input backgrounds, and active selection states.
- **Border / Outline (`#2b2f3a`):** Structural 1px boundary lines ensuring clear spatial segregation without high-contrast visual tension.
- **Primary Indigo (`#6366f1`):** Execution states, timeline playhead, and primary confirmation actions. Strictly flat or micro-shaded; zero ambient luminescence or neon glows.
- **Accent Indigo Light (`#818cf8`):** Text links, focused icon glyphs, and active toggle markers.
- **Accent Indigo Dark (`#4f46e5`):** Pressed button states and persistent process indicators.
- **Text & Foreground:**
  - **High-Emphasis Text (`#f3f4f6`):** Primary labels, titles, and active numeric readouts.
  - **Medium-Emphasis Text (`#9ca3af`):** Secondary metrics, disabled controls, and structural descriptors.
  - **Subtle Text (`#6b7280`):** Timestamps, metadata keys, and placeholder values.

## Typography
Inter delivers neutral, machine-calibrated clarity across compact mobile interfaces.

- **Numerics:** Use tabular numerals (`font-feature-settings: "tnum"`) for timestamps, frame rates, and render progress percentages.
- **Hierarchy:** Rely primarily on font weight (`500` vs `600`) and value tinting (`#f3f4f6` vs `#9ca3af`) rather than drastic font size jumps to preserve layout compactness.
- **Casing:** Uppercase treatment is restricted to `label-sm` badges and video metadata tags (e.g., `FPS`, `4K`, `PROMPT`).

## Layout & Spacing
The layout employs an 8dp/4dp strict baseline grid engineered for viewport-constrained video editing workspaces.

- **Mobile Viewport (< 600dp):** Fluid vertical stack. Upper 40-50% dedicated to the video preview canvas; lower half reserved for parameter modules, keyframe timelines, and prompt controls. Standard 16dp outer screen margin.
- **Foldable & Tablet Viewport (600dp - 1024dp):** Two-column split layout. Left side hosts persistent preview controls and timeline; right side hosts generation parameters, seed variations, and style cards.
- **Touch Accommodation:** All primary touch controls maintain visual compact boundaries (32-36dp) padded transparently to 48dp target bounding boxes to comply with Android accessibility requirements.

## Elevation & Depth
Depth is produced through tonal containment and subtle 1px border lines rather than diffuse drop shadows.

- **Ground Level (Canvas):** Pure `#0f1115`.
- **Level 1 (Dock & Timelines):** `#17191f` with a `1px solid #2b2f3a` top boundary.
- **Level 2 (Cards & Prompt Boxes):** `#1f222a` with a uniform `1px solid #2b2f3a` perimeter.
- **Level 3 (Modals, Overlays & Menus):** `#282c37` with an ambient, razor-sharp shadow: `0px 8px 24px rgba(0, 0, 0, 0.45)` and a `1px solid #3d4251` structural outline.
- **Absence of Glows:** Glowing drop-shadows around active indigo buttons or controls are strictly omitted.

## Shapes
A clean, structural radius system balances modern touch targets with technical precision.

- **Base Radius (0.5rem / 8px):** Applied to standard buttons, input fields, control chips, and parameter steppers.
- **Large Radius (1rem / 16px):** Applied to generation cards, preview containers, and modal dialog sheets.
- **Full Radius (Pill):** Reserved exclusively for generation status badges (e.g., `Rendering`, `Queued`) and timeline scrub handles.

## Components

### Buttons
- **Primary:** Background `#6366f1`, text `#ffffff`, height 40dp (within 48dp touch container), radius 8px, font `label-lg`. Pressed: `#4f46e5`. Disabled: `#1f222a` background, text `#6b7280`, border `1px solid #2b2f3a`.
- **Secondary / Ghost:** Background transparent, text `#f3f4f6`, border `1px solid #2b2f3a`. Active: `#1f222a`.

### Chips & Badges
- **Status Badges:** Height 22dp, radius 9999px, padding 0 8dp, font `label-sm`.
  - Processing: Background `#1f222a`, border `1px solid #6366f1`, text `#818cf8`.
  - Neutral / Spec: Background `#17191f`, border `1px solid #2b2f3a`, text `#9ca3af`.
- **Filter & Preset Chips:** Height 32dp, radius 8px, padding 0 12dp. Default: `#17191f` background, border `1px solid #2b2f3a`. Selected: `#1f222a` background, border `1px solid #6366f1`, text `#ffffff`.

### Inputs & Text Areas
- **Prompt Field:** Background `#17191f`, border `1px solid #2b2f3a`, radius 8px, padding 12dp, text `body-md` (`#f3f4f6`). Focused: Border `#6366f1`, outline none.
- **Numeric Steppers:** Fixed height 36dp, background `#1f222a`, border `1px solid #2b2f3a`, tabular figures centered.

### Selection Controls
- **Checkboxes & Radios:** 20dp box with 8dp touch target padding. Border 1.5px `#2b2f3a`, radius 4px (checkbox) or circle (radio). Active state fills `#6366f1` with an interior white checkmark/dot.
- **Switch:** Track 44dp x 24dp, background `#2b2f3a`, radius 12dp. Thumb 18dp `#f3f4f6`. Checked track: `#6366f1`.

### Video Generation Cards
- Background `#1f222a`, border `1px solid #2b2f3a`, radius 16px, overflow hidden.
- Media aspect-ratio container (16:9 or 9:16) with inner gradient overlay (`rgba(15, 17, 21, 0.7)` to `transparent`) anchored to the bottom edge for metadata readouts.

### Timeline Scrub Bar
- Height 4dp, background `#2b2f3a`, radius 2px. Filled progress `#6366f1`. Scrub handle: 16dp circular node (`#f3f4f6`) centered on playhead line with an invisible 48dp vertical touch strip.
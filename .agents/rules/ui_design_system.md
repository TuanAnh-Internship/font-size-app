# UI Design System & Navigation Architecture Rules
Project: Font Size Controller (FontMaster)

## 1. Color Palette & Visual Identity (Extracted from Figma Design)
- **Primary Brand / Action Button:** `#1E1B4B` (Deep Navy / Midnight Indigo - `Color(0xFF1E1B4B)`)
- **Accent Purple / Highlighting:** `#7C3AED` / `#6366F1` (Vibrant Royal Purple - `Color(0xFF7C3AED)`)
- **Background:** `#F8FAFC` (Ultra-clean light off-white - `Color(0xFFF8FAFC)`)
- **Surface / Card Background:** `#FFFFFF` (Pure white cards with subtle border & elevation)
- **Badge / Icon Background Tint:** `#EEF2FF` / `#F3F0FF` (Light lavender/indigo soft tint)
- **Success Mint / Green:** `#00C48C` / `#10B981` (Vibrant Emerald Checkmark)
- **Border / Outline Color:** `#E2E8F0` / `#CBD5E1` (Clean subtle border)
- **Text Primary:** `#0F172A` / `#1E1B4B` (High-contrast dark slate)
- **Text Secondary / Subtitle:** `#64748B` (Medium slate gray)

## 2. Typography & Component Geometry
- **Corner Radius:**
  - Cards: `16.dp` to `20.dp`
  - Primary Buttons: `16.dp` to `24.dp`
  - Icon Badges: `12.dp` to `14.dp` rounded square
  - Language / Pill buttons: `50.dp` (Full capsule pill)
- **Buttons:**
  - Primary: Deep Navy background (`#1E1B4B`), White bold text, with trailing arrow icon if applicable. Height: `52.dp` - `56.dp`.
  - Secondary: White background, subtle border (`#E2E8F0`), dark navy text. Height: `48.dp` - `52.dp`.
- **Top App Bar Standards:**
  - Title: Left-aligned bold Navy text (`18.sp` - `20.sp`).
  - Leading: Back arrow button `<-` with clean ripple.
  - Actions: Dark mode toggle circular icon button + Pill-shaped "🌐 VN | EN" toggle.

## 3. Screen Navigation Graph & Routing
1. **OnboardingScreen:**
   - Action: Click "Bắt Đầu Ngay" ➔ Navigates to `FontSizeScreen` (saves `hasSeenOnboarding = true`).
2. **FontSizeScreen (Main Hub):**
   - Action "Áp dụng":
     - If `canWrite == false` ➔ Opens `PermissionScreen` (or Dialog).
     - If `applySuccess` ➔ Navigates to `ResultScreen`.
   - Action "Khám phá Trợ Năng / Kiểu chữ" ➔ Navigates to secondary feature screens.
3. **PermissionScreen:**
   - Action "Mở Cài Đặt Hệ Thống" ➔ Fires Intent `Settings.ACTION_MANAGE_WRITE_SETTINGS`.
   - Action "Để sau / Dùng thử trong app" ➔ Returns to `FontSizeScreen`.
4. **ResultScreen:**
   - Action "Quay về trang chủ" ➔ Returns to `FontSizeScreen`.
   - Action "Thử kích cỡ khác" ➔ Returns to `FontSizeScreen` with selection enabled.

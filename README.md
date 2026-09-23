# Font Size Controller — Android Utility & Accessibility App

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-blue.svg)](https://developer.android.com/jetpack/compose)
[![MinSdk](https://img.shields.io/badge/minSdk-29%20(Android%2010)-orange.svg)](https://developer.android.com/about/versions/10)
[![TargetSdk](https://img.shields.io/badge/targetSdk-34%2F36%20(Android%2014%2B)-red.svg)](https://developer.android.com/about/versions/14)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20MVVM-success.svg)](#-kiến-trúc-phần-mềm-architecture)

> **Dự án thực tập 1 (Project 1):** Ứng dụng hỗ trợ người dùng và người cao tuổi điều chỉnh kích thước cỡ chữ, kiểm soát kiểu phông chữ và tối ưu hóa khả năng hiển thị trên toàn bộ hệ điều hành Android.

---

## 📱 Giới thiệu tổng quan (Overview)

Trên hệ điều hành Android, việc thay đổi cỡ chữ hệ thống thường nằm sâu trong nhiều tầng menu cài đặt phức tạp, không có màn hình xem trước trực quan và gây khó khăn cho người lớn tuổi hoặc người mắt kém.

**Font Size Controller** giải quyết triệt để vấn đề này bằng cách cung cấp một giao diện thân thiện, chuẩn **Material Design 3**:
* Đọc và hiển thị chính xác kích thước font hiện tại của thiết bị.
* Cung cấp các mức thiết lập sẵn dễ hiểu (**Nhỏ, Mặc định, Lớn, Rất lớn**) kèm cơ chế hiển thị mức lẻ (**Custom**).
* **Xem trước trực quan (Live Preview)** theo thời gian thực trước khi quyết định thay đổi.
* Áp dụng thay đổi cỡ chữ cho **TOÀN BỘ HỆ ĐIỀU HÀNH ANDROID** (tin nhắn SMS, Zalo, Facebook, màn hình chính và tất cả ứng dụng ngoài) thông qua quyền hệ thống an toàn `WRITE_SETTINGS`.
* Tương thích hoàn hảo với thuật toán **Non-linear Font Scaling 200%** trên Android 14+.

---

## ✨ Tính năng nổi bật (Key Features)

### 1. Tính năng cốt lõi (Core Features - P0)
- **Đọc kích thước chữ hệ thống (F01):** Đọc persistent từ `Settings.System.FONT_SCALE` qua `ContentResolver` và đồng bộ với `Configuration.fontScale`.
- **Lựa chọn Preset trực quan (F02):** 4 mức chuẩn AOSP: **Nhỏ (0.85x)**, **Mặc định (1.00x)**, **Lớn (1.15x)**, **Rất lớn (1.30x)**. Tự động nhận diện mức lẻ của hãng (ví dụ `Custom 1.10x` trên Samsung).
- **Xem trước độc lập (F03):** Khung xem trước mượt mà theo đơn vị `.sp`, không làm thay đổi hệ thống cho đến khi người dùng bấm xác nhận.
- **Kiểm soát quyền chặt chẽ (F04 - F05):** Kiểm tra `Settings.System.canWrite()` tại thời điểm thực thi; điều hướng thông minh đến `ACTION_MANAGE_WRITE_SETTINGS` với khối try-catch an toàn.
- **Tự động làm mới trong vòng đời (`onResume`):** Sử dụng `LifecycleEventObserver` để tự động kiểm tra lại quyền ngay khi người dùng từ Cài đặt máy quay về app.
- **Ghi và xác minh bắt buộc (F06):** Sau khi ghi qua `Settings.System.putFloat()`, app **bắt buộc đọc lại giá trị để Verify** rồi mới thông báo thành công (tránh báo thành công giả trên các máy bị OEM chặn).

### 2. Trải nghiệm người dùng & Nâng cao (Enhancements - P1 & P2)
- **Màn hình Giới thiệu (Onboarding Welcome Screen):** Giới thiệu trực quan giá trị ứng dụng với nút "Bắt đầu ngay" dẫn vào màn hình chính.
- **Hỗ trợ Đa ngôn ngữ toàn ứng dụng (App-wide Localization):** Tích hợp nút chuyển đổi **`🇻🇳 VN | EN`** ngay trên Top App Bar ở tất cả các màn hình; cho phép chuyển đổi tức thì 100% nội dung (tiêu đề, thẻ thiết lập, preview, dialog, thông báo và nút bấm) giữa Tiếng Việt và Tiếng Anh trên toàn bộ ứng dụng thông qua Android String Resources (`values/` và `values-en/`).
- **Thanh trượt mốc thông minh trên Bảng thông báo (Notification Slider Track):** Ghim thanh điều khiển trực quan khi vuốt từ mép trên màn hình xuống; gồm vạch tiến trình Gradient đo chuẩn %, 6 mốc chạm đổi kích cỡ ngay (0.85x, 1.00x, 1.15x, 1.30x, 1.60x, 2.00x) với hiệu ứng phát sáng mốc active, và 2 nút tăng/giảm vi mô mà không cần mở app.
- **Hồ sơ cỡ chữ gia đình (Family Profiles):** Tích hợp 3 cấu hình chuẩn (*Cá nhân 1.00x*, *Bố mẹ 1.15x*, *Ông bà 1.30x*) dạng Accordion Dropdown thu gọn/mở rộng mượt mà.
- **Khôi phục mặc định an toàn (Reset Default 1.00x):** Nút khôi phục cỡ chữ chuẩn AOSP kèm hộp thoại xác nhận an toàn.
- **Thư viện kiểu phông chữ tinh gọn (Single-Card Dropdown Gallery):** Thiết kế tối giản quy về 1 Card duy nhất, bộ chọn Dropdown tiện lợi, xem trước văn bản mẫu trực tiếp và áp dụng tức thì.
- **Bài kiểm tra thị lực thông minh (Smart Reading Eye-Test):** Đo độ mỏi mắt khi đọc và đưa ra gợi ý kích thước font tối ưu.
- **Đặc quyền & Phạm vi tác động:**
  * **Cỡ chữ (Font Scale):** Tác động **TOÀN BỘ HỆ ĐIỀU HÀNH** (SMS, mạng xã hội, màn hình chính, app ngoài) thông qua `Settings.System.FONT_SCALE`.
  * **Kiểu phông chữ (Font Typeface):** Tác động **TOÀN BỘ ỨNG DỤNG** (Tuân thủ mô hình bảo mật của Android OS và bản quyền chữ ký Samsung FlipFont).

---

## 🏗️ Kiến trúc phần mềm (Architecture)

Ứng dụng tuân thủ nghiêm ngặt mô hình **Clean Architecture** kết hợp **MVVM (Model - View - ViewModel)** và nguyên lý luồng dữ liệu một chiều (**Unidirectional Data Flow - UDF**):

```
┌─────────────────────────────────────────────────────────────┐
│ 1. UI Layer (Jetpack Compose)                               │
│    FontSizeScreen, OnboardingScreen, PocScreen, Components  │
└──────────────────────────────┬──────────────────────────────┘
                               │ StateFlow<FontSizeUiState> (Collect)
                               ▼ Events / Actions (Dispatch)
┌─────────────────────────────────────────────────────────────┐
│ 2. ViewModel Layer                                          │
│    FontSizeViewModel (Coroutines, State Management)         │
└──────────────────────────────┬──────────────────────────────┘
                               │ Domain Operations
                               ▼ FontScaleResult
┌─────────────────────────────────────────────────────────────┐
│ 3. Repository Layer                                         │
│    FontSettingsRepository, FontScaleMapper                  │
└──────────────────────────────┬──────────────────────────────┘
                               │ IPC / System Calls
                               ▼ ContentResolver / Settings
┌─────────────────────────────────────────────────────────────┐
│ 4. Android System API Layer                                 │
│    Settings.System.FONT_SCALE, canWrite(), Configuration    │
└─────────────────────────────────────────────────────────────┘
```

### Nguyên tắc phân tầng (Layer Boundaries):
* **UI Layer:** Không bao giờ gọi trực tiếp `Settings.System`. Chỉ render giao diện từ `UiState` và gửi Event lên ViewModel.
* **ViewModel Layer:** Không lưu tham chiếu `Activity`/`Context` tránh leak memory. Quản lý `StateFlow<FontSizeUiState>` bất biến.
* **Repository Layer:** Đóng vai trò Single Source of Truth cho dữ liệu cỡ chữ, bọc mọi ngoại lệ hệ thống trong `Result<T>`.

---

## 🎨 Bản thiết kế Figma (UI/UX Design)

Toàn bộ hệ thống giao diện 10 màn hình được thiết kế theo chuẩn **Material Design 3** phong cách **Clean Light Mode** (Nền trắng ngà `#F8FAFC`, Thẻ trắng `#FFFFFF`, Điểm nhấn Royal Blue `#2563EB`, Chữ đen than `#0F172A`):

* 🔗 **Figma Prototype Link:** [Font Size Controller Figma Prototype](https://www.figma.com/design/SIXUIUjDzjYbuPCUjHVOXM/Untitled?node-id=0-1&p=f&t=uBkQlDMUGb0OePXK-0)
* **Quy mô:** 10 màn hình hoàn chỉnh có liên kết Prototype tương tác:
  1. `Onboarding Screen` (Màn hình Giới thiệu)
  2. `Main Font Controller Screen (Vietnamese)` (Màn hình chính tiếng Việt)
  3. `Main Font Controller Screen (English)` (Màn hình chính tiếng Anh)
  4. `Permission Required Modal` (Hộp thoại xin quyền)
  5. `Success & Verification Screen` (Màn hình báo thành công)
  6. `Error & Unsupported OEM Screen` (Màn hình báo lỗi & hướng dẫn thủ công)
  7. `Stress Test 200% Font Scale` (Kiểm thử font chữ 200%)
  8. `Quick Settings Tile Widget` (Phím tắt thanh thông báo)
  9. `Font Style Gallery` (Thư viện phông chữ đẹp)
  10. `Smart Reading Eye-Test` (Bài test kiểm tra thị lực)

---

## 🧪 Kết quả kiểm thử thực nghiệm trên thiết bị thật (POC Results)

Ứng dụng đã được cài đặt và kiểm chứng thực nghiệm (POC) trực tiếp trên máy thật qua kết nối Wireless ADB:

* **Thiết bị:** Samsung Galaxy A11 (SM-A115F), Android 12 (API 31), OneUI Core 4.1.
* **Giá trị đo được:**
  * `Settings.System.FONT_SCALE`: **`1.1`**
  * `Configuration.fontScale`: **`1.1`**
  * So sánh: **Trùng khớp 100% (Same)**.
* **Quyền `WRITE_SETTINGS`:** Mở đúng trang cấp quyền riêng của ứng dụng và hoạt động ổn định.
* **Bằng chứng Logcat trích xuất:**
  ```text
  D FontSizePOC: Lifecycle ON_RESUME: Re-check canWrite và FontScale
  D FontSizePOC: Data refreshed -> System: 1.1, Config: 1.1, canWrite: true
  ```

---

## 📂 Cấu trúc thư mục mã nguồn (Project Structure)

```
font-size-app/
├── docs/                                # Tài liệu kỹ thuật dự án
│   ├── requirements.md                  # Yêu cầu sản phẩm & so sánh quy mô
│   ├── technical_requirements.md        # Đặc tả kỹ thuật chi tiết & Acceptance Criteria (AC01-AC08)
│   ├── architecture.md                  # Tài liệu thiết kế kiến trúc MVVM
│   ├── ui_design.md                     # Tài liệu thiết kế giao diện, Figma link & User Flows
│   ├── poc_results.md                   # Báo cáo kết quả kiểm thử thực nghiệm trên máy thật
│   └── diagram/                         # Các sơ đồ kỹ thuật (Draw.io & Mermaid)
│
└── source/                              # Mã nguồn ứng dụng Android
    ├── deploy.ps1                       # Script tự động build, install và test qua ADB
    └── app/src/main/
        ├── AndroidManifest.xml          # Khai báo quyền WRITE_SETTINGS
        └── java/com/example/fontsizecontroller/
            ├── MainActivity.kt          # Activity chính tích hợp Edge-to-Edge
            ├── model/                   # Data models, Sealed Results & UI State
            │   ├── FontSizeOption.kt
            │   ├── FontSizeUiState.kt
            │   └── FontScaleResult.kt
            ├── repository/              # Repository truy cập Android System Settings
            │   └── FontSettingsRepository.kt
            ├── viewmodel/               # ViewModel điều phối StateFlow & User Actions
            │   └── FontSizeViewModel.kt
            ├── ui/
            │   ├── screen/              # Các màn hình chính (FontSizeScreen, PocScreen)
            │   ├── component/           # Các Card, Button, Dialog dùng chung
            │   └── theme/               # MaterialTheme, Colors, Typography (.sp)
            └── util/                    # Tiện ích ánh xạ và tính toán
                └── FontScaleMapper.kt
```

---

## 🚀 Hướng dẫn cài đặt & Chạy ứng dụng (Getting Started)

### 1. Yêu cầu môi trường
* **Android Studio:** Ladybug (hoặc mới hơn) / VS Code có Android Extension Pack.
* **JDK:** OpenJDK 17 trở lên.
* **Android SDK:** Compile SDK 37, Min SDK 29 (Android 10+).
* **Thiết bị kiểm thử:** Điện thoại Android thật (bật USB/Wireless Debugging) hoặc Android Emulator.

### 2. Hướng dẫn chạy & Kiểm thử (Testing & Build)
* **Chạy kiểm thử tự động (Unit Tests):**
  ```powershell
  cd font-size-app\source
  ./gradlew testDebugUnitTest
  ```
  *(Bộ test tự động `FontScaleMapperTest` bao phủ toàn bộ Test Matrix: 0.85x, 1.00x, 1.15x, 1.30x và các trường hợp lẻ dung sai 0.03f).*

* **Biên dịch xuất file APK (Debug Build):**
  ```powershell
  ./gradlew assembleDebug
  ```
  File APK sẵn sàng để chia sẻ cài đặt trực tiếp lên các máy Android khác tại:
  `font-size-app/source/app/build/outputs/apk/debug/app-debug.apk`

* **Cài đặt nhanh lên thiết bị đang kết nối ADB:**
  ```powershell
  adb install -r app/build/outputs/apk/debug/app-debug.apk
  adb shell am start -n com.example.fontsizecontroller/.MainActivity
  ```
---

## 📖 Tài liệu tham khảo kỹ thuật
* [Android Developers — Configuration.fontScale](https://developer.android.com/reference/android/content/res/Configuration#fontScale)
* [Android Developers — Settings.System.FONT_SCALE](https://developer.android.com/reference/android/provider/Settings.System#FONT_SCALE)
* [Android Developers — ACTION_MANAGE_WRITE_SETTINGS](https://developer.android.com/reference/android/provider/Settings#ACTION_MANAGE_WRITE_SETTINGS)
* [Android 14 Features — Non-linear font scaling](https://developer.android.com/about/versions/14/features#non-linear-font-scaling)
* [Material Design 3 Typography Guidelines](https://m3.material.io/styles/typography/overview)

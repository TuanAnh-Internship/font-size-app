# Font Size Controller — Android Utility & Accessibility App

[![Release](https://img.shields.io/github/v/release/TuanAnh-Internship/font-size-app?style=for-the-badge&logo=github&color=2ea44f&label=Release)](https://github.com/TuanAnh-Internship/font-size-app/releases/latest)
[![Download APK](https://img.shields.io/badge/Download_APK-FontM_v1.0.0-success?style=for-the-badge&logo=android&logoColor=white&color=3DDC84)](https://github.com/TuanAnh-Internship/font-size-app/releases/download/v1.0.0/FontM-v1.0.0-release.apk)
[![Android CI](https://github.com/TuanAnh-Internship/font-size-app/actions/workflows/android.yml/badge.svg)](https://github.com/TuanAnh-Internship/font-size-app/actions/workflows/android.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
<br>

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
- **Tiện ích điều khiển nhanh trên Bảng thông báo (Notification Quick Control):** Tích hợp điều khiển trực quan ngay trên khay thông báo hệ thống; gồm 4 mốc chọn nhanh thiết yếu (**0.85x**, **1.00x**, **1.25x**, **1.50x**) kèm 2 nút bấm tăng/giảm bước nhảy vi mô (**[-] Giảm**, **[+] Tăng**) giúp điều chỉnh cỡ chữ máy tức thì mà không cần mở lại app. Tự động đồng bộ hai chiều thời gian thực giữa Notification và ứng dụng.
- **Hồ sơ cỡ chữ gia đình (Family Profiles):** Tích hợp 3 cấu hình chuẩn (*Cá nhân 1.00x*, *Bố mẹ 1.15x*, *Ông bà 1.30x*) dạng Accordion Dropdown thu gọn/mở rộng mượt mà.
- **Khôi phục mặc định an toàn (Reset Default 1.00x):** Nút khôi phục cỡ chữ chuẩn AOSP kèm hộp thoại xác nhận an toàn.
- **Tùy biến phông chữ ứng dụng (App Font Style in Settings):** Tích hợp bộ chọn phông chữ giao diện trực tiếp trong tab Cài Đặt & Trợ Năng với các font chữ tuyển chọn (*Roboto*, *Samsung One*, *Noto Serif*, *Dancing Script*, *Droid Sans Mono*), xem trước trực quan và áp dụng tức thì cho ứng dụng; đồng thời cung cấp nút tắt điều hướng nhanh đến Cài đặt phông chữ toàn hệ thống của thiết bị.
- **Bài kiểm tra thị lực thông minh (Smart Reading Eye-Test):** Đo độ mỏi mắt khi đọc qua bài test 3 câu hỏi và đưa ra gợi ý kích thước font tối ưu.
- **Thanh điều hướng tinh gọn 3 Tab (Clean 3-Tab Bottom Navigation):** Gồm **Cỡ Chữ** (Màn hình chính điều chỉnh font scale), **Đo Mắt** (Kiểm tra thị lực), và **Trợ Năng** (Chế độ đọc, hồ sơ gia đình & cài đặt phông chữ ứng dụng).
- **Đặc quyền & Phạm vi tác động:**
  * **Cỡ chữ (Font Scale):** Tác động **TOÀN BỘ HỆ ĐIỀU HÀNH** (SMS, mạng xã hội, màn hình chính, app ngoài) thông qua `Settings.System.FONT_SCALE`.
  * **Kiểu phông chữ (App Font Style):** Tác động **TOÀN BỘ GIAO DIỆN ỨNG DỤNG** (Tuân thủ mô hình bảo mật sandboxing của Android OS và bản quyền chữ ký Samsung FlipFont).

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
│   ├── USER_MANUAL.md                   # Sổ tay hướng dẫn sử dụng chi tiết & xử lý lỗi
│   └── diagram/                         # Các sơ đồ kỹ thuật (Draw.io & Mermaid)
│
└── source/                              # Mã nguồn ứng dụng Android
    ├── deploy.ps1                       # Script tự động build, install và test qua ADB
    └── app/src/main/
        ├── AndroidManifest.xml          # Khai báo quyền WRITE_SETTINGS, POST_NOTIFICATIONS
        ├── java/com/example/fontsizecontroller/
        │   ├── MainActivity.kt          # Activity chính tích hợp Edge-to-Edge
        │   ├── model/                   # Data models, Sealed Results & UI State
        │   ├── repository/              # Repository truy cập System Settings & DataStore
        │   ├── service/                 # QuickControlNotificationManager & NotificationReceiver
        │   ├── viewmodel/               # ViewModel điều phối StateFlow & User Actions
        │   ├── ui/
        │   │   ├── screen/              # Các màn hình (FontSizeScreen, EyeTestScreen, SettingsAndHelpScreen...)
        │   │   ├── component/           # Các Card, Button, Dialog, BottomBar dùng chung
        │   │   └── theme/               # MaterialTheme, Colors, Typography (hỗ trợ font động)
        │   └── util/                    # Tiện ích ánh xạ và tính toán
        └── res/
            ├── layout/                  # Custom RemoteViews layout cho thanh thông báo
            └── drawable/                # Icons & vector assets (ic_notification_font...)
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

# Font Size Controller (FontM) — Android Utility & Accessibility App

[![Release](https://img.shields.io/badge/Release-v1.1.0_Latest-2ea44f?style=for-the-badge&logo=github&logoColor=white)](https://github.com/TuanAnh-Internship/font-size-app/releases/tag/v1.1.0)
[![Download APK](https://img.shields.io/badge/Download_APK-FontM_v1.1.0-success?style=for-the-badge&logo=android&logoColor=white&color=3DDC84)](https://github.com/TuanAnh-Internship/font-size-app/releases/download/v1.1.0/FontM-v1.1.0-release.apk)
[![Direct APK Download](https://img.shields.io/badge/Direct_APK-Download-blue?style=for-the-badge&logo=googlechrome&logoColor=white)](https://github.com/TuanAnh-Internship/font-size-app/releases/download/v1.1.0/FontM-v1.1.0-release.apk)
[![Unit Tests](https://img.shields.io/badge/Tests-36%2F36_Passed-brightgreen?style=for-the-badge&logo=junit5)](source/app/src/test/)
<br>

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-blue.svg)](https://developer.android.com/jetpack/compose)
[![MinSdk](https://img.shields.io/badge/minSdk-26%20(Android%208.0)-orange.svg)](https://developer.android.com/about/versions/oreo)
[![TargetSdk](https://img.shields.io/badge/targetSdk-36%20(Android%2016%2B)-red.svg)](https://developer.android.com/about/versions/16)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20MVVM-success.svg)](#-kiến-trúc-phần-mềm-architecture)

> **Dự án thực tập 1 (Project 1):** Ứng dụng hỗ trợ người dùng và người cao tuổi điều chỉnh kích thước cỡ chữ trên toàn bộ hệ điều hành Android, đo thị lực thông minh, kính lúp soi chữ ngoài đời thực và hẹn giờ tăng cỡ chữ ban đêm.

---

## 📱 Giới thiệu tổng quan (Overview)

Trên hệ điều hành Android, việc thay đổi cỡ chữ hệ thống thường nằm sâu trong nhiều tầng menu cài đặt phức tạp, không có màn hình xem trước trực quan và gây khó khăn cho người lớn tuổi hoặc người mắt kém.

**Font Size Controller (FontM)** giải quyết triệt để vấn đề này bằng cách cung cấp một giao diện thân thiện, chuẩn **Material Design 3**:
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

### 2. Trợ năng & Nâng cao (Accessibility & Smart Diagnostics - P1 & P2)
- **Đo mắt & Gợi ý cỡ chữ thông minh (Smart Vision Diagnostic):** Trắc nghiệm thị lực 3 bước với động cơ chẩn đoán thị giác (`EyeTestDiagnosticEngine`), hiển thị khuyến nghị và nút 1 chạm áp dụng ngay. Màn hình bài đo luôn cố định ở cỡ 1.00x chuẩn.
- **Hẹn giờ cỡ chữ ban đêm (Scheduled Font Scale):** Tự động chuyển cỡ chữ sang mức thư giãn (`1.25x` – `1.35x`) từ 20:00 tối và tự động khôi phục về `1.00x` vào 07:00 sáng hôm sau thông qua `AlarmManager` / `BroadcastReceiver`.
- **Kính lúp đọc nhanh qua Camera (Quick Camera Loupe Magnifier):** Tích hợp CameraX thu phóng 2x – 5x, trợ sáng đèn Flash, bộ lọc tương phản và nút đóng băng khung hình (Pause Frame) giúp người cao tuổi đọc nhãn thuốc, hóa đơn không bị run tay.
- **Phản hồi xúc giác vật lý (Haptic Feedback):** Tích hợp rung phản hồi nhẹ nhàng khi bấm nút, trượt slider hoặc chạm mốc thông báo theo chuẩn WCAG 2.2 AAA.
- **Quick Settings Tile:** Ô phím tắt trên thanh trạng thái cạnh Wi-Fi / Bluetooth giúp xoay vòng 3 mốc font nhanh (`1.00x` ➔ `1.25x` ➔ `1.50x`) chỉ với 1 chạm.
- **Hỗ trợ Đa ngôn ngữ toàn ứng dụng:** Chuyển đổi tức thì giữa Tiếng Việt và Tiếng Anh (`🇻🇳 VI | 🇬🇧 EN`) mà không cần khởi động lại ứng dụng.
- **Tương thích chuyên sâu OEM:** Tự động phát hiện Samsung OneUI, Xiaomi HyperOS, ColorOS và cung cấp hướng dẫn tắt tối ưu hóa pin ngầm (Battery Optimization Whitelist).

---

## 🏗️ Kiến trúc phần mềm (Architecture)

Ứng dụng tuân thủ nghiêm ngặt mô hình **Clean Architecture** kết hợp **MVVM (Model - View - ViewModel)** và nguyên lý luồng dữ liệu một chiều (**Unidirectional Data Flow - UDF**):

```
┌─────────────────────────────────────────────────────────────┐
│ 1. UI Layer (Jetpack Compose)                               │
│    FontSizeScreen, EyeTestScreen, AccessibilityScreen,      │
│    NightScheduleCard, CameraLoupeSection, OemDiagnostics    │
└──────────────────────────────┬──────────────────────────────┘
                               │ StateFlow<FontSizeUiState> (Collect)
                               ▼ Events / Actions (Dispatch)
┌─────────────────────────────────────────────────────────────┐
│ 2. ViewModel & Domain Layer                                 │
│    FontSizeViewModel (Coroutines, StateFlow),               │
│    EyeTestDiagnosticEngine (Pure Kotlin Logic)              │
└──────────────────────────────┬──────────────────────────────┘
                               │ Domain Operations
                               ▼ FontScaleResult
┌─────────────────────────────────────────────────────────────┐
│ 3. Repository Layer                                         │
│    FontSettingsRepository, UserPreferencesRepository        │
└──────────────────────────────┬──────────────────────────────┘
                               │ IPC / System Calls
                               ▼ ContentResolver / Settings / AlarmManager
┌─────────────────────────────────────────────────────────────┐
│ 4. Android System API Layer                                 │
│    Settings.System.FONT_SCALE, canWrite(), CameraX, Tile    │
└─────────────────────────────────────────────────────────────┘
```

---

## 🧪 Kiểm thử tự động & Thiết bị thật (Test Results)

1. **Kiểm thử tự động (Unit Tests):**
   - 36/36 tests đạt tỉ lệ 100% PASS:
     * `EyeTestDiagnosticEngineTest` (6 tests)
     * `FontSizeViewModelTest` (10 tests)
     * `FontScheduleManagerTest` (3 tests)
     * `FontScaleMapperTest` (12 tests)
     * `FontScaleApplyResultTest` (2 tests)
     * `OemCompatibilityHelperTest` (2 tests)
     * `ExampleUnitTest` (1 test)
2. **Kiểm thử thiết bị thật (Samsung Galaxy A11):**
   - Stress test 50 lần đổi font liên tục: 0 crash, 0 ANR.
   - Stress test xoay màn hình 20 chu kỳ: PID duy trì ổn định, StateFlow bảo toàn dữ liệu.
   - RAM PSS: Duy trì ổn định từ **8.2 MB đến 13.1 MB** (đạt xuất sắc tiêu chuẩn < 40 MB).

---

## 🚀 Hướng dẫn biên dịch & Cài đặt (Build & Install)

* **Chạy kiểm thử đơn vị:**
  ```powershell
  cd font-size-app\source
  .\gradlew.bat testDebugUnitTest
  ```

* **Biên dịch bản Release tối ưu R8/ProGuard:**
  ```powershell
  cd font-size-app\source
  .\gradlew.bat assembleRelease
  ```
  File APK phát hành chính thức:
  `source/app/build/outputs/apk/release/FontM-v1.1.0-release.apk` (Dung lượng: ~4.16 MB).

* **Cài đặt trực tiếp lên thiết bị Android:**
  ```powershell
  adb install -r "source/app/build/outputs/apk/release/FontM-v1.1.0-release.apk"
  adb shell am start -n com.example.fontsizecontroller/.MainActivity
  ```

---

## 📖 Tài liệu liên quan
* [Sổ tay người dùng chi tiết (USER_MANUAL.md)](docs/USER_MANUAL.md)
* [Tài liệu Kiến trúc hệ thống (architecture.md)](docs/architecture.md)
* [Báo cáo Kiểm thử thực nghiệm (poc_results.md)](docs/poc_results.md)

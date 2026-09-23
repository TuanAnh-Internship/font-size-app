# Clean Architecture & Jetpack Compose Standards

## 1. Kiến trúc phân tầng (Clean Architecture & UDF)
Mã nguồn dự án FontM tuân thủ nghiêm ngặt mô hình 4 tầng với luồng dữ liệu một chiều (Unidirectional Data Flow):
1. **UI Layer (Jetpack Compose):**
   - Không gọi trực tiếp `Settings.System` hay `ContentResolver`.
   - Chỉ nhận trạng thái bất biến từ `FontSizeUiState` thông qua `collectAsState()`.
   - Phát sự kiện (Events/Actions) lên `FontSizeViewModel`.
   - Các màn hình chính được điều hướng thông qua `ScreenDestination` và thanh điều hướng đáy `AppBottomNavigationBar`.
2. **ViewModel Layer (`FontSizeViewModel`):**
   - Kế thừa `AndroidViewModel` khi cần `applicationContext` cho Repository.
   - Quản lý trạng thái thông qua `StateFlow<FontSizeUiState>`.
   - Tuyệt đối không giữ tham chiếu `Activity` để tránh memory leak.
3. **Repository Layer (`FontSettingsRepository`, `UserPreferencesRepository`):**
   - Đóng vai trò Single Source of Truth cho dữ liệu cỡ chữ và cấu hình người dùng.
   - Bọc toàn bộ các thao tác hệ thống trong khối `Result<T>` an toàn.
4. **System API Layer:**
   - Tương tác với `Settings.System.FONT_SCALE` và `Configuration.fontScale`.

## 2. Tiêu chuẩn Jetpack Compose & Material 3
- Sử dụng **Material Design 3 (`androidx.compose.material3.*`)**.
- Kích thước font chữ trong giao diện bắt buộc dùng đơn vị `.sp` để tôn trọng tỷ lệ hiển thị của hệ thống.
- Các khoảng cách (Padding / Margin) tuân thủ hệ lưới 4dp / 8dp / 12dp / 16dp / 20dp.
- Bán kính bo góc (Corner Radius):
  - Card chính: `16.dp` - `20.dp`.
  - Nút bấm chính: `14.dp` - `16.dp`.
  - Badge / Pill: `50.dp` (Capsule shape).

## 3. Bản địa hóa & Chuỗi ngôn ngữ tập trung (i18n Localization)
- Không hardcode chuỗi ký tự hiển thị trực tiếp trong các hàm `@Composable`.
- Mọi chuỗi văn bản được quản lý qua bộ từ điển tập trung `AppStrings.kt`, hỗ trợ chuyển đổi tức thì giữa Tiếng Việt (`AppLanguage.VI`) và Tiếng Anh (`AppLanguage.EN`) mà không cần khởi động lại ứng dụng.

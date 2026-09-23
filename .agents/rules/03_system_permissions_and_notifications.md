# System Permissions & Notification Quick Controls Standards

## 1. Kiểm soát quyền hệ thống & Vòng đời (System Permissions)
- **Quyền cốt lõi:** `android.permission.WRITE_SETTINGS`.
- **Kiểm tra lúc thực thi:** Trước khi thay đổi cỡ chữ, luôn kiểm tra `Settings.System.canWrite(context)`.
- **Xử lý khi chưa cấp quyền:** Điều hướng an toàn bằng Intent `Settings.ACTION_MANAGE_WRITE_SETTINGS` với URI trỏ về `package:com.example.fontsizecontroller`. Bọc trong khối try-catch để phòng ngừa lỗi crash trên các bản ROM OEM đặc thù.
- **Tự động làm mới trong vòng đời:** Khi người dùng quay lại từ màn hình cài đặt của máy (`Lifecycle.Event.ON_RESUME`), app phải tự động đọc lại trạng thái quyền và cỡ chữ hiện hành.

## 2. Ranh giới kiến trúc hệ điều hành: Cỡ chữ vs Kiểu phông
- **Cỡ chữ (`FONT_SCALE`):** Có API chuẩn AOSP, áp dụng **toàn bộ hệ điều hành** khi có quyền `WRITE_SETTINGS`.
- **Kiểu phông chữ (`Typeface`):** Không có API mở toàn hệ thống trong Android SDK; phân vùng `/system/fonts` bị khóa Read-Only; Samsung One UI đòi hỏi chứng chỉ ký độc quyền FlipFont. Do đó, kiểu phông chữ được áp dụng cho **toàn bộ ứng dụng FontM** và hướng dẫn người dùng mở Cài đặt Màn hình nếu muốn đổi phông toàn máy.

## 3. Tiêu chuẩn thiết kế Tiện ích Bảng thông báo (Notification Drawer)
- **Giới hạn kỹ thuật của `RemoteViews`:**
  - Tuyệt đối KHÔNG sử dụng `SeekBar` hay `ScrollView` trong RemoteViews (sẽ gây lỗi `Class not allowed to be inflated in RemoteViews`).
  - Cử chỉ kéo ngang trên thông báo bị Android `SystemUI` chiếm quyền để xóa thông báo (`Swipe-to-dismiss`).
- **Giải pháp chuẩn hóa:** Xây dựng **Thanh trượt mốc trực quan (Visual Slider Track)**:
  - Thanh ray tiến trình `ProgressBar` chuẩn màu Gradient (`custom_progress_track.xml`).
  - 6 Mốc chạm nhanh phân bố đều trên thanh trượt (`0.85x`, `1.00x`, `1.15x`, `1.30x`, `1.60x`, `2.00x`).
  - Đổi màu nền mốc đang chọn sang màu phát sáng xanh ngọc (`bg_track_node_active.xml`) bằng lệnh `setInt(nodeId, "setBackgroundResource", resId)` và `setTextColor(...)`.
  - Bộ nút tăng/giảm từng nấc `[-]` / `[+]` (`ACTION_STEP_SCALE`).
- **Cấu hình `NotificationChannel`:**
  - Độ ưu tiên: `NotificationManager.IMPORTANCE_HIGH`.
  - Âm thanh & Rung: `setSound(null, null)`, `enableVibration(false)` để việc chạm đổi cỡ chữ không phát tiếng ting ting liên tục.
  - Cờ thông báo: `setOngoing(true)` (thường trực), `setShowWhen(false)`, `setOnlyAlertOnce(true)`.

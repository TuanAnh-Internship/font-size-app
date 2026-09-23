# UI/UX Design System & Navigation Standards

## 1. Bảng màu & Nhận diện thị giác (Modern Design Identity)
- **Primary Navy:** `#0F172A` (Nền chính tối) / `#1E293B` (Bề mặt Card).
- **Light Background:** `#F8FAFC` (Nền sáng ngà) / `#FFFFFF` (Card sáng).
- **Vibrant Purple:** `#8B5CF6` / `#6366F1` (Điểm nhấn hành động, Stepper, Gradient tiến trình).
- **Emerald Mint:** `#10B981` (Trạng thái thành công, Huy hiệu cỡ chữ, Mốc kích hoạt trên thanh trượt).
- **Border Subtle:** `#334155` (Đường viền tối) / `#E2E8F0` (Đường viền sáng).
- **Muted Text:** `#94A3B8` / `#64748B`.

## 2. Tiêu chuẩn điều hướng & Cấu trúc màn hình (Navigation Structure)
- **4 Tab chính trên thanh điều hướng đáy (`AppBottomNavigationBar`):**
  1. `Cỡ Chữ` (`ScreenDestination.FONT_SIZE` - Màn hình chính điều khiển kích thước).
  2. `Kiểu Chữ` (`ScreenDestination.FONT_GALLERY` - Thư viện chọn phông chữ).
  3. `Thị Lực` (`ScreenDestination.EYE_TEST` - Bài kiểm tra thị lực đọc văn bản).
  4. `Cài Đặt` (`ScreenDestination.SETTINGS` - Cài đặt ứng dụng, tiện ích thông báo, hồ sơ gia đình).
- **Quy tắc nút Quay lại (`<-`):**
  - **Trên 4 Tab chính:** Tuyệt đối KHÔNG hiển thị nút quay lại (`<-`), chỉ hiển thị nút Cài Đặt (hoặc các nút tiện ích như Đổi ngôn ngữ, Bật/Tắt Dark Mode).
  - **Trên các màn hình phụ:** (Như màn hình cấp quyền `PERMISSION`, màn hình kết quả `RESULT`, màn hình lỗi OEM `UNSUPPORTED_ERROR`), Top App Bar bắt buộc có nút quay lại `<-` dẫn về tab chính tương ứng.

## 3. Quy chuẩn UX Thư viện Kiểu chữ (Font Gallery)
- **Thiết kế Single-Card tối giản:** Không dàn trải danh sách nhiều card làm tràn màn hình. Toàn bộ tính năng gói gọn trong **1 Card duy nhất**:
  - Hộp thả xuống `ExposedDropdownMenuBox` chọn nhanh phông chữ (Inter, Roboto Slab, Playfair, Be Vietnam Pro, + Cài đặt kiểu chữ mới...).
  - Thẻ xem trước văn bản mẫu ngắn gọn, tự động co giãn theo cỡ chữ hệ thống hiện hành.
  - Nút bấm "Áp dụng kiểu chữ này" hiển thị phản hồi tức thì với biểu tượng tích xanh.

## 4. Quy chuẩn UX Hồ sơ cỡ chữ gia đình (Family Profiles)
- Sử dụng mô hình **Accordion Dropdown** (nút bấm hình mũi tên `^` / `v` để đóng/mở danh sách 3 cấu hình chuẩn: Cá nhân 1.00x, Bố mẹ 1.15x, Ông bà 1.30x), tránh làm dài giao diện cài đặt khi không cần thiết.

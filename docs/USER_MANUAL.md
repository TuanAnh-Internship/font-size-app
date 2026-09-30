# Sổ Tay Hướng Dẫn Sử Dụng Ứng Dụng FontM (User Manual v1.0.0)

Chào mừng bạn đến với **FontM - Bộ Điều Khiển Cỡ Chữ & Hệ Sinh Thái Trợ Năng Chăm Sóc Thị Lực Chuẩn Thiết Kế Cho Android (Phiên bản v1.0.0 Chính Thức)**!  
Tài liệu này hướng dẫn chi tiết cách sử dụng các tính năng từ cơ bản đến nâng cao của FontM.

---

## 1. Tổng Quan Về Ứng Dụng
FontM được phát triển nhằm mang lại trải nghiệm đọc tối ưu và bảo vệ thị lực toàn diện cho mọi lứa tuổi:
- **Người trẻ & Chuyên viên văn phòng**: Tùy chỉnh tỉ lệ chữ vừa vặn, giao diện tối (Dark mode) dịu mắt, chuyển đổi nhanh giữa các môi trường làm việc.
- **Người cao tuổi & Bố mẹ, Ông bà**: Cỡ chữ lớn tức thì, nét chữ đậm nét rõ ràng, kiểm tra thị lực thông minh, kính lúp soi chữ đời thực.
- **Tiện ích thao tác nhanh**: Điều chỉnh cỡ chữ ngay trên thanh thông báo hệ thống và ô phím tắt Cài đặt nhanh (Quick Settings Tile) mà không cần mở ứng dụng.

---

## 2. Hướng Dẫn Sử Dụng Chi Tiết Từng Tính Năng

### 2.1. Tab "Cỡ Chữ" (Trang Chủ)
1. **Thẻ cỡ chữ hiện tại**:
   - Hiển thị trực quan tỉ lệ phần trăm và hệ số (ví dụ: `1.00x — Mặc định`, `1.30x — Rất lớn`).
   - Nút **"Khôi phục mặc định"**: Chạm vào sẽ mở hộp thoại xác nhận đưa hệ số về `1.00x (100%)` an toàn.
2. **Hồ sơ gia đình (Family Profiles)**:
   - Gồm 3 hồ sơ được tối ưu sẵn:
     * **Cá Nhân (1.00x - 100%)**: Dành cho người dùng thông thường, kích thước chuẩn.
     * **Bố Mẹ (1.20x - 120%)**: Cỡ chữ to hơn, dễ nhìn tin nhắn & báo chí.
     * **Ông Bà (1.45x - 145%)**: Cỡ chữ cực lớn, nét to rõ ràng.
   - Nhấn vào bất kỳ hồ sơ nào để áp dụng ngay lập tức cho toàn bộ điện thoại.
3. **Thẻ xem trước trực tiếp (Live Preview)**:
   - Hiển thị mẫu văn bản mô phỏng trực tiếp độ lớn chữ tức thì khi bạn thay đổi mốc hoặc kéo thanh trượt.
4. **4 Mốc chọn nhanh (Presets)**:
   - **Nhỏ (0.85x)** | **Chuẩn (1.00x)** | **Lớn (1.15x)** | **Rất Lớn (1.30x)**.
5. **Thanh trượt tự do (Custom Slider)**:
   - Kéo trượt mượt mà từ **0.80x (80%)** đến **2.00x (200%)** với bước nhảy vi mô `0.05x`.
   - Có hai nút `-` và `+` để tinh chỉnh từng bước nhỏ chính xác.
6. **Phản hồi rung xúc giác (Haptic Feedback)**:
   - Mỗi lần chạm nút, chọn mốc hoặc kéo trượt thanh slider đều có rung nhẹ xác nhận, hỗ trợ thao tác chuẩn xác ngay cả khi tay run.

---

### 2.2. Tab "Đo Mắt" (Smart Vision Eye Test)
- **Quy trình trắc nghiệm thị lực 3 bước thông minh:**
  1. **Bước 1 - Khoảng cách đọc sách báo thông thường (35 – 40cm)**: Đánh giá độ thoải mái khi đọc đoạn văn bản ngắn.
  2. **Bước 2 - Khoảng cách cánh tay (50 – 60cm)**: Đánh giá khả năng đọc lướt tiêu đề và bảng tin.
  3. **Bước 3 - Khoảng cách xa (70cm+)**: Kiểm tra nhận diện chữ kích thước tiêu chuẩn mô phỏng bảng Snellen.
- **Động cơ phân tích độ thoải mái thị giác (`EyeTestDiagnosticEngine`)**:
  - Tự động phân tích các phản hồi của người dùng để xác định ngưỡng đọc tối ưu cho thị lực (`1.00x`, `1.15x`, `1.30x` hoặc `1.45x`).
  - Cung cấp nút 1 chạm **"Áp dụng ngay cỡ chữ gợi ý"** để thay đổi font hệ thống tức thì mà không cần cài đặt thủ công.
  - Giao diện bài đo mắt luôn được cố định ở chuẩn `1.00x` nhằm đảm bảo kết quả trắc nghiệm đạt độ chính xác 100%.

---

### 2.3. Tab "Trợ Năng" (Display Accessibility)
- **Thẻ so sánh trực quan (Before / After Comparison Card)**:
  - Cho phép người dùng nhìn thấy trực tiếp sự khác biệt giữa văn bản mặc định và sau khi tối ưu hiển thị.
- **Tùy chỉnh hệ thống chuyên sâu**:
  * **Chữ đậm toàn hệ thống (System Bold Text [HOT])**: Tăng nét chữ đậm rõ nét trên toàn bộ ứng dụng và tin nhắn.
  * **Chế độ tương phản cao (High Contrast Mode)**: Tối ưu màu sắc chữ trên nền đen/vàng để mắt yếu hoặc người khiếm thị không bị chói mắt.
  * **Thu phóng màn hình (Screen Magnification)**: Thanh trượt điều chỉnh tỉ lệ phóng đại giao diện từ 85% đến 150%.
- **Tiện ích trợ năng thông minh**:
  * **Tự động phóng to chữ ban đêm (Scheduled Font Scale)**: Tự động đổi cỡ chữ lên mức đọc thư giãn (`1.25x` – `1.35x`) từ 20:00 tối và tự động khôi phục về `1.00x` vào 07:00 sáng hôm sau. Không gây hao pin ngầm.
  * **Kính lúp soi chữ nhỏ đời thực (Quick Camera Loupe Magnifier)**:
    - Sử dụng camera của máy để soi đọc hạn sử dụng trên vỉ thuốc, hóa đơn tính tiền, sách báo.
    - Hỗ trợ 4 mức zoom nhanh: **1x (Chuẩn)**, **2x (To)**, **3x (Rất to)**, **5x (Siêu to)**.
    - Bật/Tắt đèn Flash trợ sáng khi đọc trong môi trường thiếu sáng.
    - **Nút "Giữ hình" (Pause Frame)**: Đóng băng khung hình để đọc dễ dàng không sợ mỏi hoặc run tay.
    - Bộ lọc tương phản đảo màu giúp đọc chữ mờ cực rõ nét.

---

### 2.4. Phím Tắt Tiện Ích Ngoài Màn Hình

#### 1. Bảng điều khiển trên thanh thông báo (Notification Quick Control)
- Bật/Tắt dễ dàng trong mục Cài đặt.
- Cung cấp thanh trượt tiến trình trực quan (`0.85x` ──▶ `2.00x`).
- 4 mốc chạm nhanh: `0.85x` (Nhỏ), `1.00x` (Chuẩn), `1.25x` (Lớn), `1.50x` (Rất lớn).
- 2 nút tăng giảm `[-]` và `[+]` với bước nhảy `0.15x`.

#### 2. Phím tắt Cài đặt nhanh (Quick Settings Tile)
- Vuốt thanh trạng thái của Android từ mép trên xuống (cạnh icon Wi-Fi/Bluetooth).
- Nhấn nút "Sửa" (Bút chì) để kéo biểu tượng **"Cỡ Chữ FontM"** vào bảng điều khiển nhanh.
- Nhấn 1 chạm vào biểu tượng để xoay vòng nhanh kích thước chữ toàn hệ thống: `1.00x` ➔ `1.25x` ➔ `1.50x` ngay cả khi đang xem phim, gọi video hay ở màn hình khóa.

---

### 2.5. Cài Đặt & Khả Năng Tương Thích Thiết Bị (Settings & Help)
- **Kiểu chữ ứng dụng**: Lựa chọn font riêng biệt cho FontM (*Roboto, Samsung One, Noto Serif, Dancing Script, Droid Sans Mono*).
- **Chuyển đổi giao diện Tối (Dark Mode)**: Dịu mắt, tiết kiệm pin AMOLED.
- **Đa ngôn ngữ 1 chạm**: Chuyển đổi tức thì giữa **Tiếng Việt (VI)** và **Tiếng Anh (EN)**.
- **Hộp thoại chẩn đoán tương thích OEM (OEM Diagnostics Card)**:
  - Tự động nhận diện dòng máy đang chạy (Samsung OneUI, Xiaomi HyperOS/MIUI, Oppo ColorOS, Android thuần).
  - Cung cấp nút mở trực tiếp trang cài đặt tắt Tối ưu hóa pin (Battery Optimization Whitelist) để đảm bảo lịch hẹn giờ và khay thông báo hoạt động 24/7 ổn định.

---

## 3. Khắc Phục Sự Cố Thường Gặp
1. **Ứng dụng yêu cầu "Cấp quyền thay đổi cài đặt hệ thống"**:
   - Khi mở ứng dụng lần đầu, chạm nút "Cấp quyền ngay" để mở cài đặt Android > Bật công tắc cho FontM.
2. **Quyền Camera cho Kính lúp soi chữ**:
   - Chạm nút "Cho phép" khi mở Kính lúp lần đầu. FontM chỉ sử dụng camera để hiển thị trực tiếp và không lưu trữ bất kỳ hình ảnh nào vào máy.
3. **Cỡ chữ trên màn hình chính chưa thay đổi ngay**:
   - Trên một số dòng máy Samsung OneUI hoặc Xiaomi HyperOS, launcher hệ thống có thể cần 1-2 giây để tải lại kích thước icon và nhãn ứng dụng.

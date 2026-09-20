# Technical Requirements & Acceptance Criteria — Project 1: Font Size App

> **Tài liệu đặc tả kỹ thuật (Technical Specifications)**  
> **Giai đoạn:** Tuần 2 - Ngày 1  
> **Phiên bản:** 1.0  
> **Trạng thái:** Chốt Technical Requirements & Acceptance Criteria  

---

## 1. Mục tiêu (Objective)

Chuyển đổi các yêu cầu sản phẩm (Product Requirements) từ Tuần 1 thành đặc tả kỹ thuật chi tiết, có thể kiểm thử (testable), đo lường được (measurable) và phản ánh đúng cơ chế của Android OS.

Tài liệu này xác định rõ:
- Quyết định kỹ thuật cho các câu hỏi mở (Open Questions Q01–Q07).
- Ma trận hỗ trợ nền tảng (Platform & Support Matrix).
- Đặc tả chi tiết 6 tính năng cốt lõi (Technical Requirements F01–F06) bao gồm Input, Behavior, Output, Architecture rules.
- Bộ tiêu chí chấp nhận (Acceptance Criteria AC01–AC08) theo chuẩn Given / When / Then.
- Kịch bản xử lý biên (Edge Cases Handling).
- Danh sách giá trị Font Scale Presets chính thức.

---

## 2. Task 1 - Chốt Open Questions (Q01–Q07)

*Quy tắc tài liệu: Các mục đã qua kiểm chứng kỹ thuật hoặc đối chiếu tài liệu chính thức được đánh dấu **Confirmed / Approved**; các mục còn phụ thuộc trao đổi cuối cùng với mentor được ghi rõ **Assumption** và trạng thái **Pending**.*

| ID | Câu hỏi | Hướng xử lý & Phân tích kỹ thuật | Quyết định đề xuất | Kết quả / Trạng thái |
|---|---|---|---|---|
| **Q01** | Direct system font hay Settings-assisted flow? | - Android 6.0+ (API 23+) cho phép ghi `Settings.System.FONT_SCALE` khi có quyền `WRITE_SETTINGS`.<br>- Một số OEM (MIUI, HyperOS, ColorOS) hoặc Android 14+ có thể hạn chế ghi ngầm hoặc không đồng bộ ngay tức thì.<br>- **Giải pháp:** Tiếp cận Hybrid (Ưu tiên Direct write; nếu thất bại hoặc thiếu quyền thì Fallback sang mở Cài đặt màn hình hệ thống). | **Hybrid Flow:**<br>1. Kiểm tra `canWrite()` → Direct write qua `Settings.System.putFloat()`.<br>2. Xác minh (verify) lại giá trị.<br>3. Fallback sang mở `Settings.ACTION_DISPLAY_SETTINGS` nếu ghi thất bại hoặc quyền bị từ chối. | **Pending**<br>*(Assumption: Ưu tiên Direct write + Fallback Settings-assisted)* |
| **Q02** | minSdk / Android versions? | - Căn cứ `build.gradle.kts` hiện tại: `minSdk = 29` (Android 10), `targetSdk = 36`, `compileSdk = 37`.<br>- Android 10+ chiếm >93% thiết bị, hỗ trợ đầy đủ API `canWrite()` và Compose UI Material 3.<br>- Cần lưu ý Android 14+ (API 34) có non-linear font scaling tới 200%. | Chốt hỗ trợ từ **Android 10 (API 29)** đến **Android 15+ (API 35+)**. | **Confirmed**<br>*(Phù hợp cấu hình project hiện tại)* |
| **Q03** | Preset scale values? | - Trên Android thuần (AOSP / Pixel): các nấc mặc định là `0.85`, `1.00`, `1.15`, `1.30`.<br>- Trên Samsung OneUI: có dải giá trị mở rộng từ 0.82 đến 1.35.<br>- Prototype trước đây (0.85 / 1.0 / 1.15 / 1.30) đáp ứng chuẩn AOSP. | Chốt 4 preset chuẩn AOSP: `0.85`, `1.00`, `1.15`, `1.30`. Nếu hệ thống trả về số khác, hiển thị trạng thái `Custom (x.xx)`. | **Pending**<br>*(Assumption: Dùng 4 nấc AOSP chuẩn + Custom mapping)* |
| **Q04** | Có dùng WRITE_SETTINGS? | - Bắt buộc phải có để thực hiện Direct system font scale thay đổi giá trị `Settings.System.FONT_SCALE`.<br>- Khai báo trong AndroidManifest và xin quyền qua `ACTION_MANAGE_WRITE_SETTINGS`. | Chấp thuận sử dụng quyền `android.permission.WRITE_SETTINGS`. | **Approved** |
| **Q05** | OEM / device cần support? | - Thiết bị thật sẵn có: Samsung (OneUI).<br>- Emulator: Google Pixel (AOSP thuần, Android 13/14).<br>- Giới hạn scope kiểm thử để đảm bảo độ tin cậy. | Thiết bị ưu tiên:<br>1. Samsung (Thiết bị vật lý thật).<br>2. Google Pixel (AOSP Emulator). | **Confirmed** |
| **Q06** | Có UI guideline? | - Chưa có tài liệu thiết kế riêng từ công ty.<br>- Áp dụng **Material Design 3 (Material You)** chuẩn của Google kết hợp Accessibility Standards (WCAG 2.1 AA - Touch target tối thiểu 48x48dp, contrast ratio chuẩn). | Sử dụng Material 3 Design System trong Jetpack Compose. | **Confirmed** |
| **Q07** | Smart Font Recommendation có scope? | - Tính năng gợi ý font thông minh (AI/Heuristics) đòi hỏi khảo sát thị lực và logic riêng.<br>- Tuần 2 cần tập trung hoàn thiện Core Architecture, System API, State Handling và UI Preview. | Xếp vào nhóm **Nice-to-have / Out of scope** cho giai đoạn Core Tuần 2. Sẽ nghiên cứu ở Tuần 3 nếu kịp tiến độ. | **Confirmed**<br>*(Out of Week 2 Core)* |

---

## 3. Task 2 - minSdk và Android Version Support Matrix

| Mục | Giá trị xác định | Ghi chú & Ràng buộc kỹ thuật |
|---|---|---|
| **minSdk** | **29** (Android 10 Q) | Đảm bảo bao phủ >93% thiết bị thị trường; hỗ trợ đầy đủ `Settings.System.canWrite(context)` (có từ API 23) và tương thích hoàn hảo với Jetpack Compose. |
| **targetSdk** | **34** (Android 14) / **36** | Khai báo theo chuẩn mới nhất của Google Play Console; bắt buộc xử lý non-linear font scaling (Android 14+). |
| **compileSdk** | **34** – **37** | Biên dịch với Android SDK mới nhất của project. |
| **Android versions ưu tiên test** | **Android 12, 13, 14, 15** | - Android 12 (API 31): Splash screen, Material You nền tảng.<br>- Android 13 (API 33): Runtime behavior ổn định.<br>- Android 14 (API 34): **Trọng tâm kiểm thử** Non-linear font scaling tới 200%.<br>- Android 15 (API 35): Kiểm tra tính tương thích biên. |
| **OEM ưu tiên kiểm thử** | **Samsung (OneUI), Google Pixel (AOSP)** | - Samsung: Thiết bị vật lý thực tế của dự án.<br>- Pixel: Chuẩn hóa hành vi AOSP gốc trên Android Emulator. |

---

## 4. Task 3 - Technical Requirements (F01–F06)

### Tổng quan kiến trúc phân tầng (Architecture Boundary Rules)
1. **Separation of Concerns:** UI Layer (Compose) không được gọi trực tiếp `Settings.System` hoặc `ContentResolver`. Mọi thao tác phải thông qua `Repository` / `DataSource`.
2. **Safe Exception Handling:** System API call không bao giờ ném Exception trực tiếp lên UI thread. Mọi lỗi phải được bọc trong `Result<T>` hoặc `UIState.Error`.
3. **No Direct Mutation on Select:** Việc lựa chọn preset chỉ cập nhật In-memory State / Preview State, tuyệt đối không gọi lệnh ghi hệ thống trước khi người dùng nhấn "Apply".

---

### Chi tiết từng tính năng

#### F01: Read Current Font Scale
- **Mục tiêu:** Đọc kích thước chữ hiện tại của hệ điều hành và ánh xạ sang trạng thái hiển thị an toàn.
- **Input:** `Context` / `ContentResolver` / `Resources.configuration`.
- **Behavior:**
  1. Đọc giá trị `Settings.System.getFloat(resolver, Settings.System.FONT_SCALE, 1.0f)`.
  2. Đồng thời kiểm tra `configuration.fontScale` để đối chiếu tính nhất quán.
  3. Ánh xạ giá trị float sang danh sách Preset (Small / Normal / Large / Extra Large).
  4. Nếu giá trị không trùng khớp với các preset có sẵn, đánh dấu là trạng thái `Custom` kèm giá trị số (ví dụ: `1.08x`).
- **Output:** `CurrentFontScaleState` (`scale: Float`, `preset: FontSizePreset?`, `isCustom: Boolean`, `status: Status`).
- **Quy tắc kỹ thuật:** Bọc trong khối `try-catch`, fallback về `1.0f` nếu xảy ra `SettingNotFoundException` hoặc lỗi bảo mật, không làm vỡ UI.

#### F02: Select Preset
- **Mục tiêu:** Cho phép người dùng chọn một mức cỡ chữ mới từ danh sách.
- **Input:** `targetPreset: FontSizePreset` hoặc `targetScale: Float`.
- **Behavior:**
  1. Cập nhật biến `selectedPreset` trong `ViewModel` / UI State.
  2. Trạng thái `selected` hoàn toàn độc lập với `currentSystemScale`.
  3. Tuyệt đối **không gọi** hàm ghi hệ thống.
  4. Bật/tắt trạng thái nút "Apply" (Disable nếu `selectedPreset.scale == currentSystemScale`).
- **Output:** `UIState.selectedOption` được cập nhật; kích hoạt F03.

#### F03: Preview
- **Mục tiêu:** Hiển thị trực quan văn bản xem trước với cỡ chữ người dùng vừa chọn.
- **Input:** `selectedScale: Float`.
- **Behavior:**
  1. Áp dụng scale factor cục bộ cho vùng chứa Preview Text trong ứng dụng.
  2. Sử dụng đơn vị `sp` kết hợp cấu hình `LocalDensity` hoặc Typography tuỳ biến trong Jetpack Compose.
  3. Hệ thống Android bên ngoài ứng dụng hoàn toàn không bị ảnh hưởng.
- **Output:** Giao diện Card Preview cập nhật kích thước chữ tức thì.

#### F04: Permission Capability Check
- **Mục tiêu:** Kiểm tra xem ứng dụng có đủ quyền ghi cài đặt hệ thống hay không trước khi thực hiện ghi.
- **Input:** `Context`.
- **Behavior:**
  1. Gọi `Settings.System.canWrite(context)`.
  2. Trả về `Boolean` biểu thị trạng thái capability.
  3. Luôn re-check tại thời điểm người dùng nhấn "Apply" (không dùng giá trị cache cũ vì user có thể thu hồi quyền trong Settings bất cứ lúc nào).
- **Output:** `canWrite: Boolean`.

#### F05: Open Manage Write Settings
- **Mục tiêu:** Điều hướng người dùng đến đúng trang cấp quyền `WRITE_SETTINGS` của hệ thống khi chưa có quyền.
- **Input:** `Context`, package name.
- **Behavior:**
  1. Khởi tạo `Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:${context.packageName}"))`.
  2. Kiểm tra `intent.resolveActivity(packageManager) != null`.
  3. Nếu resolve được: gọi `context.startActivity(intent)`.
  4. Nếu không resolve được (một số OEM tùy biến làm mất Activity này): Fallback mở `Settings.ACTION_SETTINGS` hoặc hiển thị Dialog hướng dẫn thủ công, không để crash ứng dụng.
- **Output:** Mở màn hình Cài đặt hệ thống hoặc hiển thị hướng dẫn fallback.

#### F06: Apply + Verify
- **Mục tiêu:** Thực hiện ghi kích thước chữ mới vào hệ thống và xác thực kết quả.
- **Input:** `targetScale: Float`, `Context`.
- **Behavior:**
  1. Kiểm tra lại `Settings.System.canWrite(context)`. Nếu `false`, chuyển trạng thái sang `PermissionRequired`.
  2. Nếu `true`, gọi `Settings.System.putFloat(resolver, Settings.System.FONT_SCALE, targetScale)`.
  3. **Bước Verify bắt buộc:** Đọc lại giá trị từ `Settings.System.getFloat(...)`.
  4. So sánh giá trị vừa đọc với `targetScale`:
     - Nếu khớp (dung sai `abs(read - target) < 0.01`): Chuyển trạng thái sang `Success`, cập nhật `currentSystemScale`.
     - Nếu không khớp hoặc ném lỗi SecurityException: Chuyển trạng thái sang `Error / Unsupported OEM`.
- **Output:** `ApplyResult` (`SUCCESS`, `PERMISSION_DENIED`, `VERIFY_FAILED`, `UNSUPPORTED`).

---

## 5. Task 4 - Acceptance Criteria (AC01–AC08)

Các tiêu chí chấp nhận được chuẩn hóa theo định dạng **Given / When / Then**:

| ID | Kịch bản (Scenario) | Given (Điều kiện tiên quyết) | When (Thao tác kích hoạt) | Then (Kết quả mong muốn) |
|---|---|---|---|---|
| **AC01** | Khởi động app hiển thị đúng cỡ chữ | Giá trị `FONT_SCALE` hệ thống hợp lệ (ví dụ: `1.0f`). | Người dùng mở ứng dụng. | Giá trị hiện tại được hiển thị đúng trên giao diện, preset tương ứng được highlight (hoặc hiển thị đúng giá trị). |
| **AC02** | Xử lý scale tùy chỉnh không khớp preset | Giá trị `FONT_SCALE` hệ thống là giá trị lẻ (ví dụ: `1.08f` do OEM đặt). | Người dùng mở ứng dụng. | App hiển thị nhãn `Custom (1.08x)`, không bị crash và các preset vẫn cho phép chọn bình thường. |
| **AC03** | Xem trước độc lập với hệ thống | Cỡ chữ hiện tại là `Normal (1.00)`. | Người dùng chọn preset `Large (1.15)`. | Vùng Preview hiển thị chữ phóng to theo mức `Large`, nhưng cỡ chữ toàn hệ thống và các app khác vẫn giữ nguyên `1.00`. |
| **AC04** | Nhấn Apply khi chưa có quyền | `Settings.System.canWrite(context) == false`. | Người dùng nhấn nút "Apply". | Hệ thống không gọi lệnh ghi; giao diện hiển thị thông báo `Yêu cầu cấp quyền` kèm nút dẫn đến Cài đặt. |
| **AC05** | Mở màn hình quản lý quyền thành công | Ứng dụng đang yêu cầu quyền `WRITE_SETTINGS`. | Người dùng nhấn nút "Cấp quyền / Mở Cài đặt". | Màn hình `ACTION_MANAGE_WRITE_SETTINGS` của ứng dụng được mở (nếu intent resolve được). |
| **AC06** | Tự động kiểm tra lại quyền khi quay về app | Người dùng vừa từ màn hình Cài đặt hệ thống quay trở lại app (`onResume`). | Activity được resume. | Ứng dụng tự động gọi lại `canWrite(context)` để cập nhật trạng thái mới nhất lên UI mà không cần tắt mở lại app. |
| **AC07** | Áp dụng cỡ chữ thành công và xác thực | `Settings.System.canWrite(context) == true`. | Người dùng chọn một mức khác và nhấn "Apply". | Ứng dụng thực hiện ghi → đọc lại giá trị hệ thống → xác nhận trùng khớp → hiển thị thông báo thành công. |
| **AC08** | Phát hiện ghi thất bại / OEM từ chối | `Settings.System.canWrite(context) == true` nhưng hệ thống OEM chặn ngầm việc sửa `FONT_SCALE`. | Người dùng nhấn "Apply". | Sau khi ghi, bước verify đọc lại thấy giá trị không đổi → hiển thị thông báo lỗi `Không thể áp dụng trên thiết bị này`, không được báo thành công giả tạo. |

---

## 6. Task 5 - Edge Cases Handling Matrix

| STT | Tình huống biên (Edge Case) | Hành vi kỹ thuật mong đợi (Expected Technical Behavior) |
|---|---|---|
| 1 | **Permission Denied** (Người dùng từ chối cấp quyền trong Cài đặt) | Ứng dụng tuyệt đối không crash; hiển thị banner/thông báo giải thích rõ lý do cần quyền và cung cấp nút thử lại. Các tính năng Preview vẫn hoạt động bình thường. |
| 2 | **Permission Revoked** (Quyền bị thu hồi khi app đang chạy nền) | Khi user quay lại app hoặc bấm Apply, app re-check `canWrite()` tức thời và chuyển ngay về trạng thái `PermissionRequired`. |
| 3 | **Device/OEM không hỗ trợ ghi trực tiếp** (Bị OEM chặn ngầm) | Bắt lỗi hoặc nhận diện qua bước Verify thất bại. Hiển thị thông báo `Thiết bị không hỗ trợ ghi trực tiếp` và cung cấp nút mở Cài đặt màn hình của máy (Settings-assisted fallback). |
| 4 | **Scale Unknown / Custom** (Font scale nằm ngoài dải 4 preset) | UI map vào trạng thái `Custom (x.xx)`. Không cố ép sai lệch vào một preset bất kỳ; không gây lỗi index out of bounds. |
| 5 | **`ACTION_MANAGE_WRITE_SETTINGS` không resolve được** | Bọc `startActivity` trong khối `try-catch (ActivityNotFoundException)`. Fallback mở màn hình `Settings.ACTION_SETTINGS` tổng hoặc hiển thị Toast/Dialog hướng dẫn người dùng tìm kiếm thủ công. |
| 6 | **User quay về từ Settings nhưng vẫn chưa gạt bật quyền** | `canWrite()` trả về `false`. Nút Apply giữ trạng thái yêu cầu quyền; không tiến hành ghi. |
| 7 | **Selected Scale trùng với Current Scale** | Vô hiệu hóa (Disable) nút "Apply" hoặc khi bấm hiển thị thông báo "Cỡ chữ này đang được áp dụng", tránh gọi lệnh ghi thừa thãi vào hệ thống. |
| 8 | **Activity Recreate / Configuration Change** | Khi cỡ chữ hệ thống đổi, Android có thể kích hoạt configuration change tái tạo Activity. State lựa chọn và thông báo phải được bảo toàn qua `rememberSaveable` hoặc `ViewModel`. |

---

## 7. Task 6 - Bảng Font Presets Chính Thức

| Preset Name | Scale Factor | Mô tả / Tỷ lệ tương đối | Trạng thái | Nguồn xác nhận & Căn cứ |
|---|---|---|---|---|
| **Small** | `0.85f` | Nhỏ hơn bình thường 15% | **Confirmed** | Chuẩn AOSP font scale nấc 1 (Android Developers Reference) |
| **Normal** | `1.00f` | Kích thước mặc định của hệ thống | **Confirmed** | Chuẩn AOSP mặc định (Base density scale factor) |
| **Large** | `1.15f` | Lớn hơn bình thường 15% | **Confirmed** | Chuẩn AOSP font scale nấc 3 |
| **Extra Large** | `1.30f` | Lớn hơn bình thường 30% | **Confirmed** | Chuẩn AOSP font scale nấc 4 (Trước Android 14) |
| *Custom* | `Any float` | Các mức lẻ do OEM (Samsung/Xiaomi) thiết lập | **Confirmed** | Cơ chế Fallback hiển thị giá trị thực tế của hệ thống |

> **Ghi chú về Android 14+ (Non-linear Font Scaling):**  
> Trên Android 14+, hệ thống hỗ trợ scale tối đa lên tới `2.00f` (200%) với thuật toán phi tuyến (chữ tiêu đề lớn tăng ít hơn chữ nội dung nhỏ để tránh vỡ layout). Danh sách 4 preset trên phục vụ cho phiên bản v1; ở các phiên bản nâng cao có thể bổ sung preset `Huge (1.50f)` hoặc `Maximum (2.00f)` sau khi kiểm thử giao diện kỹ lưỡng.

---

## 8. Kết luận & Kế hoạch tiếp theo (Next Steps)

Tài liệu này đóng vai trò là "kim chỉ nam" kỹ thuật để:
1. **Ngày 2 Tuần 2:** Thiết kế kiến trúc phần mềm chi tiết (Architecture Design: Clean Architecture / MVI-MVVM, Repository Pattern, System API Wrapper).
2. **Ngày 3 Tuần 2:** Thiết kế giao diện (UI Design & Figma Tokens) tuân thủ tiêu chuẩn Accessibility và Material 3.
3. **Ngày 4–5 Tuần 2:** Cài đặt mã nguồn, tích hợp System API và kiểm thử Unit/Edge Cases trên thiết bị thật.

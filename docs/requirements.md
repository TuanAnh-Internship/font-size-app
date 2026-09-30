# Requirements — Project 1: Font Size App

## 1. Project Overview

| Thuộc tính | Phân tích |
|---|---|
| Loại ứng dụng | Android Utility / Accessibility Support |
| Platform | Android |
| Công nghệ | Kotlin + Android Native + Jetpack Compose |
| Độ lớn nghiệp vụ | Nhỏ |
| Độ sâu kỹ thuật | Tương đối cao (System API / permission / compatibility) |
| Backend | Chưa cần |

## 2. Problem Statement

Người dùng Android có thể cần tăng hoặc giảm cỡ chữ để phù hợp với khả năng đọc, kích thước màn hình hoặc thói quen sử dụng. Tuy nhiên việc tìm đúng phần cài đặt, hiểu các mức cỡ chữ và biết mức nào phù hợp không phải lúc nào cũng trực quan.

```
Vấn đề
  ├─ Khó tìm đúng Settings
  ├─ Không biết nên chọn mức nào
  ├─ Không thấy trước thay đổi
  └─ Permission/thiết bị có thể gây khó hiểu
        ↓
Giải pháp
  Current → Select → Preview → Apply / Guidance
```

## 3. Đối tượng người dùng

| Nhóm người dùng | Nhu cầu chính | Điều app cần ưu tiên |
|---|---|---|
| Người dùng Android phổ thông | Muốn chỉnh cỡ chữ nhanh | Ít bước, UI dễ hiểu |
| Người lớn tuổi | Cần chữ lớn/dễ đọc | Text rõ, nút lớn, ít thuật ngữ kỹ thuật |
| Người gặp khó khăn với chữ nhỏ | Muốn tìm mức đọc thoải mái | Preview và lựa chọn rõ ràng |
| Người ít quen Android Settings | Không biết đường dẫn cài đặt | Hướng dẫn và permission flow rõ |

## 4. User Goals

- **UG-01:** Tôi muốn biết cỡ chữ hiện tại của thiết bị.
- **UG-02:** Tôi muốn lựa chọn một mức cỡ chữ dễ hiểu.
- **UG-03:** Tôi muốn xem trước trước khi áp dụng.
- **UG-04:** Nếu ứng dụng cần quyền, tôi muốn biết tại sao và phải làm gì.
- **UG-05:** Tôi muốn nhận thông báo rõ ràng sau khi thao tác.
- **UG-06 (mở rộng):** Nếu tôi không biết chọn mức nào, tôi muốn được ứng dụng gợi ý.

## 5. Feature List (Danh mục tính năng hoàn thiện v1.0.0)

| ID | Feature | Trạng thái | Mô tả |
|---|---|:---:|---|
| P1-F01 | Current Font Status | **Đã hoàn thành** | Đọc và hiển thị chính xác trạng thái/cỡ chữ hiện tại |
| P1-F02 | Font Size Selection | **Đã hoàn thành** | Lựa chọn 4 Preset hoặc thanh trượt Custom vi mô |
| P1-F03 | Preview | **Đã hoàn thành** | Xem trước trực tiếp theo đơn vị `.sp` trước khi Apply |
| P1-F04 | Apply | **Đã hoàn thành** | Ghi cài đặt an toàn và bắt buộc verify đọc lại |
| P1-F05 | Permission Handling | **Đã hoàn thành** | Kiểm tra `canWrite()` và điều hướng mở Cài đặt quyền |
| P1-F06 | Result Feedback | **Đã hoàn thành** | Thông báo thành công, thất bại, hoặc hướng dẫn lỗi OEM |
| P1-F07 | Reset Default | **Đã hoàn thành** | Hộp thoại an toàn khôi phục cỡ chữ chuẩn 1.00x |
| P1-F08 | Family Profiles | **Đã hoàn thành** | 3 hồ sơ định hình sẵn (Cá nhân 1.00x, Bố mẹ 1.20x, Ông bà 1.45x) |
| P1-F09 | System Bold Text | **Đã hoàn thành** | Chế độ xem trước chữ đậm và liên kết cài đặt máy |
| P1-F10 | Font Style Gallery | **Đã hoàn thành** | Thư viện phông chữ nội bộ (Roboto, Samsung One, Noto Serif...) |
| P1-AI01 | Smart Vision Test | **Đã hoàn thành** | Trắc nghiệm thị lực 3 bước với động cơ chẩn đoán và gợi ý font tự động |
| P1-F11 | Scheduled Night Font | **Đã hoàn thành** | Hẹn giờ tự động phóng to chữ buổi tối (20:00 - 07:00) qua AlarmManager |
| P1-F12 | Quick Camera Loupe | **Đã hoàn thành** | Kính lúp CameraX zoom 2x-5x, đèn flash, giữ yên hình và lọc tương phản |
| P1-F13 | Quick Settings Tile | **Đã hoàn thành** | Phím tắt cạnh Wi-Fi đổi nhanh 3 mốc font chỉ với 1 chạm |
| P1-F14 | Haptic Feedback | **Đã hoàn thành** | Rung phản hồi vật lý khi chạm nút/thanh trượt đạt chuẩn WCAG AAA |
| P1-F15 | App-wide Localization| **Đã hoàn thành** | Chuyển đổi song ngữ tức thì `🇻🇳 VI ⇄ 🇬🇧 EN` không cần restart |

## 6. Functional Requirements

| Function | Requirement |
|---|---|
| FR-P1-01 Display Current State | Khi mở màn hình chính, đọc/diễn giải trạng thái cỡ chữ hiện tại ở mức Android cho phép và hiển thị cho người dùng |
| FR-P1-02 Select Font Option | Cung cấp một nhóm preset; tại một thời điểm chỉ có một lựa chọn đang selected |
| FR-P1-03 Preview | Khi lựa chọn thay đổi, preview phải cập nhật nhưng chưa được coi là system setting đã thay đổi |
| FR-P1-04 Apply by explicit action | Chỉ bắt đầu thay đổi sau khi người dùng nhấn Apply |
| FR-P1-05 Permission/Capability Handling | Nếu thiếu quyền hoặc capability, giải thích và hướng dẫn thay vì crash |
| FR-P1-06 Re-check after external Settings | Sau khi người dùng quay lại từ Android Settings, kiểm tra lại trạng thái thay vì giả định quyền đã được cấp |
| FR-P1-07 Result Feedback | Sau thao tác, UI có trạng thái rõ ràng: success / permission required / failure / unsupported |

## 7. Non-functional Requirements

| ID | Nhóm | Yêu cầu |
|---|---|---|
| P1-NFR01 | Usability | Giao diện ít bước, thuật ngữ thân thiện |
| P1-NFR02 | Reliability | Không crash khi user từ chối quyền hoặc thao tác thất bại |
| P1-NFR03 | Compatibility | Kiểm thử trên nhiều Android version/thiết bị theo phạm vi mentor xác nhận |
| P1-NFR04 | Accessibility | Text dùng sp và UI chịu được font lớn |
| P1-NFR05 | Maintainability | Tách UI/state/system-access hợp lý khi bước sang triển khai |

## 8. Phạm vi

| Trong phạm vi (In scope) | Ngoài phạm vi (Out of scope) |
|---|---|
| Android UI và interaction | Web Admin |
| Font-size selection/preview | Backend REST API |
| Android system settings integration | Cloud database |
| Permission handling | Authentication/account |
| Device/version compatibility test | Microservices |
| Basic local state/data nếu cần | Cloud sync |

## 9. Đề xuất AI (Future)

**Smart Font Recommendation:** AI giải bài toán "người dùng không biết mức chữ nào phù hợp"

```
Recommend for me
   ↓
3-4 câu hỏi ngắn về khả năng đọc / ưu tiên
   ↓
Recommendation Engine (rule-based trước, AI sau)
   ↓
Recommended: Large
   ↓
Preview → User Confirm → Apply
```

> Phiên bản đầu dùng rule-based để chạy offline. AI chỉ gợi ý, không tự ý thay đổi System Settings.

---

# So Sánh Quy Mô 2 Project

| Tiêu chí | Project 1 - Font Size App | Project 2 - CV App |
|---|---|---|
| Platform | Android | iOS + Android |
| Quy mô nghiệp vụ | Nhỏ | Lớn |
| Số màn hình | Ít | Nhiều |
| Dữ liệu | Ít | Nhiều section, list, form động |
| System API | Quan trọng | Có nhưng không phải trọng tâm duy nhất |
| Permission | Rất quan trọng | Tùy file/share/storage feature |
| Local storage | Đơn giản/nếu cần | Core requirement |
| File/PDF | Không phải core | Core requirement |
| Template | Không | Core feature |
| Cross-platform | Không | Core challenge |
| Testing | Android version/device | Android + iOS + file/PDF/UI |
| Độ khó chính | Chiều sâu Android | Chiều rộng nghiệp vụ + đa nền tảng |

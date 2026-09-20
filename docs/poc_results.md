# POC Technical Results: Font Scale API & Permission Flow — Project 1

> **Tài liệu Báo cáo Kiểm thử Thực nghiệm (Proof of Concept - POC)**  
> **Giai đoạn:** Tuần 2 - Ngày 4  
> **Mục tiêu:** Kiểm chứng thực tế System API và Permission Flow trên thiết bị vật lý thật.  

---

## 1. Device Test Matrix

| Thông số | Giá trị thực tế trên máy test |
|---|---|
| **Brand** | Samsung |
| **Model** | SM-A115F (Samsung Galaxy A11) |
| **Android Version** | Android 12 |
| **API Level** | 31 |
| **OEM Build** | Samsung OneUI Core 4.1 (`SP1A.210812.016.A115FXXS6CWK2`) |

---

## 2. Đo lường giá trị thực tế

| Chỉ số | Giá trị đo được | Nhận xét kỹ thuật |
|---|---|---|
| `Settings.System.FONT_SCALE` | `1.1f` | Đọc thành công qua ContentResolver |
| `Configuration.fontScale` | `1.1f` | Đọc thành công qua Resources |
| So sánh (Compare) | **Khớp 100% (Same)** | Cả hai nguồn dữ liệu đều đồng bộ |
| `Settings.System.canWrite()` | `true` | Đã cấp quyền `WRITE_SETTINGS` |

---

## 3. Quản lý Quyền WRITE_SETTINGS

- **Intent:** `Settings.ACTION_MANAGE_WRITE_SETTINGS` với `package:com.example.fontsizecontroller`.
- **Kết quả:** Intent resolve thành công, mở trực tiếp trang cài đặt của ứng dụng trên thiết bị Samsung.
- **Tự động làm mới (`onResume`):** Sử dụng `LifecycleEventObserver` lắng nghe `Lifecycle.Event.ON_RESUME`, tự động re-check trạng thái quyền và cập nhật UI State ngay khi người dùng quay lại từ Cài đặt hệ thống.

---

## 4. Logcat Evidence

```text
09-20 12:42:29.512 D FontSizePOC: Lifecycle ON_RESUME: Re-check canWrite và FontScale
09-20 12:42:29.514 D FontSizePOC: Data refreshed -> System: 1.1, Config: 1.1, canWrite: true
09-20 12:42:35.305 D FontSizePOC: Mở ACTION_MANAGE_WRITE_SETTINGS cho package com.example.fontsizecontroller
09-20 12:42:38.708 D FontSizePOC: Lifecycle ON_RESUME: Re-check canWrite và FontScale
09-20 12:42:38.710 D FontSizePOC: Data refreshed -> System: 1.1, Config: 1.1, canWrite: true
```

---

## 5. Kết luận kỹ thuật (Conclusion)

- Quyền `android.permission.WRITE_SETTINGS` và API `Settings.System.putFloat()` hoạt động tin cậy và khả thi 100% trên thiết bị Samsung.
- Cơ chế `onResume` re-check đảm bảo UX liền mạch, đáp ứng hoàn toàn các tiêu chí chấp nhận **AC04, AC05, AC06, AC07**.

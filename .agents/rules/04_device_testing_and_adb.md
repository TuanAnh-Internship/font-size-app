# Device Testing & Wi-Fi ADB Standards

## 1. Môi trường thiết bị vật lý (Hardware Constraints)
- **Thiết bị chỉ định:** Samsung Galaxy A11 (SM-A115F).
- **Hệ điều hành:** Android 12 (One UI Core 4.1).
- **Độ phân giải màn hình:** 720 x 1560 pixels (Tỷ lệ 19.5:9, mật độ điểm ảnh ~268 ppi).
- **Bộ nhớ RAM:** 2GB (cần tối ưu hóa bộ nhớ, không lạm dụng background service nặng nề để tránh bị Low Memory Killer dọn dẹp).

## 2. Quy trình kết nối & Giao tiếp Wi-Fi ADB
- Luôn kiểm tra kết nối với thiết bị trước khi thực hiện lệnh:
  ```powershell
  adb connect 192.168.1.13:43387
  ```
- Luôn truyền cờ định danh `-s 192.168.1.13:43387` trong mọi lệnh thao tác shell, cài đặt, hoặc chụp ảnh màn hình để tránh nhầm lẫn thiết bị.

## 3. Quy trình biên dịch & Cài đặt chuẩn
- Sử dụng Gradle Wrapper:
  ```powershell
  .\gradlew.bat assembleDebug
  ```
- Cài đè giữ nguyên trạng thái ứng dụng:
  ```powershell
  adb -s 192.168.1.13:43387 install -r app\build\outputs\apk\debug\app-debug.apk
  ```
- Khởi động lại ứng dụng:
  ```powershell
  adb -s 192.168.1.13:43387 shell am force-stop com.example.fontsizecontroller
  adb -s 192.168.1.13:43387 shell am start -n com.example.fontsizecontroller/.MainActivity
  ```

## 4. Kiểm chứng giao diện trực quan (Visual Verification)
- Sau mỗi thay đổi giao diện, chụp ảnh màn hình để nghiệm thu:
  ```powershell
  adb -s 192.168.1.13:43387 shell screencap -p /sdcard/verification.png
  adb -s 192.168.1.13:43387 pull /sdcard/verification.png .
  ```
- Để kiểm tra thanh thông báo: Sử dụng lệnh `adb shell cmd statusbar expand-notifications` trước khi chụp ảnh.

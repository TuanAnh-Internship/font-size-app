# Day 02 - Android Project Structure

## Các file chính trong project

| File / Thư mục | Vai trò |
|---|---|
| `MainActivity.kt` | Màn hình chính, điểm khởi chạy của app |
| `AndroidManifest.xml` | Khai báo app, activity, permission |
| `app/build.gradle.kts` | Cấu hình build: SDK version, dependencies |
| `settings.gradle.kts` | Khai báo project name và các module |
| `gradlew.bat` | Chạy Gradle build trên Windows |
| `res/` | Tài nguyên: string, icon, theme |
| `java/com/example/fontsizecontroller/` | Toàn bộ source code Kotlin |

## Ghi chú
- Package chính: com.example.fontsizecontroller
- Công nghệ: Kotlin + Jetpack Compose + Material3
- minSdk = 29 (Android 10 trở lên)

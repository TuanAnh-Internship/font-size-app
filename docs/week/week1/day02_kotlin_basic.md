# Day 02 - Kotlin Basics & Data Model

## Kiến thức đã học

### Kotlin cơ bản
| Khái niệm | Mô tả ngắn | Ví dụ trong project |
|---|---|---|
| `val` / `var` | val = hằng số, var = biến đổi được | `val label = "Normal"`, `var selectedLabel = "Large"` |
| `String` / `Int` / `Float` / `Boolean` | Các kiểu dữ liệu cơ bản | `val scale: Float = 1.15f` |
| `if` / `if expression` | Điều kiện, có thể dùng như expression | `val desc = if (scale > 1.0f) "Lớn" else "Nhỏ"` |
| `when` | Thay thế switch/case, mạnh hơn | `when (label) { "Small" -> ... }` |
| `fun` | Khai báo hàm | `fun isLargeFont(scale: Float): Boolean` |
| Nullable `?` | Biến có thể null, tránh NullPointerException | `val option: FontSizeOption? = null` |
| Elvis `?:` | Fallback khi giá trị null | `option ?: FontSizeOption("Normal", 1.0f)` |
| `data class` | Class chứa dữ liệu, tự sinh equals/toString/copy | `data class FontSizeOption(val label: String, val scale: Float)` |
| `listOf` | Tạo danh sách bất biến | `listOf(0.85f, 1.0f, 1.15f, 1.30f)` |

## File đã tạo

| File | Vị trí | Chức năng |
|---|---|---|
| `day02_android_structure.md` | `docs/week/week1/` | Ghi chú cấu trúc project Android |
| `FontSizeOption.kt` | `model/` | data class chứa label và scale |
| `FontSizeData.kt` | `model/` | Danh sách 4 font size option mẫu |
| `FontSizeUtils.kt` | `model/` | 3 hàm xử lý: isLargeFont, findFontOption, getDefaultFontOption |

## Chức năng đã làm

### FontSizeOption.kt
```kotlin
data class FontSizeOption(
    val label: String,   // tên hiển thị: "Small", "Normal", "Large", "Extra Large"
    val scale: Float     // hệ số scale: 0.85f, 1.0f, 1.15f, 1.30f
)
```

### FontSizeData.kt
```kotlin
val fontSizeOptions = listOf(
    FontSizeOption("Small", 0.85f),
    FontSizeOption("Normal", 1.0f),
    FontSizeOption("Large", 1.15f),
    FontSizeOption("Extra Large", 1.30f)
)
// Dữ liệu PROTOTYPE - chưa kết nối System API (sẽ làm ở Tuần 2)
```

### FontSizeUtils.kt
```kotlin
fun isLargeFont(scale: Float): Boolean          // scale > 1.0f → true
fun findFontOption(label: String): FontSizeOption?  // tìm theo label, null nếu không có
fun getDefaultFontOption(): FontSizeOption      // trả về option "Normal"
```

## Kết quả build

```
.\gradlew.bat assembleDebug
BUILD SUCCESSFUL in 54s
35 actionable tasks: 6 executed, 29 up-to-date
```

## Lỗi đã gặp & cách xử lý

| Lỗi | Nguyên nhân | Cách xử lý |
|---|---|---|
| `Redeclaration: data class FontSizeOption` | Có 2 file cùng khai báo `FontSizeOption` trong src/ | Xóa file trùng, chỉ giữ 1 file |
| `more than one device/emulator` | Cùng lúc có điện thoại thật + Genymotion | Dùng `adb -s <device-id> install ...` để chỉ định thiết bị |
| `mainClass not found` | Kotlin tạo class từ tên file theo quy tắc riêng (giữ underscore) | Kiểm tra tên `.class` thật trong `build/classes/kotlin/main/` |

## Kết quả cài app

```
adb -s adb-R9JN60YZSVJ-FVMKFl._adb-tls-connect._tcp install -r app-debug.apk
Performing Streamed Install
Success
```

## Git commit

```
[main 8b58d10] feat: add font size data model
4 files changed, 66 insertions(+)
```

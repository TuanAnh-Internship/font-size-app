package com.example.fontsizecontroller.model

/**
 * Các chế độ đọc trực quan nhằm hỗ trợ thị lực và bảo vệ mắt.
 */
enum class ReadingMode(val labelVi: String, val labelEn: String) {
    STANDARD("Chuẩn", "Standard"),
    SEPIA("Sách giấy", "Warm Sepia"),
    HIGH_CONTRAST("Tương phản cao", "High Contrast")
}

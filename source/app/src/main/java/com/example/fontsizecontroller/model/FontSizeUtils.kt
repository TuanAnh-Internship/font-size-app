package com.example.fontsizecontroller.model

/**
 * Kiểm tra xem cỡ chữ có phải loại "lớn" không.
 * @return true nếu scale > 1.0f, false nếu scale <= 1.0f
 */
fun isLargeFont(scale: Float): Boolean {
    return scale > 1.0f
}

/**
 * Tìm FontSizeOption theo label.
 * @return FontSizeOption nếu tìm thấy, null nếu không có
 */
fun findFontOption(label: String): FontSizeOption? {
    return fontSizeOptions.find { it.label == label }
}

/**
 * Lấy FontSizeOption mặc định (Normal).
 * @return FontSizeOption có label == "Normal"
 */
fun getDefaultFontOption(): FontSizeOption {
    return findFontOption("Normal")
        ?: FontSizeOption("Normal", 1.0f)
}

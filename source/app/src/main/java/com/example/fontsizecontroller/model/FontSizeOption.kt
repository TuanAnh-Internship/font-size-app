package com.example.fontsizecontroller.model

/**
 * Model đại diện cho một lựa chọn cỡ chữ.
 *
 * @param label Tên hiển thị cho người dùng (ví dụ: "Small", "Normal", "Large")
 * @param scale Hệ số phóng to/thu nhỏ chữ (ví dụ: 0.85f, 1.0f, 1.15f)
 */
data class FontSizeOption(
    val label: String,
    val scale: Float
)

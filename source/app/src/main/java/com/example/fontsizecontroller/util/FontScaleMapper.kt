package com.example.fontsizecontroller.util

import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.fontSizeOptions
import kotlin.math.abs

/**
 * Tiện ích ánh xạ giữa giá trị Float scale của hệ thống và FontSizeOption domain model.
 */
object FontScaleMapper {

    /**
     * Tìm preset phù hợp nhất với giá trị scale thực tế.
     * Nếu không trùng khớp với các preset có sẵn, trả về Option dạng "Custom (x.xx)".
     */
    fun mapScaleToOption(scale: Float): FontSizeOption {
        return fontSizeOptions.firstOrNull { abs(it.scale - scale) < 0.03f }
            ?: FontSizeOption(label = "Custom (${String.format("%.2f", scale)}x)", scale = scale)
    }
}

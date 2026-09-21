package com.example.fontsizecontroller.util

import com.example.fontsizecontroller.model.FontSizeOption
import java.util.Locale
import kotlin.math.abs

/**
 * Tiện ích ánh xạ giữa giá trị Float scale của hệ thống Android và FontSizeOption domain model.
 * Logic thuần Kotlin (Pure Kotlin), hoàn toàn không phụ thuộc Android SDK để phục vụ Unit Test nhanh.
 */
object FontScaleMapper {

    /**
     * Danh sách 4 presets chuẩn của dự án:
     * Small (0.85x), Default (1.00x), Large (1.15x), Extra Large (1.30x)
     */
    val defaultPresets = listOf(
        FontSizeOption("Small", 0.85f),
        FontSizeOption("Default", 1.00f),
        FontSizeOption("Large", 1.15f),
        FontSizeOption("Extra Large", 1.30f)
    )

    /**
     * Ngưỡng sai số (tolerance) quy định cho project: 0.03f.
     * Giải quyết hiện tượng float rounding error hoặc các dòng máy OEM (như Samsung)
     * trả về các giá trị cận biên (ví dụ: 0.999f hay 1.151f).
     */
    const val TOLERANCE = 0.03f

    /**
     * Tìm preset phù hợp nhất với giá trị scale thực tế theo dung sai TOLERANCE.
     */
    fun toOption(
        scale: Float,
        presets: List<FontSizeOption> = defaultPresets
    ): FontSizeOption? {
        return presets.firstOrNull {
            abs(scale - it.scale) < TOLERANCE
        }
    }

    /**
     * Chuyển đổi giá trị scale sang nhãn hiển thị cho người dùng.
     * Nếu trùng khớp với preset -> trả về label của preset.
     * Nếu là custom scale (ví dụ: Samsung trả về 1.10x) -> trả về dạng "Custom (1.10x)".
     */
    fun toDisplayLabel(
        scale: Float,
        presets: List<FontSizeOption> = defaultPresets
    ): String {
        val preset = toOption(scale, presets)
        return preset?.label
            ?: String.format(Locale.US, "Custom (%.2fx)", scale)
    }

    /**
     * Tiện ích phụ trợ tương thích ngược: Ánh xạ scale sang một FontSizeOption cụ thể.
     */
    fun mapScaleToOption(
        scale: Float,
        presets: List<FontSizeOption> = defaultPresets
    ): FontSizeOption {
        return toOption(scale, presets)
            ?: FontSizeOption(
                label = String.format(Locale.US, "Custom (%.2fx)", scale),
                scale = scale
            )
    }
}

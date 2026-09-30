package com.example.fontsizecontroller.domain

import java.util.Locale

/**
 * Thuật toán đánh giá thị lực và đề xuất cỡ chữ phù hợp (Eye Test Diagnostic Engine).
 * Độc lập với UI Compose để phục vụ Unit Testing 100%.
 */
object EyeTestDiagnosticEngine {

    /**
     * Tính toán cỡ chữ đề xuất dựa trên kết hợp câu trả lời 2 bước:
     * @param step1Answer: Cảm nhận khoảng cách 35-40cm (0 = Chữ nhỏ mỏi mắt, 1 = Rõ ràng thoải mái, 2 = Chữ hơi to)
     * @param step2Answer: Bảng Snellen (0 = Dòng 1 cỡ lớn, 1 = Dòng 2 cỡ vừa, 2 = Dòng 3 cỡ nhỏ)
     */
    fun calculateRecommendedScale(step1Answer: Int, step2Answer: Int): Float {
        return when {
            // Bước 2 chỉ đọc được dòng lớn nhất -> Mắt yếu / lão thị cần phóng to mạnh
            step2Answer == 0 -> if (step1Answer == 0) 1.45f else 1.35f
            // Bước 2 đọc được dòng vừa -> Cần phóng to nhẹ hoặc vừa
            step2Answer == 1 -> if (step1Answer == 0) 1.25f else 1.15f
            // Bước 2 đọc rõ cả dòng nhỏ -> Mắt sáng
            else -> if (step1Answer == 0) 1.15f else if (step1Answer == 2) 0.85f else 1.00f
        }
    }

    fun formatScaleLabel(scale: Float): String {
        return String.format(Locale.US, "Cỡ %.2fx", scale)
    }

    fun getDiagnosticCategory(recommendedScale: Float, isVi: Boolean): String {
        return when {
            recommendedScale >= 1.35f -> if (isVi) "Nên dùng chữ to để mắt thư giãn" else "Larger text recommended for eye comfort"
            recommendedScale >= 1.15f -> if (isVi) "Nên tăng nhẹ cỡ chữ khi đọc nhiều" else "Slightly larger text helps ease eye strain"
            else -> if (isVi) "Mắt nhìn rất tốt, cỡ chữ hiện tại đã vừa" else "Sharp vision, default font size is ideal"
        }
    }

    fun getDiagnosticAdvice(recommendedScale: Float, isVi: Boolean, recommendedLabel: String): String {
        return when {
            recommendedScale >= 1.35f -> if (isVi)
                "Mắt bạn đọc chữ nhỏ sẽ nhanh mỏi. Cỡ chữ $recommendedLabel giúp bạn đọc tin tức, nhắn tin thoải mái mà không phải đưa điện thoại sát mắt."
            else
                "Reading fine print strains your eyes quickly. Scale $recommendedLabel lets you read messages and news comfortably without holding the phone too close."
            recommendedScale >= 1.15f -> if (isVi)
                "Mắt bạn đọc lâu có thể hơi mỏi. Mức $recommendedLabel giúp chữ rõ nét, đọc êm mắt mà bố cục màn hình vẫn gọn gàng."
            else
                "Your eyes may tire during long reading sessions. Scale $recommendedLabel improves clarity while keeping the screen neat."
            else -> if (isVi)
                "Mắt bạn nhìn rất khỏe và rõ. Cỡ chữ $recommendedLabel là mức chuẩn tự nhiên, hiển thị trọn vẹn mọi ứng dụng."
            else
                "Your eyesight is sharp and healthy. Scale $recommendedLabel is the natural standard size for all apps."
        }
    }

    fun evaluate(step1Answer: Int, step2Answer: Int, isVi: Boolean): EyeTestDiagnosticResult {
        val scale = calculateRecommendedScale(step1Answer, step2Answer)
        val label = formatScaleLabel(scale)
        val category = getDiagnosticCategory(scale, isVi)
        val advice = getDiagnosticAdvice(scale, isVi, label)
        return EyeTestDiagnosticResult(
            recommendedScale = scale,
            recommendedLabel = label,
            category = category,
            advice = advice
        )
    }
}

data class EyeTestDiagnosticResult(
    val recommendedScale: Float,
    val recommendedLabel: String,
    val category: String,
    val advice: String
)

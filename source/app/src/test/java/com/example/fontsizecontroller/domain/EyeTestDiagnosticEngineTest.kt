package com.example.fontsizecontroller.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit Test kiểm tra thuật toán chẩn đoán thị lực và đề xuất cỡ chữ (EyeTestDiagnosticEngine).
 */
class EyeTestDiagnosticEngineTest {

    @Test
    fun calculateRecommendedScale_step2OnlyFirstRow_recommendsLargeScale() {
        // Chỉ đọc được dòng 1 (step2 = 0)
        // và thấy mỏi mắt ở bước 1 (step1 = 0) -> Đề xuất 1.45x
        val scaleHigh = EyeTestDiagnosticEngine.calculateRecommendedScale(step1Answer = 0, step2Answer = 0)
        assertEquals(1.45f, scaleHigh, 0.001f)

        // Chỉ đọc được dòng 1 (step2 = 0), bước 1 bình thường (step1 = 1) -> Đề xuất 1.35x
        val scaleNormal = EyeTestDiagnosticEngine.calculateRecommendedScale(step1Answer = 1, step2Answer = 0)
        assertEquals(1.35f, scaleNormal, 0.001f)
    }

    @Test
    fun calculateRecommendedScale_step2MediumRow_recommendsMediumScale() {
        // Đọc được dòng vừa (step2 = 1)
        val scaleTired = EyeTestDiagnosticEngine.calculateRecommendedScale(step1Answer = 0, step2Answer = 1)
        assertEquals(1.25f, scaleTired, 0.001f)

        val scaleComfort = EyeTestDiagnosticEngine.calculateRecommendedScale(step1Answer = 1, step2Answer = 1)
        assertEquals(1.15f, scaleComfort, 0.001f)
    }

    @Test
    fun calculateRecommendedScale_step2SmallestRow_recommendsStandardOrCompact() {
        // Đọc rõ cả dòng nhỏ nhất (step2 = 2)
        // Mắt cảm thấy chữ to (step1 = 2) -> Có thể thu nhỏ 0.85x
        val scaleSmall = EyeTestDiagnosticEngine.calculateRecommendedScale(step1Answer = 2, step2Answer = 2)
        assertEquals(0.85f, scaleSmall, 0.001f)

        // Bình thường thoải mái -> 1.00x chuẩn
        val scaleStandard = EyeTestDiagnosticEngine.calculateRecommendedScale(step1Answer = 1, step2Answer = 2)
        assertEquals(1.00f, scaleStandard, 0.001f)

        // Hơi mỏi mắt -> Tăng nhẹ 1.15x
        val scaleSlight = EyeTestDiagnosticEngine.calculateRecommendedScale(step1Answer = 0, step2Answer = 2)
        assertEquals(1.15f, scaleSlight, 0.001f)
    }

    @Test
    fun formatScaleLabel_formatsCorrectly() {
        val label = EyeTestDiagnosticEngine.formatScaleLabel(1.25f)
        assertEquals("Cỡ 1.25x", label)
    }

    @Test
    fun getDiagnosticCategory_vietnameseAndEnglish() {
        val catViHigh = EyeTestDiagnosticEngine.getDiagnosticCategory(1.45f, isVi = true)
        assertTrue(catViHigh.contains("chữ to"))

        val catEnHigh = EyeTestDiagnosticEngine.getDiagnosticCategory(1.45f, isVi = false)
        assertTrue(catEnHigh.contains("Larger text"))

        val catViNormal = EyeTestDiagnosticEngine.getDiagnosticCategory(1.00f, isVi = true)
        assertTrue(catViNormal.contains("Mắt nhìn rất tốt"))
    }

    @Test
    fun evaluate_returnsFullDiagnosticResult() {
        val result = EyeTestDiagnosticEngine.evaluate(step1Answer = 0, step2Answer = 0, isVi = true)
        assertEquals(1.45f, result.recommendedScale, 0.001f)
        assertEquals("Cỡ 1.45x", result.recommendedLabel)
        assertTrue(result.category.isNotBlank())
        assertTrue(result.advice.isNotBlank())
        assertTrue(result.advice.contains("1.45x"))
    }
}

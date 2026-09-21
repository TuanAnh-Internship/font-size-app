package com.example.fontsizecontroller.util

import com.example.fontsizecontroller.model.FontSizeOption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit Test toàn diện cho FontScaleMapper theo chuẩn Week 3 Day 1 Test Matrix.
 * Chạy độc lập hoàn toàn khỏi Android Runtime / JVM thuần túy.
 */
class FontScaleMapperTest {

    private val presets = listOf(
        FontSizeOption("Small", 0.85f),
        FontSizeOption("Default", 1.00f),
        FontSizeOption("Large", 1.15f),
        FontSizeOption("Extra Large", 1.30f)
    )

    @Test
    fun `0_85 maps to Small`() {
        val option = FontScaleMapper.toOption(0.85f, presets)
        assertNotNull(option)
        assertEquals("Small", option?.label)
        assertEquals("Small", FontScaleMapper.toDisplayLabel(0.85f, presets))
    }

    @Test
    fun `1_0 maps to Default`() {
        val option = FontScaleMapper.toOption(1.00f, presets)
        assertNotNull(option)
        assertEquals("Default", option?.label)
        assertEquals("Default", FontScaleMapper.toDisplayLabel(1.00f, presets))
    }

    @Test
    fun `1_15 maps to Large`() {
        val option = FontScaleMapper.toOption(1.15f, presets)
        assertNotNull(option)
        assertEquals("Large", option?.label)
        assertEquals("Large", FontScaleMapper.toDisplayLabel(1.15f, presets))
    }

    @Test
    fun `1_30 maps to Extra Large`() {
        val option = FontScaleMapper.toOption(1.30f, presets)
        assertNotNull(option)
        assertEquals("Extra Large", option?.label)
        assertEquals("Extra Large", FontScaleMapper.toDisplayLabel(1.30f, presets))
    }

    @Test
    fun `1_151 within tolerance 0_03 maps to Large`() {
        val option = FontScaleMapper.toOption(1.151f, presets)
        assertNotNull(option)
        assertEquals("Large", option?.label)
        assertEquals("Large", FontScaleMapper.toDisplayLabel(1.151f, presets))
    }

    @Test
    fun `unknown scale 1_10 becomes Custom (Samsung OEM case)`() {
        val option = FontScaleMapper.toOption(1.10f, presets)
        assertNull(option) // Không khớp preset nào trong tolerance
        val label = FontScaleMapper.toDisplayLabel(1.10f, presets)
        assertTrue("Label must start with Custom", label.startsWith("Custom"))
        assertEquals("Custom (1.10x)", label)
    }

    @Test
    fun `unknown scale 1_50 becomes Custom`() {
        val option = FontScaleMapper.toOption(1.50f, presets)
        assertNull(option)
        val label = FontScaleMapper.toDisplayLabel(1.50f, presets)
        assertEquals("Custom (1.50x)", label)
    }

    @Test
    fun `boundary check - within tolerance 1_025 maps to Default`() {
        val option = FontScaleMapper.toOption(1.025f, presets)
        assertNotNull(option)
        assertEquals("Default", option?.label)
    }

    @Test
    fun `boundary check - outside tolerance 1_05 becomes Custom`() {
        val option = FontScaleMapper.toOption(1.05f, presets)
        assertNull(option)
        val label = FontScaleMapper.toDisplayLabel(1.05f, presets)
        assertEquals("Custom (1.05x)", label)
    }
}

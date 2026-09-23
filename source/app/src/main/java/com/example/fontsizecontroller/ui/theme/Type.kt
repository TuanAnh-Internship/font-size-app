package com.example.fontsizecontroller.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.io.File

/**
 * Kiểm tra xem file font có sẵn trong hệ thống /system/fonts/ hay không
 */
fun isSystemFontPresent(fileName: String): Boolean {
    return try {
        File("/system/fonts/$fileName").exists()
    } catch (_: Exception) {
        false
    }
}

/**
 * Nạp Typeface thực từ /system/fonts/ trên thiết bị Android
 */
fun getSystemFontFamily(fileName: String, fallback: FontFamily = FontFamily.SansSerif): FontFamily {
    return try {
        val file = File("/system/fonts/$fileName")
        if (file.exists() && file.canRead()) {
            val androidTypeface = android.graphics.Typeface.createFromFile(file)
            FontFamily(androidTypeface)
        } else {
            fallback
        }
    } catch (_: Exception) {
        fallback
    }
}

/**
 * Chuyển tên font được lưu trong DataStore sang FontFamily tương ứng
 */
fun getFontFamilyFromName(fontName: String): FontFamily {
    return when (fontName) {
        "Samsung One" -> getSystemFontFamily("SEC-Regular.ttf", FontFamily.SansSerif)
        "Noto Serif" -> getSystemFontFamily("NotoSerif-Regular.ttf", FontFamily.Serif)
        "Dancing Script" -> getSystemFontFamily("DancingScript-Regular.ttf", FontFamily.Cursive)
        "Coming Soon" -> getSystemFontFamily("ComingSoon.ttf", FontFamily.Cursive)
        "Droid Sans Mono" -> getSystemFontFamily("DroidSansMono.ttf", FontFamily.Monospace)
        "Source Sans" -> getSystemFontFamily("SourceSansPro-Regular.ttf", FontFamily.SansSerif)
        "Roboto" -> FontFamily.Default
        else -> FontFamily.Default
    }
}

/**
 * Tạo Material3 Typography áp dụng FontFamily người dùng đã chọn
 */
fun createAppTypography(fontFamily: FontFamily = FontFamily.Default): Typography {
    return Typography(
        displayLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        ),
        displayMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),
        titleLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}

val Typography = createAppTypography(FontFamily.Default)
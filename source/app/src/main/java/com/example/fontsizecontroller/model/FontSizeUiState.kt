package com.example.fontsizecontroller.model

/**
 * Trạng thái kết quả của thao tác thay đổi cỡ chữ.
 */
sealed interface ApplyUiResult {
    data object Idle : ApplyUiResult
    data object PermissionRequired : ApplyUiResult
    data class Success(val scale: Float, val label: String) : ApplyUiResult
    data class Error(val message: String? = null) : ApplyUiResult
    data object Unsupported : ApplyUiResult
}

/**
 * Các màn hình trong luồng điều hướng của ứng dụng FontMaster.
 */
enum class ScreenDestination {
    ONBOARDING,
    MAIN_FONT,
    PERMISSION,
    RESULT,
    ACCESSIBILITY,
    EYE_TEST,
    FONT_GALLERY,
    SETTINGS_AND_HELP,
    UNSUPPORTED_ERROR
}

/**
 * UI State toàn diện cho toàn bộ luồng ứng dụng theo chuẩn UDF (Unidirectional Data Flow).
 */
data class FontSizeUiState(
    val isLoading: Boolean = true,
    val currentScale: Float? = null,
    val currentLabel: String = "",
    val selectedOption: FontSizeOption? = null,
    val canWriteSettings: Boolean = false,
    val isApplying: Boolean = false,
    val result: ApplyUiResult = ApplyUiResult.Idle,
    val language: AppLanguage = AppLanguage.VI,
    val isDarkMode: Boolean = false,
    val currentScreen: ScreenDestination = ScreenDestination.MAIN_FONT,
    val selectedTab: Int = 0,
    val readingMode: ReadingMode = ReadingMode.STANDARD,
    val isBoldPreview: Boolean = false,
    val showOemFallbackDialog: Boolean = false,
    val eyeTestDone: Boolean = false,
    val eyeTestResultScale: Float = 1.0f,
    val showResetDialog: Boolean = false,
    val selectedProfileId: String = "myself",
    val selectedFontName: String = "Roboto",
    val isNotificationEnabled: Boolean = true
)

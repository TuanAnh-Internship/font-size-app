package com.example.fontsizecontroller.model

/**
 * Trạng thái kết quả của thao tác thay đổi cỡ chữ.
 */
enum class FontSizeStatus {
    Idle,
    PermissionRequired,
    Success,
    Error,
    Unsupported
}

/**
 * UI State toàn diện cho màn hình quản lý cỡ chữ.
 */
data class FontSizeUiState(
    val isLoading: Boolean = true,
    val currentScale: Float? = null,
    val selectedOption: FontSizeOption? = null,
    val canWriteSettings: Boolean = false,
    val isApplying: Boolean = false,
    val status: FontSizeStatus = FontSizeStatus.Idle,
    val message: String? = null
)

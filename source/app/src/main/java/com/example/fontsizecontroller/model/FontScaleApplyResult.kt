package com.example.fontsizecontroller.model

/**
 * Kết quả phân định rõ domain result sau khi thực hiện ghi font scale vào Android System Settings.
 */
sealed interface FontScaleApplyResult {
    /**
     * Ghi thành công và đã xác minh lại giá trị (read-back verify) nằm trong dung sai cho phép (< 0.03f).
     */
    data class Success(
        val requestedScale: Float,
        val verifiedScale: Float
    ) : FontScaleApplyResult

    /**
     * Ứng dụng chưa có quyền WRITE_SETTINGS từ người dùng.
     */
    data object PermissionRequired : FontScaleApplyResult

    /**
     * Hệ thống hoặc nhà sản xuất (OEM ROM) không hỗ trợ hoặc từ chối áp dụng scale yêu cầu.
     */
    data object Unsupported : FontScaleApplyResult

    /**
     * Xảy ra ngoại lệ hoặc lỗi hệ thống không lường trước.
     */
    data class Error(val throwable: Throwable? = null) : FontScaleApplyResult
}

package com.example.fontsizecontroller.model

/**
 * Kết quả trả về sau thao tác đọc/ghi cài đặt font hệ thống.
 */
sealed class FontScaleResult {
    /** Ghi thành công và đã xác minh giá trị trên hệ thống */
    data class Success(val scale: Float) : FontScaleResult()

    /** Ứng dụng chưa được cấp quyền WRITE_SETTINGS */
    object PermissionRequired : FontScaleResult()

    /** Gặp lỗi trong quá trình thực thi hoặc verify thất bại */
    data class Error(val message: String, val throwable: Throwable? = null) : FontScaleResult()

    /** Thiết bị / OEM không hỗ trợ ghi trực tiếp FONT_SCALE */
    object Unsupported : FontScaleResult()
}

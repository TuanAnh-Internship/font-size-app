package com.example.fontsizecontroller.repository

import com.example.fontsizecontroller.model.FontScaleResult

/**
 * Interface Repository quản lý các tương tác với Android System Settings liên quan đến cỡ chữ.
 */
interface FontSettingsRepository {

    /**
     * Đọc giá trị font scale hiện tại của hệ thống (fallback 1.0f nếu lỗi).
     */
    fun getCurrentFontScale(): Float

    /**
     * Kiểm tra ứng dụng có quyền WRITE_SETTINGS hay không.
     */
    fun canWriteSettings(): Boolean

    /**
     * Áp dụng cỡ chữ mới vào hệ thống và xác thực lại giá trị sau khi ghi.
     */
    suspend fun applyFontScale(scale: Float): FontScaleResult
}

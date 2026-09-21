package com.example.fontsizecontroller.repository

import com.example.fontsizecontroller.model.FontScaleApplyResult

/**
 * Contract Repository giao tiếp trực tiếp với tầng cài đặt hệ thống Android (System Settings).
 * Tuân thủ Clean Architecture: Tách biệt hoàn toàn khỏi ViewModel và Jetpack Compose.
 */
interface SystemFontSettingsRepository {

    /**
     * Đọc giá trị FONT_SCALE từ Settings.System.
     * Trả về Result<Float> để xử lý an toàn lỗi SettingNotFoundException hoặc ngoại lệ bảo mật.
     */
    fun readFontScale(): Result<Float>

    /**
     * Kiểm tra ứng dụng đã có quyền WRITE_SETTINGS (cho phép sửa đổi cài đặt hệ thống) hay chưa.
     */
    fun canWriteSettings(): Boolean

    /**
     * Áp dụng cỡ chữ mới với quy trình nghiêm ngặt:
     * 1. Kiểm tra quyền canWriteSettings()
     * 2. Ghi FONT_SCALE qua Settings.System.putFloat()
     * 3. Đọc lại (read-back verify) và so sánh dung sai sai số nội bộ (< 0.03f)
     * 4. Bắt lỗi SecurityException, SettingNotFoundException
     */
    suspend fun applyFontScale(targetScale: Float): FontScaleApplyResult
}

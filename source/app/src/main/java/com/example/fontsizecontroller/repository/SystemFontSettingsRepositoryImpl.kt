package com.example.fontsizecontroller.repository

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.example.fontsizecontroller.model.FontScaleApplyResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs

/**
 * Triển khai cụ thể của SystemFontSettingsRepository tương tác với Android Settings.System.
 * Đảm bảo bắt đầy đủ SecurityException, SettingNotFoundException và xử lý read-back verification.
 */
class SystemFontSettingsRepositoryImpl(
    private val context: Context
) : SystemFontSettingsRepository {

    /**
     * Đọc giá trị FONT_SCALE từ Settings.System.
     * Sử dụng runCatching để tránh crash app nếu SettingNotFoundException xảy ra trên một số OEM ROM.
     */
    override fun readFontScale(): Result<Float> = runCatching {
        Settings.System.getFloat(
            context.contentResolver,
            Settings.System.FONT_SCALE
        )
    }

    /**
     * Kiểm tra quyền sửa đổi cài đặt hệ thống (WRITE_SETTINGS).
     * Bắt buộc từ Android 6.0 (API 23 Marshmallow) trở lên.
     */
    override fun canWriteSettings(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(context)
        } else {
            true
        }
    }

    /**
     * Ghi cỡ chữ mới vào hệ thống và xác thực lại (read-back verify).
     * Nếu sau khi ghi mà giá trị hệ thống không đổi hoặc lệch ngoài sai số 0.03f,
     * trả về Unsupported (dấu hiệu OEM ROM can thiệp).
     */
    override suspend fun applyFontScale(targetScale: Float): FontScaleApplyResult = withContext(Dispatchers.IO) {
        if (!canWriteSettings()) {
            return@withContext FontScaleApplyResult.PermissionRequired
        }

        return@withContext try {
            val writeOk = Settings.System.putFloat(
                context.contentResolver,
                Settings.System.FONT_SCALE,
                targetScale
            )

            if (!writeOk) {
                return@withContext FontScaleApplyResult.Error(
                    Exception("Settings.System.putFloat returned false")
                )
            }

            val verified = Settings.System.getFloat(
                context.contentResolver,
                Settings.System.FONT_SCALE
            )

            // Cập nhật cấu hình hiển thị nội bộ của ứng dụng
            try {
                val config = context.resources.configuration
                config.fontScale = targetScale
                val metrics = context.resources.displayMetrics
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, metrics)
            } catch (_: Exception) {}

            if (abs(verified - targetScale) < TOLERANCE) {
                FontScaleApplyResult.Success(
                    requestedScale = targetScale,
                    verifiedScale = verified
                )
            } else {
                FontScaleApplyResult.Unsupported
            }
        } catch (e: SecurityException) {
            FontScaleApplyResult.PermissionRequired
        } catch (e: Settings.SettingNotFoundException) {
            FontScaleApplyResult.Error(e)
        } catch (e: Exception) {
            FontScaleApplyResult.Error(e)
        }
    }

    companion object {
        private const val TOLERANCE = 0.03f
    }
}

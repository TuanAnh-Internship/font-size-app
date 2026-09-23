package com.example.fontsizecontroller.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

/**
 * BroadcastReceiver tiếp nhận lệnh đổi cỡ chữ từ các nút trên thanh thông báo.
 */
class FontSizeNotificationReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_SET_SCALE = "com.example.fontsizecontroller.ACTION_SET_SCALE"
        const val ACTION_STEP_SCALE = "com.example.fontsizecontroller.ACTION_STEP_SCALE"
        const val EXTRA_SCALE = "extra_scale"
        const val EXTRA_STEP_DELTA = "extra_step_delta"
        const val EXTRA_LABEL = "extra_label"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val canWrite = Settings.System.canWrite(context)
        if (!canWrite) {
            Toast.makeText(context, "Cần cấp quyền ghi cài đặt để đổi cỡ chữ!", Toast.LENGTH_LONG).show()
            return
        }

        val currentScale = try {
            Settings.System.getFloat(context.contentResolver, Settings.System.FONT_SCALE, 1.0f)
        } catch (_: Exception) {
            1.0f
        }

        val targetScale: Float = when (intent.action) {
            ACTION_STEP_SCALE -> {
                val delta = intent.getFloatExtra(EXTRA_STEP_DELTA, 0.15f)
                val raw = kotlin.math.round((currentScale + delta) * 100) / 100f
                raw.coerceIn(0.85f, 2.00f)
            }
            ACTION_SET_SCALE -> {
                val raw = intent.getFloatExtra(EXTRA_SCALE, 1.0f)
                raw.coerceIn(0.85f, 2.00f)
            }
            else -> return
        }

        val label = intent.getStringExtra(EXTRA_LABEL) ?: String.format(java.util.Locale.US, "%.2fx", targetScale)

        try {
            Settings.System.putFloat(context.contentResolver, Settings.System.FONT_SCALE, targetScale)
            QuickControlNotificationManager.showNotification(context, targetScale)
            Toast.makeText(context, "✓ Cỡ chữ hệ thống: ${String.format(java.util.Locale.US, "%.2fx", targetScale)}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Không thể áp dụng: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

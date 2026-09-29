package com.example.fontsizecontroller.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.example.fontsizecontroller.repository.UserPreferencesRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * BroadcastReceiver tiếp nhận sự kiện kích hoạt lịch hẹn ban đêm, khôi phục ban ngày
 * hoặc tái kích hoạt lịch khi thiết bị khởi động lại (BOOT_COMPLETED).
 */
class FontScheduleReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            FontScheduleManager.ACTION_APPLY_NIGHT_FONT -> {
                handleApplyNightFont(context, intent)
            }

            FontScheduleManager.ACTION_RESTORE_DAY_FONT -> {
                handleRestoreDayFont(context, intent)
            }

            Intent.ACTION_BOOT_COMPLETED -> {
                handleBootCompleted(context)
            }
        }
    }

    private fun handleApplyNightFont(context: Context, intent: Intent) {
        val canWrite = Settings.System.canWrite(context)
        if (!canWrite) return

        val nightScale = intent.getFloatExtra(FontScheduleManager.EXTRA_NIGHT_SCALE, 1.25f)
        val dayScale = intent.getFloatExtra(FontScheduleManager.EXTRA_DAY_SCALE, 1.00f)

        try {
            Settings.System.putFloat(context.contentResolver, Settings.System.FONT_SCALE, nightScale)

            // Cập nhật cấu hình hiển thị nội bộ
            try {
                val config = context.resources.configuration
                config.fontScale = nightScale
                val metrics = context.resources.displayMetrics
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, metrics)
            } catch (_: Exception) {}

            QuickControlNotificationManager.showNotification(context, nightScale)

            Toast.makeText(
                context,
                "🌙 Chế độ đọc ban đêm: Đã tăng cỡ chữ ${String.format(Locale.US, "%.2f", nightScale)}x để chống mỏi mắt",
                Toast.LENGTH_LONG
            ).show()

            // Lên lịch cho chu kỳ tiếp theo
            FontScheduleManager.scheduleNightMode(context, nightScale = nightScale, dayScale = dayScale)
        } catch (_: Exception) {}
    }

    private fun handleRestoreDayFont(context: Context, intent: Intent) {
        val canWrite = Settings.System.canWrite(context)
        if (!canWrite) return

        val dayScale = intent.getFloatExtra(FontScheduleManager.EXTRA_DAY_SCALE, 1.00f)
        val nightScale = intent.getFloatExtra(FontScheduleManager.EXTRA_NIGHT_SCALE, 1.25f)

        try {
            Settings.System.putFloat(context.contentResolver, Settings.System.FONT_SCALE, dayScale)

            // Cập nhật cấu hình hiển thị nội bộ
            try {
                val config = context.resources.configuration
                config.fontScale = dayScale
                val metrics = context.resources.displayMetrics
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, metrics)
            } catch (_: Exception) {}

            QuickControlNotificationManager.showNotification(context, dayScale)

            Toast.makeText(
                context,
                "☀️ Chào buổi sáng: Cỡ chữ đã khôi phục về ${String.format(Locale.US, "%.2f", dayScale)}x",
                Toast.LENGTH_LONG
            ).show()

            // Lên lịch cho chu kỳ tiếp theo
            FontScheduleManager.scheduleNightMode(context, nightScale = nightScale, dayScale = dayScale)
        } catch (_: Exception) {}
    }

    private fun handleBootCompleted(context: Context) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = UserPreferencesRepositoryImpl(context)
                val prefs = repository.userPreferencesFlow.firstOrNull()
                if (prefs?.isNightScheduleEnabled == true) {
                    FontScheduleManager.scheduleNightMode(
                        context = context,
                        nightScale = prefs.nightScheduleScale,
                        dayScale = prefs.lastAppliedScale ?: 1.00f,
                        startHour = prefs.nightScheduleStartHour,
                        startMinute = prefs.nightScheduleStartMinute,
                        endHour = prefs.nightScheduleEndHour,
                        endMinute = prefs.nightScheduleEndMinute
                    )
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}

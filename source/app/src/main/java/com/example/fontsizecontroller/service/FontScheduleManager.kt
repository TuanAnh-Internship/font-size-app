package com.example.fontsizecontroller.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

/**
 * Trình quản lý lịch hẹn tự động phóng to cỡ chữ ban đêm (Scheduled Font Scale).
 * Sử dụng AlarmManager chính xác với cơ chế cho phép kích hoạt khi máy đang ở chế độ chờ (AllowWhileIdle).
 */
object FontScheduleManager {

    const val ACTION_APPLY_NIGHT_FONT = "com.example.fontsizecontroller.ACTION_APPLY_NIGHT_FONT"
    const val ACTION_RESTORE_DAY_FONT = "com.example.fontsizecontroller.ACTION_RESTORE_DAY_FONT"

    const val EXTRA_NIGHT_SCALE = "extra_night_scale"
    const val EXTRA_DAY_SCALE = "extra_day_scale"

    private const val REQUEST_CODE_NIGHT = 2001
    private const val REQUEST_CODE_DAY = 2002

    /**
     * Kích hoạt lịch hẹn phóng to cỡ chữ ban đêm và khôi phục vào sáng hôm sau.
     * Mặc định: 20:00 tối phóng to (1.25x - 1.35x), 07:00 sáng khôi phục (1.00x).
     */
    fun scheduleNightMode(
        context: Context,
        nightScale: Float = 1.25f,
        dayScale: Float = 1.00f,
        startHour: Int = 20,
        startMinute: Int = 0,
        endHour: Int = 7,
        endMinute: Int = 0
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        // 1. Lên lịch hẹn bắt đầu ban đêm
        val nightIntent = Intent(context, FontScheduleReceiver::class.java).apply {
            action = ACTION_APPLY_NIGHT_FONT
            putExtra(EXTRA_NIGHT_SCALE, nightScale)
            putExtra(EXTRA_DAY_SCALE, dayScale)
        }
        val nightPendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_NIGHT,
            nightIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val nightTriggerTime = calculateNextTriggerMillis(startHour, startMinute)
        setAlarmExactOrIdle(alarmManager, nightTriggerTime, nightPendingIntent)

        // 2. Lên lịch hẹn khôi phục ban ngày
        val dayIntent = Intent(context, FontScheduleReceiver::class.java).apply {
            action = ACTION_RESTORE_DAY_FONT
            putExtra(EXTRA_DAY_SCALE, dayScale)
            putExtra(EXTRA_NIGHT_SCALE, nightScale)
        }
        val dayPendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_DAY,
            dayIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dayTriggerTime = calculateNextTriggerMillis(endHour, endMinute)
        setAlarmExactOrIdle(alarmManager, dayTriggerTime, dayPendingIntent)
    }

    /**
     * Hủy bỏ toàn bộ lịch hẹn ban đêm đang chạy ngầm.
     */
    fun cancelNightMode(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val nightIntent = Intent(context, FontScheduleReceiver::class.java).apply {
            action = ACTION_APPLY_NIGHT_FONT
        }
        val nightPendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_NIGHT,
            nightIntent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (nightPendingIntent != null) {
            alarmManager.cancel(nightPendingIntent)
            nightPendingIntent.cancel()
        }

        val dayIntent = Intent(context, FontScheduleReceiver::class.java).apply {
            action = ACTION_RESTORE_DAY_FONT
        }
        val dayPendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_DAY,
            dayIntent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (dayPendingIntent != null) {
            alarmManager.cancel(dayPendingIntent)
            dayPendingIntent.cancel()
        }
    }

    /**
     * Tính toán mốc thời gian kích hoạt kế tiếp (hôm nay hoặc ngày mai).
     */
    internal fun calculateNextTriggerMillis(hour: Int, minute: Int, nowMillis: Long = System.currentTimeMillis()): Long {
        val now = Calendar.getInstance().apply { timeInMillis = nowMillis }
        val target = Calendar.getInstance().apply {
            timeInMillis = nowMillis
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        return target.timeInMillis
    }

    private fun setAlarmExactOrIdle(
        alarmManager: AlarmManager,
        triggerAtMillis: Long,
        pendingIntent: PendingIntent
    ) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            // Fallback nếu quyền SCHEDULE_EXACT_ALARM bị hạn chế
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
    }
}

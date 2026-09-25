package com.example.fontsizecontroller.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.fontsizecontroller.MainActivity
import com.example.fontsizecontroller.R

/**
 * Trình quản lý tiện ích điều khiển cỡ chữ thông minh trên thanh thông báo (vuốt từ trên xuống).
 * Cung cấp giao diện thanh trượt trực quan (Visual Slider Track) với các mốc chạm nhanh từ 0.85x đến 2.00x.
 */
object QuickControlNotificationManager {
    const val CHANNEL_ID = "font_size_quick_controls_v3"
    const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Điều khiển cỡ chữ nhanh"
            val descriptionText = "Tiện ích chỉnh cỡ chữ trực quan trên thanh thông báo khi vuốt từ trên xuống"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                setShowBadge(false)
                enableVibration(false)
                setSound(null, null)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createScalePendingIntent(context: Context, scale: Float, requestCode: Int): PendingIntent {
        val intent = Intent(context, FontSizeNotificationReceiver::class.java).apply {
            action = FontSizeNotificationReceiver.ACTION_SET_SCALE
            putExtra(FontSizeNotificationReceiver.EXTRA_SCALE, scale)
            putExtra(FontSizeNotificationReceiver.EXTRA_LABEL, String.format(java.util.Locale.US, "%.2fx", scale))
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createStepPendingIntent(context: Context, delta: Float, requestCode: Int): PendingIntent {
        val intent = Intent(context, FontSizeNotificationReceiver::class.java).apply {
            action = FontSizeNotificationReceiver.ACTION_STEP_SCALE
            putExtra(FontSizeNotificationReceiver.EXTRA_STEP_DELTA, delta)
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun buildNotification(context: Context, currentScale: Float? = null): android.app.Notification {
        createNotificationChannel(context)

        val scale = currentScale ?: try {
            Settings.System.getFloat(context.contentResolver, Settings.System.FONT_SCALE, 1.0f)
        } catch (_: Exception) {
            1.0f
        }

        val scaleFormatted = String.format(java.util.Locale.US, "%.2fx", scale)
        // Tính % thanh trượt từ 0.85x (0%) đến 2.00x (100%)
        val progressPercent = (((scale - 0.85f) / (2.00f - 0.85f)) * 100).toInt().coerceIn(0, 100)

        // PendingIntent mở app khi chạm vào thông báo
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intents cho 4 mốc chọn nhanh thiết yếu
        val pScale85 = createScalePendingIntent(context, 0.85f, 201)
        val pScale100 = createScalePendingIntent(context, 1.00f, 202)
        val pScale125 = createScalePendingIntent(context, 1.25f, 203)
        val pScale150 = createScalePendingIntent(context, 1.50f, 204)

        // Intents cho 2 nút tinh chỉnh tăng / giảm
        val pStepDown = createStepPendingIntent(context, -0.15f, 210)
        val pStepUp = createStepPendingIntent(context, 0.15f, 211)

        val activeBg = R.drawable.bg_track_node_active
        val normalBg = R.drawable.bg_track_node
        val activeTextColor = Color.WHITE
        val normalTextColor = Color.parseColor("#CBD5E1")

        // ── 1. GIAO DIỆN THU GỌN (Collapsed View: Gọn gàng, 4 mốc chuẩn và nút - / +) ──
        val collapsedViews = RemoteViews(context.packageName, R.layout.notification_quick_control_collapsed).apply {
            setTextViewText(R.id.tv_collapsed_scale, scaleFormatted)
            setProgressBar(R.id.pb_collapsed_track, 100, progressPercent, false)

            setOnClickPendingIntent(R.id.btn_collapsed_down, pStepDown)
            setOnClickPendingIntent(R.id.btn_collapsed_up, pStepUp)

            setOnClickPendingIntent(R.id.btn_collapsed_85, pScale85)
            setOnClickPendingIntent(R.id.btn_collapsed_100, pScale100)
            setOnClickPendingIntent(R.id.btn_collapsed_125, pScale125)
            setOnClickPendingIntent(R.id.btn_collapsed_150, pScale150)

            val highlightNode = when {
                scale <= 0.92f -> R.id.btn_collapsed_85
                scale <= 1.12f -> R.id.btn_collapsed_100
                scale <= 1.37f -> R.id.btn_collapsed_125
                else -> R.id.btn_collapsed_150
            }

            val collapsedNodes = listOf(
                R.id.btn_collapsed_85,
                R.id.btn_collapsed_100,
                R.id.btn_collapsed_125,
                R.id.btn_collapsed_150
            )
            for (nodeId in collapsedNodes) {
                if (nodeId == highlightNode) {
                    setInt(nodeId, "setBackgroundResource", activeBg)
                    setTextColor(nodeId, activeTextColor)
                } else {
                    setInt(nodeId, "setBackgroundResource", normalBg)
                    setTextColor(nodeId, normalTextColor)
                }
            }
        }

        // ── 2. GIAO DIỆN MỞ RỘNG (Expanded View: 4 mốc lớn và 2 nút [-] Giảm / [+] Tăng) ──
        val expandedViews = RemoteViews(context.packageName, R.layout.notification_quick_control_expanded).apply {
            setTextViewText(R.id.tv_expanded_scale_badge, scaleFormatted)
            setProgressBar(R.id.pb_scale_track, 100, progressPercent, false)

            setOnClickPendingIntent(R.id.btn_node_85, pScale85)
            setOnClickPendingIntent(R.id.btn_node_100, pScale100)
            setOnClickPendingIntent(R.id.btn_node_125, pScale125)
            setOnClickPendingIntent(R.id.btn_node_150, pScale150)

            val highlightExpandedNode = when {
                scale <= 0.92f -> R.id.btn_node_85
                scale <= 1.12f -> R.id.btn_node_100
                scale <= 1.37f -> R.id.btn_node_125
                else -> R.id.btn_node_150
            }

            val expandedNodes = listOf(
                R.id.btn_node_85,
                R.id.btn_node_100,
                R.id.btn_node_125,
                R.id.btn_node_150
            )
            for (nodeId in expandedNodes) {
                if (nodeId == highlightExpandedNode) {
                    setInt(nodeId, "setBackgroundResource", activeBg)
                    setTextColor(nodeId, activeTextColor)
                } else {
                    setInt(nodeId, "setBackgroundResource", normalBg)
                    setTextColor(nodeId, normalTextColor)
                }
            }

            setOnClickPendingIntent(R.id.btn_step_down, pStepDown)
            setOnClickPendingIntent(R.id.btn_step_up, pStepUp)
        }

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_font)
            .setContentTitle("⚡ Cỡ chữ hệ thống: $scaleFormatted")
            .setContentText("Kéo thanh thông báo để chỉnh nhanh từ 0.85x đến 1.50x")
            .setCustomContentView(collapsedViews)
            .setCustomBigContentView(expandedViews)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setShowWhen(false)
            .setOnlyAlertOnce(true)
            .setContentIntent(openAppPendingIntent)
            .build()
    }

    fun isNotificationPermissionGranted(context: Context): Boolean {
        return androidx.core.app.NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun showNotification(context: Context, currentScale: Float? = null): Boolean {
        return try {
            val notification = buildNotification(context, currentScale)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
            true
        } catch (_: Exception) {
            false
        }
    }
}

package com.example.fontsizecontroller.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import com.example.fontsizecontroller.MainActivity
import java.util.Locale

/**
 * Quick Settings Tile cho phép người dùng vuốt bảng Cài Đặt Nhanh từ mép trên màn hình
 * (cạnh biểu tượng Wi-Fi / Bluetooth) để chạm 1 lần xoay vòng nhanh cỡ chữ:
 * 1.00x ➔ 1.25x ➔ 1.50x.
 */
class FontSizeTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onClick() {
        super.onClick()

        val canWrite = Settings.System.canWrite(this)
        if (!canWrite) {
            Toast.makeText(this, "Vui lòng cấp quyền sửa cài đặt hệ thống để dùng phím tắt FontM", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val pendingIntent = android.app.PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )
                startActivityAndCollapse(pendingIntent)
            } else {
                @Suppress("DEPRECATION")
                startActivityAndCollapse(intent)
            }
            return
        }

        val currentScale = try {
            Settings.System.getFloat(contentResolver, Settings.System.FONT_SCALE, 1.0f)
        } catch (_: Exception) {
            1.0f
        }

        // Xoay vòng 3 mốc tiện dụng: 1.00x -> 1.25x -> 1.50x -> 1.00x
        val targetScale = when {
            currentScale < 1.15f -> 1.25f
            currentScale < 1.40f -> 1.50f
            else -> 1.00f
        }

        try {
            Settings.System.putFloat(contentResolver, Settings.System.FONT_SCALE, targetScale)

            // Cập nhật cấu hình hiển thị nội bộ
            try {
                val config = resources.configuration
                config.fontScale = targetScale
                val metrics = resources.displayMetrics
                @Suppress("DEPRECATION")
                resources.updateConfiguration(config, metrics)
            } catch (_: Exception) {}

            // Rung nhẹ xác nhận thao tác
            vibrateFeedback()

            // Đồng bộ khay thông báo nếu đang hiển thị
            QuickControlNotificationManager.showNotification(this, targetScale)

            Toast.makeText(
                this,
                "✓ Cỡ chữ hệ thống: ${String.format(Locale.US, "%.2f", targetScale)}x",
                Toast.LENGTH_SHORT
            ).show()

            updateTileState(targetScale)
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi áp dụng cỡ chữ: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTileState(overrideScale: Float? = null) {
        val tile = qsTile ?: return

        val canWrite = Settings.System.canWrite(this)
        if (!canWrite) {
            tile.state = Tile.STATE_INACTIVE
            tile.label = "FontM"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                tile.subtitle = "Cần cấp quyền"
            }
            tile.updateTile()
            return
        }

        val scale = overrideScale ?: try {
            Settings.System.getFloat(contentResolver, Settings.System.FONT_SCALE, 1.0f)
        } catch (_: Exception) {
            1.0f
        }

        tile.state = Tile.STATE_ACTIVE
        tile.label = "${String.format(Locale.US, "%.2f", scale)}x"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tile.subtitle = "Chạm để đổi (1.0x-1.25x-1.5x)"
        }
        tile.updateTile()
    }

    private fun vibrateFeedback() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(30)
            }
        } catch (_: Exception) {}
    }
}

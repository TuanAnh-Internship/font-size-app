package com.example.fontsizecontroller.util

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.os.UserManager
import android.provider.Settings
import java.util.Locale

/**
 * Tiện ích kiểm tra và xử lý tương thích các dòng máy OEM (Xiaomi HyperOS/MIUI, Samsung OneUI,
 * Oppo ColorOS, Vivo FuntouchOS) và chính sách bảo mật doanh nghiệp (Device Policy Manager / MDM).
 */
object OemCompatibilityHelper {

    /**
     * Tên hãng sản xuất chuẩn hóa
     */
    val manufacturer: String = (Build.MANUFACTURER ?: "UNKNOWN").uppercase(Locale.US)
    val brand: String = (Build.BRAND ?: "UNKNOWN").uppercase(Locale.US)
    val model: String = Build.MODEL ?: "Device"

    val isXiaomi: Boolean = manufacturer.contains("XIAOMI") || brand.contains("REDMI") || brand.contains("POCO")
    val isSamsung: Boolean = manufacturer.contains("SAMSUNG")
    val isOppoOrRealme: Boolean = manufacturer.contains("OPPO") || brand.contains("REALME") || manufacturer.contains("ONEPLUS")
    val isVivo: Boolean = manufacturer.contains("VIVO") || brand.contains("IQOO")
    val isHuawei: Boolean = manufacturer.contains("HUAWEI") || brand.contains("HONOR")

    /**
     * Tên hiển thị thân thiện của hãng sản xuất
     */
    fun getFriendlyDeviceName(): String {
        return when {
            isXiaomi -> "Xiaomi / Redmi (HyperOS / MIUI)"
            isSamsung -> "Samsung (One UI)"
            isOppoOrRealme -> "Oppo / Realme (ColorOS)"
            isVivo -> "Vivo (Funtouch OS / OriginOS)"
            isHuawei -> "Huawei / Honor (EMUI / MagicOS)"
            else -> {
                val mfg = (Build.MANUFACTURER ?: "Android").replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString()
                }
                "$mfg $model"
            }
        }
    }

    /**
     * Kiểm tra ứng dụng đã được miễn trừ tối ưu hóa pin (Battery Optimization Whitelist) chưa.
     * Cần thiết để đảm bảo tính năng hẹn giờ ban đêm (Scheduled Font Scale) và phím tắt thông báo không bị hệ thống tắt ngầm.
     */
    fun isBatteryOptimizationIgnored(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager ?: return true
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    /**
     * Mở hộp thoại yêu cầu miễn trừ tối ưu pin trực tiếp cho ứng dụng.
     */
    fun requestIgnoreBatteryOptimization(context: Context): Boolean {
        return try {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:${context.packageName}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            openBatteryOptimizationSettings(context)
        }
    }

    /**
     * Fallback mở màn hình danh sách Tối ưu hóa pin hệ thống.
     */
    fun openBatteryOptimizationSettings(context: Context): Boolean {
        return try {
            val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            openAppDetailsSettings(context)
        }
    }

    /**
     * Mở màn hình quản lý tự khởi chạy / chạy ngầm đặc thù của từng hãng máy (OEM Autostart / Background Manager).
     */
    fun openOemAutostartSettings(context: Context): Boolean {
        val intents = mutableListOf<Intent>()

        when {
            isXiaomi -> {
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
                    )
                )
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsConfigActivity")
                    )
                )
            }

            isSamsung -> {
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")
                    )
                )
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.samsung.android.sm", "com.samsung.android.sm.ui.battery.BatteryActivity")
                    )
                )
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.battery.BatteryActivity")
                    )
                )
            }

            isOppoOrRealme -> {
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")
                    )
                )
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")
                    )
                )
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")
                    )
                )
            }

            isVivo -> {
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")
                    )
                )
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")
                    )
                )
            }

            isHuawei -> {
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")
                    )
                )
                intents.add(
                    Intent().setComponent(
                        ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")
                    )
                )
            }
        }

        for (intent in intents) {
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                return true
            } catch (_: Exception) {
                // Thử intent tiếp theo nếu máy chưa hỗ trợ component này
            }
        }

        return openAppDetailsSettings(context)
    }

    /**
     * Mở trang chi tiết ứng dụng trong Cài đặt hệ thống (App Info).
     */
    fun openAppDetailsSettings(context: Context): Boolean {
        return try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Kiểm tra xem thiết bị có đang bị giới hạn bởi chính sách doanh nghiệp (MDM / Device Policy) hay không.
     */
    fun isDevicePolicyRestricted(context: Context): Boolean {
        return try {
            val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as? DevicePolicyManager
            val userManager = context.getSystemService(Context.USER_SERVICE) as? UserManager
            val hasUserRestriction = userManager?.hasUserRestriction(UserManager.DISALLOW_CONFIG_SCREEN_TIMEOUT) == true
            val isDeviceOwner = dpm?.isDeviceOwnerApp(context.packageName) == false && (dpm.activeAdmins?.isNotEmpty() == true)
            hasUserRestriction || isDeviceOwner
        } catch (_: Exception) {
            false
        }
    }
}

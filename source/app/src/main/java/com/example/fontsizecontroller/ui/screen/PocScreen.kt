package com.example.fontsizecontroller.ui.screen

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

private const val TAG = "FontSizePOC"

/**
 * Task 2: Đọc Settings.System.FONT_SCALE
 */
fun readSystemFontScale(context: Context): Float {
    return try {
        Settings.System.getFloat(
            context.contentResolver,
            Settings.System.FONT_SCALE,
            1.0f
        )
    } catch (e: Exception) {
        Log.e(TAG, "Lỗi đọc Settings.System.FONT_SCALE", e)
        1.0f
    }
}

/**
 * Task 3: Đọc Configuration.fontScale
 */
fun readConfigurationFontScale(context: Context): Float {
    return context.resources.configuration.fontScale
}

/**
 * Task 4: Kiểm tra capability canWrite (Settings.System.canWrite)
 */
fun checkCanWrite(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.System.canWrite(context)
    } else {
        true
    }
}

/**
 * Task 5: Mở màn hình Cài đặt cấp quyền WRITE_SETTINGS kèm fallback
 */
fun openManageWriteSettings(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val intent = Intent(
            Settings.ACTION_MANAGE_WRITE_SETTINGS,
            Uri.parse("package:${context.packageName}")
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            Log.d(TAG, "Mở ACTION_MANAGE_WRITE_SETTINGS cho package ${context.packageName}")
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "ActivityNotFoundException - Fallback mở cài đặt tổng", e)
            Toast.makeText(context, "Không thể mở trang cấp quyền trực tiếp, đang mở Cài đặt chung...", Toast.LENGTH_LONG).show()
            val fallbackIntent = Intent(Settings.ACTION_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallbackIntent)
        }
    } else {
        Toast.makeText(context, "Android < 6.0 không cần cấp quyền này thủ công", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Thử ghi FONT_SCALE vào hệ thống và đọc lại để verify
 */
fun writeAndVerifyFontScale(context: Context, targetScale: Float): Pair<Boolean, String> {
    if (!checkCanWrite(context)) {
        return Pair(false, "Chưa được cấp quyền WRITE_SETTINGS!")
    }
    return try {
        Log.d(TAG, "Bắt đầu ghi Settings.System.FONT_SCALE = $targetScale")
        val isPutOk = Settings.System.putFloat(
            context.contentResolver,
            Settings.System.FONT_SCALE,
            targetScale
        )
        val readBack = readSystemFontScale(context)
        val isVerified = Math.abs(readBack - targetScale) < 0.01f
        Log.d(TAG, "Kết quả ghi: putOk=$isPutOk, readBack=$readBack, verified=$isVerified")
        if (isVerified) {
            Pair(true, "Ghi thành công và đã verify: $readBack")
        } else {
            Pair(false, "Đã ghi nhưng đọc lại thấy giá trị không đổi ($readBack) - Có thể bị OEM chặn!")
        }
    } catch (e: Exception) {
        Log.e(TAG, "Lỗi khi ghi Settings.System.FONT_SCALE", e)
        Pair(false, "Lỗi ngoại lệ: ${e.localizedMessage}")
    }
}

/**
 * Task 6: Lắng nghe vòng đời onResume để tự động re-check
 */
@Composable
fun ObserveOnResume(onResume: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                Log.d(TAG, "Lifecycle ON_RESUME: Re-check canWrite và FontScale")
                onResume()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

/**
 * Task 1: Màn hình POC hoàn chỉnh
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PocScreen(
    onNavigateBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // State đo lường
    var systemFontScale by remember { mutableStateOf(readSystemFontScale(context)) }
    var configFontScale by remember { mutableStateOf(readConfigurationFontScale(context)) }
    var canWrite by remember { mutableStateOf(checkCanWrite(context)) }
    var testResultMsg by remember { mutableStateOf<String?>(null) }

    fun refreshData() {
        systemFontScale = readSystemFontScale(context)
        configFontScale = readConfigurationFontScale(context)
        canWrite = checkCanWrite(context)
        Log.d(TAG, "Data refreshed -> System: $systemFontScale, Config: $configFontScale, canWrite: $canWrite")
    }

    // Tự động re-check khi quay lại app
    ObserveOnResume {
        refreshData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("POC: Font Scale & Permission") },
                navigationIcon = {
                    if (onNavigateBack != null) {
                        TextButton(onClick = onNavigateBack) {
                            Text("Quay lại")
                        }
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card hiển thị thông số thiết bị
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📱 Thông tin thiết bị",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Model: ${Build.MANUFACTURER.uppercase()} ${Build.MODEL} (Android ${Build.VERSION.RELEASE}, API ${Build.VERSION.SDK_INT})")
                }
            }

            // Card đo lường thời gian thực
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (canWrite) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📊 Giá trị đo được thực tế",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Settings.System.FONT_SCALE: $systemFontScale",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "• Configuration.fontScale: $configFontScale",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    val isMatch = Math.abs(systemFontScale - configFontScale) < 0.01f
                    Text(
                        text = "• So sánh (Compare): ${if (isMatch) "Giống nhau (Same)" else "Khác nhau (Different)"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• canWrite: $canWrite ${if (canWrite) "✅ (ĐÃ CẤP QUYỀN)" else "❌ (CHƯA CÓ QUYỀN)"}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (canWrite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }

            // Các nút hành động chính
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { refreshData() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Refresh")
                }

                Button(
                    onClick = { openManageWriteSettings(context) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (canWrite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(1.5f)
                ) {
                    Text(if (canWrite) "Quản lý quyền" else "Cấp WRITE_SETTINGS")
                }
            }

            // Khu vực kiểm thử ghi trực tiếp (Write test)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "✍️ Kiểm thử ghi trực tiếp (Write Test)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Chỉ hoạt động khi canWrite = true. Sẽ ghi thử vào hệ thống và đọc lại để verify.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                val (ok, msg) = writeAndVerifyFontScale(context, 1.0f)
                                testResultMsg = msg
                                refreshData()
                            },
                            enabled = canWrite,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Ghi 1.00x")
                        }

                        FilledTonalButton(
                            onClick = {
                                val (ok, msg) = writeAndVerifyFontScale(context, 1.15f)
                                testResultMsg = msg
                                refreshData()
                            },
                            enabled = canWrite,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Ghi 1.15x")
                        }

                        FilledTonalButton(
                            onClick = {
                                val (ok, msg) = writeAndVerifyFontScale(context, 1.30f)
                                testResultMsg = msg
                                refreshData()
                            },
                            enabled = canWrite,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Ghi 1.30x")
                        }
                    }

                    if (testResultMsg != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = testResultMsg ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

package com.example.fontsizecontroller.ui.component

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppStrings
import com.example.fontsizecontroller.util.OemCompatibilityHelper

/**
 * Thẻ chẩn đoán và cấu hình tương thích hệ thống theo từng hãng sản xuất (OEM Diagnostics Card).
 */
@Composable
fun OemDiagnosticsCard(
    isVi: Boolean,
    strings: AppStrings,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // KHỐI 1: HƯỚNG DẪN CÀI ĐẶT THEO HÃNG MÁY (OEM ACCORDION)
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = strings.sectionOemGuidance,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Nút mở thẳng Settings màn hình của máy
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            val fallbackIntent = Intent(Settings.ACTION_SETTINGS).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(fallbackIntent)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = strings.btnOpenDisplaySettings, fontWeight = FontWeight.Bold)
                }

                // Danh sách hướng dẫn các hãng
                OemGuideAccordionItem(
                    title = strings.oemSamsungTitle,
                    content = strings.oemSamsungGuide,
                    accentColor = Color(0xFF1E88E5)
                )
                OemGuideAccordionItem(
                    title = strings.oemXiaomiTitle,
                    content = strings.oemXiaomiGuide,
                    accentColor = Color(0xFFFF6D00)
                )
                OemGuideAccordionItem(
                    title = strings.oemOppoTitle,
                    content = strings.oemOppoGuide,
                    accentColor = Color(0xFF00897B)
                )
            }
        }

        // KHỐI 2: TỐI ƯU HÓA PIN & TỰ KHỞI CHẠY (OEM BATTERY WHITELIST)
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val isBatteryIgnored = remember {
                    OemCompatibilityHelper.isBatteryOptimizationIgnored(context)
                }
                val deviceName = remember {
                    OemCompatibilityHelper.getFriendlyDeviceName()
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isVi) "Chạy Ngầm & Chống Tắt Ứng Dụng" else "Background & Battery Whitelist",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${if (isVi) "Thiết bị:" else "Device:"} $deviceName",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // Badge trạng thái pin
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isBatteryIgnored) Color(0xFF2E7D32).copy(alpha = 0.12f)
                            else Color(0xFFED6C02).copy(alpha = 0.12f)
                        )
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isBatteryIgnored) Icons.Default.Check else Icons.Default.Info,
                            contentDescription = null,
                            tint = if (isBatteryIgnored) Color(0xFF2E7D32) else Color(0xFFED6C02),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBatteryIgnored) {
                                if (isVi) "✓ Đã cho phép chạy ngầm (Lịch hẹn & thông báo hoạt động ổn định nhất)"
                                else "✓ Battery optimization ignored (Alarms & shortcuts work reliably)"
                            } else {
                                if (isVi) "⚠️ Đang bị tối ưu pin (Hãng máy có thể tắt ngầm lịch hẹn cỡ chữ ban đêm)"
                                else "⚠️ Battery restricted (ROM may kill background night schedules)"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isBatteryIgnored) Color(0xFF1B5E20) else Color(0xFFE65100),
                            lineHeight = 16.sp
                        )
                    }
                }

                Text(
                    text = if (isVi)
                        "Các dòng máy Xiaomi HyperOS/MIUI, Samsung One UI, Oppo ColorOS có cơ chế tiết kiệm pin rất mạnh. Để tính năng hẹn giờ ban đêm và thanh điều khiển hoạt động chính xác 100%, bạn nên cho phép FontM chạy nền không hạn chế."
                    else
                        "Aggressive OEM battery savers (Xiaomi, Samsung, Oppo) can kill background schedulers. Whitelist FontM to ensure night reading schedules activate on time.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Nút yêu cầu bỏ tối ưu pin
                    Button(
                        onClick = {
                            OemCompatibilityHelper.requestIgnoreBatteryOptimization(context)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = if (isVi) "Bỏ Tối Ưu Pin ↗" else "Unrestrict Battery ↗",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Nút mở quản lý tự khởi chạy
                    OutlinedButton(
                        onClick = {
                            val opened = OemCompatibilityHelper.openOemAutostartSettings(context)
                            if (!opened) {
                                Toast.makeText(context, if (isVi) "Đã mở thông tin ứng dụng" else "Opened App Info", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isVi) "Quản Lý Khởi Chạy ↗" else "Autostart Manager ↗",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OemGuideAccordionItem(
    title: String,
    content: String,
    accentColor: Color
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { isExpanded = !isExpanded },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(accentColor)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 8.dp, start = 20.dp)) {
                    Text(
                        text = content,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }
    }
}

package com.example.fontsizecontroller.ui.component

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.ui.theme.PurpleAccent
import com.example.fontsizecontroller.util.OemCompatibilityHelper

/**
 * Thẻ cấu hình hẹn giờ tự động phóng to chữ ban đêm (Scheduled Font Scale Card).
 */
@Composable
fun NightScheduleCard(
    isNightScheduleEnabled: Boolean,
    nightScheduleScale: Float,
    isVi: Boolean,
    onToggleNightSchedule: (Boolean, Float) -> Unit,
    onSetNightScheduleScale: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PurpleAccent.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DarkMode,
                            contentDescription = null,
                            tint = PurpleAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isVi) "Tự phóng to chữ ban đêm" else "Auto Larger Text at Night",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isVi) "Từ 20:00 tối ➔ 07:00 sáng hôm sau" else "8:00 PM ➔ 7:00 AM daily",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Switch(
                    checked = isNightScheduleEnabled,
                    onCheckedChange = { checked ->
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onToggleNightSchedule(checked, nightScheduleScale)
                        Toast.makeText(
                            context,
                            if (checked) {
                                if (isVi) "Đã bật: Tự phóng to chữ ${nightScheduleScale}x sau 20:00 tối" else "Enabled: Auto-scale ${nightScheduleScale}x at 8:00 PM"
                            } else {
                                if (isVi) "Đã tắt hẹn giờ ban đêm" else "Disabled night reading schedule"
                            },
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = PurpleAccent
                    )
                )
            }

            AnimatedVisibility(
                visible = isNightScheduleEnabled,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(PurpleAccent.copy(alpha = 0.08f))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = if (isVi)
                                "🌙 Buổi tối đọc trong phòng tối mắt dễ mỏi. Ứng dụng sẽ tự phóng to chữ lên mức bạn chọn và khôi phục lại bình thường vào sáng hôm sau."
                            else
                                "🌙 Reading at night strains eyes easily. Font size will enlarge automatically to your choice and restore to default next morning.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Pair(1.20f, if (isVi) "Lớn nhẹ\n1.20x" else "Mild\n1.20x"),
                            Pair(1.25f, if (isVi) "Vừa mắt\n1.25x" else "Medium\n1.25x"),
                            Pair(1.35f, if (isVi) "Rất rõ\n1.35x" else "Large\n1.35x")
                        ).forEach { (scale, label) ->
                            val isSelected = Math.abs(nightScheduleScale - scale) < 0.02f
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) PurpleAccent.copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) PurpleAccent else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        onSetNightScheduleScale(scale)
                                        onToggleNightSchedule(true, scale)
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) PurpleAccent else MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

                    // Khuyến nghị bỏ tối ưu pin để đảm bảo Alarm kích hoạt đúng giờ
                    val isBatteryIgnored = OemCompatibilityHelper.isBatteryOptimizationIgnored(context)
                    if (!isBatteryIgnored) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFED6C02).copy(alpha = 0.10f))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isVi)
                                    "⚡ Bỏ tối ưu pin để hẹn giờ chuẩn 100%"
                                else
                                    "⚡ Unrestrict battery for 100% precision",
                                fontSize = 11.sp,
                                color = Color(0xFFE65100),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isVi) "Bật ngay" else "Enable",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurpleAccent,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PurpleAccent.copy(alpha = 0.15f))
                                    .clickable {
                                        OemCompatibilityHelper.requestIgnoreBatteryOptimization(context)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

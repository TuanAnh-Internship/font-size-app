package com.example.fontsizecontroller.ui.component

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.ui.theme.PurpleAccent

@Composable
fun OemFallbackDialog(
    language: AppLanguage,
    onOpenDisplaySettings: () -> Unit,
    onDismiss: () -> Unit,
    isMdmRestricted: Boolean = false,
    reason: String? = null
) {
    val isVi = language == AppLanguage.VI
    val deviceName = remember { com.example.fontsizecontroller.util.OemCompatibilityHelper.getFriendlyDeviceName() }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        if (isMdmRestricted) MaterialTheme.colorScheme.errorContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "OEM / MDM Notice",
                    tint = if (isMdmRestricted) MaterialTheme.colorScheme.error else PurpleAccent,
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        title = {
            Text(
                text = if (isMdmRestricted) {
                    if (isVi) "Chính Sách Bảo Mật Thiết Bị" else "Device Security Policy"
                } else {
                    if (isVi) "Xác Nhận Trên $deviceName" else "Device Confirmation"
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        text = {
            Column {
                Text(
                    text = if (isMdmRestricted) {
                        if (isVi)
                            "Thiết bị của bạn đang chịu sự kiểm soát của chính sách bảo mật doanh nghiệp (MDM/Work Profile) hoặc hệ thống khóa quyền ghi cài đặt. Bạn có thể mở cài đặt màn hình của máy để tự điều chỉnh thủ công."
                        else
                            "This device is restricted by an Enterprise Device Policy (MDM/Work Profile). Please use the System Display Settings to adjust font scale manually."
                    } else {
                        if (isVi)
                            "Hệ điều hành của máy bạn ($deviceName) có cơ chế bảo vệ riêng, yêu cầu bạn xác nhận trực tiếp trong mục Cài đặt Màn hình của hệ thống."
                        else
                            "Your device ROM ($deviceName) requires you to directly confirm font scaling inside the System Display Settings."
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                )
                if (reason != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Chi tiết: $reason",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = 11.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (isVi)
                        "💡 Bấm nút bên dưới để mở ngay màn hình Cài đặt của máy chỉ với 1 chạm."
                    else
                        "💡 Tap below to open Display Settings directly in 1 click.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = PurpleAccent,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onOpenDisplaySettings()
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = if (language == AppLanguage.VI) "Mở Cài Đặt Màn Hình ↗" else "Open Display Settings ↗",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.VI) "Để sau" else "Later",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

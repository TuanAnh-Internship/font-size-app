package com.example.fontsizecontroller.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.ui.component.TopAppBarWithLanguage

@Composable
fun UnsupportedErrorScreen(
    language: AppLanguage,
    isDarkMode: Boolean,
    onCloseClick: () -> Unit,
    onBackClick: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isVi = language == AppLanguage.VI
    val navyDark = Color(0xFF1E1B4B)
    val cardBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    val orangePhone = Color(0xFFF97316)
    val circlePeachBg = Color(0xFFFFF1EB)
    val purpleInfo = Color(0xFF8B5CF6)
    val stepCircleBg = Color(0xFFEDE9FE)

    Scaffold(
        topBar = {
            TopAppBarWithLanguage(
                title = if (isVi) "Thông Báo Lỗi" else "Error Notice",
                language = language,
                isDarkMode = isDarkMode,
                onBackClick = onBackClick,
                onToggleLanguage = onToggleLanguage,
                onToggleDarkMode = onToggleDarkMode
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = onCloseClick,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = navyDark,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = if (isVi) "Đóng" else "Close",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // GRAPHIC: Phone in soft peach circle with red 'X' badge
            Box(
                modifier = Modifier.size(110.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(circlePeachBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = orangePhone,
                        modifier = Modifier.size(46.dp)
                    )
                }

                // Red 'X' badge at top right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEF4444)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Tiêu đề & Diễn giải
            Text(
                text = if (isVi) "Thiết bị không hỗ trợ" else "Device Unsupported",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = navyDark
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = if (isVi)
                    "Rất tiếc, thiết bị hoặc phiên bản hệ điều hành hiện tại của bạn không hỗ trợ tính năng thay đổi phông chữ hệ thống. Vui lòng kiểm tra lại yêu cầu cấu hình tối thiểu."
                else
                    "Unfortunately, your current device or operating system version does not support system font scale modifications. Please verify the minimum requirements.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            // Card hướng dẫn khắc phục
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header card
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(purpleInfo),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isVi) "Hướng dẫn khắc phục" else "Troubleshooting Guide",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = navyDark
                        )
                    }

                    // Bước 1
                    TroubleshootingStep(
                        stepNumber = "1",
                        content = if (isVi)
                            "Cập nhật hệ điều hành (iOS/Android) lên phiên bản mới nhất trong Cài đặt hệ thống."
                        else
                            "Update your operating system (Android) to the latest version in System Settings.",
                        circleBg = stepCircleBg,
                        circleTextColor = purpleInfo
                    )

                    // Bước 2
                    TroubleshootingStep(
                        stepNumber = "2",
                        content = if (isVi)
                            "Kiểm tra xem thiết bị có đang ở chế độ Tiết kiệm pin hoặc Giới hạn quyền truy cập không."
                        else
                            "Check if device is running in Battery Saver or restricted permission mode.",
                        circleBg = stepCircleBg,
                        circleTextColor = purpleInfo
                    )

                    // Bước 3
                    TroubleshootingStep(
                        stepNumber = "3",
                        content = if (isVi)
                            "Nếu lỗi vẫn tiếp diễn, hãy thử khởi động lại thiết bị và mở lại ứng dụng."
                        else
                            "If the issue persists, try restarting your device and relaunching FontM.",
                        circleBg = stepCircleBg,
                        circleTextColor = purpleInfo
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TroubleshootingStep(
    stepNumber: String,
    content: String,
    circleBg: Color,
    circleTextColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(circleBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = circleTextColor
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

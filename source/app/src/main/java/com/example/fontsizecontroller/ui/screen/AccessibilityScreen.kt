package com.example.fontsizecontroller.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.FontSizeUiState
import com.example.fontsizecontroller.model.ReadingMode
import com.example.fontsizecontroller.ui.component.TopAppBarWithLanguage
import com.example.fontsizecontroller.ui.theme.PurpleAccent
import kotlin.math.roundToInt

@Composable
fun AccessibilityScreen(
    uiState: FontSizeUiState,
    onReadingModeChange: (ReadingMode) -> Unit,
    onToggleBold: () -> Unit,
    onSelectScale: (FontSizeOption) -> Unit,
    onApplyScale: () -> Unit = {},
    onOpenSettings: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Trạng thái cục bộ phục vụ trải nghiệm mượt mà
    var currentScaleSlider by remember(uiState.selectedOption, uiState.currentScale) {
        mutableFloatStateOf(uiState.selectedOption?.scale ?: uiState.currentScale ?: 1.0f)
    }

    var isHighContrastActive by remember(uiState.readingMode) {
        mutableStateOf(uiState.readingMode == ReadingMode.HIGH_CONTRAST)
    }

    val animatedScale by animateFloatAsState(
        targetValue = currentScaleSlider,
        label = "animatedAccessibilityScale"
    )

    Scaffold(
        topBar = {
            TopAppBarWithLanguage(
                title = if (uiState.language == AppLanguage.VI) "Trợ Năng Hiển Thị" else "Display Accessibility",
                language = uiState.language,
                isDarkMode = uiState.isDarkMode,
                onBackClick = null,
                onToggleLanguage = onToggleLanguage,
                onToggleDarkMode = onToggleDarkMode,
                onSettingsClick = onOpenSettings
            )
        },
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // 1. THẺ SO SÁNH TRỰC QUAN TRƯỚC / SAU KHI TỐI ƯU (Comparison Card)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrastActive) Color(0xFF000000) else Color(0xFF1E293B)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = if (isHighContrastActive) Color(0xFFFDE047) else Color(0xFF06B6D4).copy(alpha = 0.5f),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth(0.75f)) {
                        Text(
                            text = if (uiState.language == AppLanguage.VI) "SAU KHI TỐI ƯU" else "AFTER OPTIMIZATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF06B6D4),
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (uiState.language == AppLanguage.VI)
                                "Nội dung rõ nét, dễ đọc."
                            else
                                "Crisp text, easy to read.",
                            fontSize = (15 * animatedScale.coerceAtMost(1.3f)).sp,
                            fontWeight = if (uiState.isBoldPreview) FontWeight.ExtraBold else FontWeight.Bold,
                            lineHeight = (20 * animatedScale.coerceAtMost(1.3f)).sp,
                            color = if (isHighContrastActive) Color(0xFFFDE047) else Color.White
                        )
                    }

                    // Vạch chia so sánh phong cách thanh trượt kéo dọc (Cyan split bar)
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 12.dp)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF06B6D4)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CompareArrows,
                            contentDescription = "Compare",
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // 2. PHẦN TÙY CHỈNH HỆ THỐNG
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF06B6D4).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "A",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF06B6D4),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(includeFontPadding = false),
                            textAlign = TextAlign.Center
                        )
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (uiState.language == AppLanguage.VI) "TÙY CHỈNH HỆ THỐNG" else "SYSTEM ADJUSTMENTS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF06B6D4),
                    letterSpacing = 0.5.sp
                )
            }

            // TÙY CHỌN 1: CHỮ ĐẬM TOÀN HỆ THỐNG [HOT]
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (uiState.language == AppLanguage.VI) "Chữ đậm toàn hệ thống" else "System Bold Text",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(PurpleAccent.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "HOT",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PurpleAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Aa",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = " ——— ",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                text = "Aa",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PurpleAccent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Switch(
                        checked = uiState.isBoldPreview,
                        onCheckedChange = { onToggleBold() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = PurpleAccent
                        )
                    )
                }
            }

            // TÙY CHỌN 2: ĐỘ TƯƠNG PHẢN CAO
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (uiState.language == AppLanguage.VI) "Độ tương phản cao" else "High Contrast Mode",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (uiState.language == AppLanguage.VI)
                                "Tăng độ rõ nét cho văn bản khó đọc"
                            else
                                "Enhance contrast for low vision clarity",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Switch(
                        checked = isHighContrastActive,
                        onCheckedChange = { checked ->
                            isHighContrastActive = checked
                            onReadingModeChange(if (checked) ReadingMode.HIGH_CONTRAST else ReadingMode.STANDARD)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF06B6D4)
                        )
                    )
                }
            }

            // TÙY CHỌN 3: THU PHÓNG MÀN HÌNH (Stepped Cyan Slider)
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.language == AppLanguage.VI) "Thu phóng màn hình" else "Screen Magnification",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF06B6D4).copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${(currentScaleSlider * 100).toInt()}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF06B6D4)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (uiState.language == AppLanguage.VI)
                            "Phóng to biểu tượng và kích thước chữ"
                        else
                            "Magnify system icons and reading typography",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Slider(
                        value = currentScaleSlider,
                        onValueChange = { raw ->
                            val stepped = (raw * 20).roundToInt() / 20f
                            currentScaleSlider = stepped
                            onSelectScale(FontSizeOption(label = "Custom (${String.format(java.util.Locale.US, "%.2fx", stepped)})", scale = stepped))
                        },
                        valueRange = 0.85f..1.50f,
                        steps = 12,
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF06B6D4),
                            activeTrackColor = Color(0xFF06B6D4),
                            inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "A", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (uiState.language == AppLanguage.VI) "Nhỏ" else "Small", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = if (uiState.language == AppLanguage.VI) "Lớn" else "Large", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF06B6D4))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "A", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF06B6D4))
                        }
                    }
                }
            }

            // 3. NÚT LƯU VÀ ÁP DỤNG CẤU HÌNH (Purple CTA)
            Button(
                onClick = onApplyScale,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleAccent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (uiState.language == AppLanguage.VI) "Lưu và áp dụng cấu hình" else "Save & Apply Settings",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            // Nút "Khôi phục mặc định"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = {
                        currentScaleSlider = 1.00f
                        isHighContrastActive = false
                        onReadingModeChange(ReadingMode.STANDARD)
                        onSelectScale(FontSizeOption(label = "Default (1.00x)", scale = 1.00f))
                    }
                ) {
                    Text(
                        text = if (uiState.language == AppLanguage.VI) "Khôi phục mặc định" else "Reset to Default",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

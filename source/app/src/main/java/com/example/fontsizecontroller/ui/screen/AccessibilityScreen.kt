package com.example.fontsizecontroller.ui.screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.FontSizeUiState
import com.example.fontsizecontroller.model.ReadingMode
import com.example.fontsizecontroller.ui.component.FontPreviewCard
import com.example.fontsizecontroller.ui.component.TopAppBarWithLanguage
import com.example.fontsizecontroller.ui.theme.PurpleAccent

@Composable
fun AccessibilityScreen(
    uiState: FontSizeUiState,
    onReadingModeChange: (ReadingMode) -> Unit,
    onToggleBold: () -> Unit,
    onSelectScale: (FontSizeOption) -> Unit,
    onBackClick: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBarWithLanguage(
                title = if (uiState.language == AppLanguage.VI) "Bộ Trợ Năng Mở Rộng" else "Accessibility Suite",
                language = uiState.language,
                isDarkMode = uiState.isDarkMode,
                onBackClick = onBackClick,
                onToggleLanguage = onToggleLanguage,
                onToggleDarkMode = onToggleDarkMode
            )
        },
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

            // Thẻ xem trước trực tiếp theo chế độ đọc đã chọn
            FontPreviewCard(
                selectedOption = uiState.selectedOption,
                language = uiState.language,
                readingMode = uiState.readingMode,
                isBoldPreview = uiState.isBoldPreview
            )

            // PHẦN 1: CHẾ ĐỘ ĐỌC BẢO VỆ MẮT (Reading Modes)
            Text(
                text = if (uiState.language == AppLanguage.VI) "CHẾ ĐỘ ĐỌC BẢO VỆ MẮT" else "EYE-CARE READING MODES",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PurpleAccent,
                    letterSpacing = 0.5.sp
                )
            )

            ReadingModeItem(
                title = if (uiState.language == AppLanguage.VI) "Chuẩn Hệ Thống" else "Standard System",
                description = if (uiState.language == AppLanguage.VI) "Theo chế độ sáng / tối mặc định của máy" else "Follows system light / dark mode theme",
                icon = Icons.Outlined.WbSunny,
                isSelected = uiState.readingMode == ReadingMode.STANDARD,
                onClick = { onReadingModeChange(ReadingMode.STANDARD) }
            )

            ReadingModeItem(
                title = if (uiState.language == AppLanguage.VI) "Sách Giấy Cổ Điển (Sepia)" else "Warm Sepia (Book Mode)",
                description = if (uiState.language == AppLanguage.VI) "Nền vàng kem dịu mắt, giảm ánh sáng xanh khi đọc ban đêm" else "Warm paper tint, reduces blue light strain at night",
                icon = Icons.Outlined.MenuBook,
                isSelected = uiState.readingMode == ReadingMode.SEPIA,
                accentColor = Color(0xFF8B4513),
                onClick = { onReadingModeChange(ReadingMode.SEPIA) }
            )

            ReadingModeItem(
                title = if (uiState.language == AppLanguage.VI) "Tương Phản Cao (WCAG AAA)" else "High Contrast (WCAG AAA)",
                description = if (uiState.language == AppLanguage.VI) "Nền đen tuyền, chữ vàng chanh rực rỡ cho mắt yếu" else "Pure black & vivid yellow for low vision / elderly",
                icon = Icons.Outlined.Contrast,
                isSelected = uiState.readingMode == ReadingMode.HIGH_CONTRAST,
                accentColor = Color(0xFFFDE047),
                onClick = { onReadingModeChange(ReadingMode.HIGH_CONTRAST) }
            )

            // PHẦN 2: CHỮ ĐẬM TRỰC QUAN (Bold Text Toggle)
            Text(
                text = if (uiState.language == AppLanguage.VI) "TĂNG ĐỘ RÕ NÉT CỦA CHỮ" else "TEXT LEGIBILITY ENHANCEMENT",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PurpleAccent,
                    letterSpacing = 0.5.sp
                )
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FormatBold,
                                contentDescription = "Bold text",
                                tint = PurpleAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (uiState.language == AppLanguage.VI) "Mô phỏng chữ đậm" else "Bold Text Preview",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    "Tăng độ dày nét chữ để dễ nhận diện"
                                else
                                    "Increase stroke thickness for clearer reading",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

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

            // PHẦN 3: GỢI Ý CỠ CHỮ THÔNG MINH (Reading Recommendations)
            Text(
                text = if (uiState.language == AppLanguage.VI) "GỢI Ý CỠ CHỮ THEO ĐỘ TUỔI & THỊ LỰC" else "SMART FONT RECOMMENDATIONS",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PurpleAccent,
                    letterSpacing = 0.5.sp
                )
            )

            RecommendationCard(
                category = if (uiState.language == AppLanguage.VI) "Thị lực tốt / Giới trẻ" else "Normal Vision / Young",
                scaleText = "1.00x (Chuẩn)",
                hint = if (uiState.language == AppLanguage.VI) "Kích thước gốc của nhà sản xuất" else "Default manufacturer size",
                onClick = { onSelectScale(FontSizeOption("Default", 1.00f)) }
            )

            RecommendationCard(
                category = if (uiState.language == AppLanguage.VI) "Mắt cận thị / Mỏi mắt văn phòng" else "Myopia / Digital Eye Fatigue",
                scaleText = "1.15x (Lớn)",
                hint = if (uiState.language == AppLanguage.VI) "Tăng 15% kích thước, đọc thoải mái hơn" else "+15% scale for comfortable long reading",
                onClick = { onSelectScale(FontSizeOption("Large", 1.15f)) }
            )

            RecommendationCard(
                category = if (uiState.language == AppLanguage.VI) "Người cao tuổi / Thị lực lão hóa" else "Elderly / Low Vision Accessibility",
                scaleText = "1.30x - 1.50x (Rất Lớn)",
                hint = if (uiState.language == AppLanguage.VI) "Chữ to bản, chống nhòe nét triệt để" else "Maximum clarity and magnified readability",
                onClick = { onSelectScale(FontSizeOption("Extra Large", 1.30f)) }
            )

            // Nút hoàn tất quay lại
            Button(
                onClick = onBackClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = if (uiState.language == AppLanguage.VI) "Hoàn Tất & Về Trang Chủ" else "Complete & Return Home",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ReadingModeItem(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    accentColor: Color = PurpleAccent,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) accentColor else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) accentColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendationCard(
    category: String,
    scaleText: String,
    hint: String,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = scaleText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = PurpleAccent
                )
            }
        }
    }
}

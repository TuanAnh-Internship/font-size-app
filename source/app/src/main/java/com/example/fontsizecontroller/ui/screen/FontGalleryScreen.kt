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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.ContactPhone
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.FontSizeUiState
import com.example.fontsizecontroller.ui.component.CustomFontSliderCard
import com.example.fontsizecontroller.ui.component.TopAppBarWithLanguage
import com.example.fontsizecontroller.ui.theme.MintSuccess
import com.example.fontsizecontroller.ui.theme.PurpleAccent

@Composable
fun FontGalleryScreen(
    uiState: FontSizeUiState,
    onBackClick: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    var customInputText by remember {
        mutableStateOf(
            if (uiState.language == AppLanguage.VI)
                "Chữ to rõ ràng giúp mắt thư giãn mỗi ngày."
            else
                "Clear readable typography relieves daily eye fatigue."
        )
    }

    var selectedFontFamily by remember { mutableStateOf(FontFamily.SansSerif) }
    var selectedFontLabel by remember { mutableStateOf("Sans Serif") }

    val currentScale = uiState.selectedOption?.scale ?: uiState.currentScale ?: 1.0f

    Scaffold(
        topBar = {
            TopAppBarWithLanguage(
                title = if (uiState.language == AppLanguage.VI) "Thư Viện Kiểu Chữ" else "Font Style Gallery",
                language = uiState.language,
                isDarkMode = uiState.isDarkMode,
                onBackClick = onBackClick,
                onToggleLanguage = onToggleLanguage,
                onToggleDarkMode = onToggleDarkMode
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

            // PHẦN 1: CHỌN HỌ PHÔNG CHỮ (Font Families)
            Text(
                text = if (uiState.language == AppLanguage.VI) "CHỌN KIỂU DÁNG CHỮ" else "SELECT TYPOGRAPHY STYLE",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PurpleAccent,
                    letterSpacing = 0.5.sp
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FontFamilyChip(
                    name = "Sans-Serif",
                    sub = "Hiện đại",
                    fontFamily = FontFamily.SansSerif,
                    isSelected = selectedFontLabel == "Sans Serif",
                    onClick = {
                        selectedFontFamily = FontFamily.SansSerif
                        selectedFontLabel = "Sans Serif"
                    },
                    modifier = Modifier.weight(1f)
                )

                FontFamilyChip(
                    name = "Serif",
                    sub = "Sách in",
                    fontFamily = FontFamily.Serif,
                    isSelected = selectedFontLabel == "Serif",
                    onClick = {
                        selectedFontFamily = FontFamily.Serif
                        selectedFontLabel = "Serif"
                    },
                    modifier = Modifier.weight(1f)
                )

                FontFamilyChip(
                    name = "Monospace",
                    sub = "Mã số",
                    fontFamily = FontFamily.Monospace,
                    isSelected = selectedFontLabel == "Monospace",
                    onClick = {
                        selectedFontFamily = FontFamily.Monospace
                        selectedFontLabel = "Monospace"
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            // PHẦN 2: Ô GÕ THỬ VĂN BẢN TRỰC TIẾP (Interactive Playground)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.TextFields,
                            contentDescription = "Text Input",
                            tint = PurpleAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.language == AppLanguage.VI) "Gõ thử văn bản của bạn:" else "Type your own text:",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = customInputText,
                        onValueChange = { customInputText = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurpleAccent,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Khung kết quả hiển thị tương ứng
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = customInputText.ifBlank { "(Nhập nội dung bất kỳ...)" },
                            fontSize = (16 * currentScale).sp,
                            fontFamily = selectedFontFamily,
                            lineHeight = (24 * currentScale).sp,
                            fontWeight = if (uiState.isBoldPreview) FontWeight.Bold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // PHẦN 3: MÔ PHỎNG 3 ỨNG DỤNG THỰC TẾ TRÊN ĐIỆN THOẠI (Real-life App Simulations)
            Text(
                text = if (uiState.language == AppLanguage.VI) "MÔ PHỎNG ỨNG DỤNG THỰC TẾ TRÊN MÁY" else "REAL-LIFE APP SIMULATIONS",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PurpleAccent,
                    letterSpacing = 0.5.sp
                )
            )

            // MÔ PHỎNG 1: TIN NHẮN ZALO / SMS
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Chat,
                            contentDescription = "Chat",
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.language == AppLanguage.VI) "Tin nhắn Zalo / SMS" else "SMS / Zalo Messages",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bong bóng tin nhắn đi
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF0284C7))
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = if (uiState.language == AppLanguage.VI) "Mẹ ơi trưa nay con về ăn cơm nhé!" else "Mom, I am coming home for lunch!",
                                fontSize = (14 * currentScale).sp,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bong bóng tin nhắn đến
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = if (uiState.language == AppLanguage.VI) "Ừ con yêu, mẹ nấu canh chua rồi!" else "Sure sweetie, soup is ready!",
                                fontSize = (14 * currentScale).sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            // MÔ PHỎNG 2: BÀI BÁO TIN TỨC (News Article)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Newspaper,
                            contentDescription = "News",
                            tint = Color(0xFFE11D48),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.language == AppLanguage.VI) "Đọc báo VnExpress" else "Online News Article",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (uiState.language == AppLanguage.VI)
                            "Ứng dụng công nghệ Trợ Năng giúp người lớn tuổi làm chủ Smartphone"
                        else
                            "Accessibility Technology Empowers Elderly in the Digital Age",
                        fontSize = (16 * currentScale).sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = (22 * currentScale).sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (uiState.language == AppLanguage.VI)
                            "Việc tùy chỉnh cỡ chữ và độ tương phản cao giúp tăng khả năng tiếp cận thông tin, bảo vệ thị lực và mang lại sự tự tin khi sử dụng thiết bị."
                        else
                            "Custom font magnification and high contrast dramatically improve content accessibility, protecting vision and enhancing digital confidence.",
                        fontSize = (13 * currentScale).sp,
                        lineHeight = (19 * currentScale).sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // MÔ PHỎNG 3: DANH BẠ CUỘC GỌI CHỮ LỚN
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.ContactPhone,
                            contentDescription = "Contacts",
                            tint = MintSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (uiState.language == AppLanguage.VI) "Danh bạ điện thoại" else "Phone Contacts",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PurpleAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "BS",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (uiState.language == AppLanguage.VI) "Bác sĩ Tuấn (Viện Tim)" else "Dr. Alex (Cardiology)",
                                fontSize = (15 * currentScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "0988 123 456 • Gọi nhanh",
                                fontSize = (12 * currentScale).sp,
                                color = MintSuccess,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FontFamilyChip(
    name: String,
    sub: String,
    fontFamily: FontFamily,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PurpleAccent.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 1.dp),
        modifier = modifier
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PurpleAccent else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Aa",
                fontSize = 20.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) PurpleAccent else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) PurpleAccent else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = sub,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

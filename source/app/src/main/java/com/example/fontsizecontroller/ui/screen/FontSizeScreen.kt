package com.example.fontsizecontroller.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.FamilyProfile
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.FontSizeUiState
import com.example.fontsizecontroller.model.LocalizedStrings
import com.example.fontsizecontroller.ui.component.CurrentFontCard
import com.example.fontsizecontroller.ui.component.FontPresetGrid
import com.example.fontsizecontroller.ui.component.FontPreviewCard
import com.example.fontsizecontroller.ui.component.OemFallbackDialog
import com.example.fontsizecontroller.ui.component.TopAppBarWithLanguage
import com.example.fontsizecontroller.ui.theme.PurpleAccent
import com.example.fontsizecontroller.util.FontScaleMapper

@Composable
fun FontSizeScreen(
    uiState: FontSizeUiState,
    onSelectOption: (FontSizeOption) -> Unit,
    onApply: () -> Unit,
    onBackClick: (() -> Unit)? = null,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onOpenAccessibility: () -> Unit,
    onOpenDisplaySettings: () -> Unit,
    onDismissOemDialog: () -> Unit,
    onOpenSettings: () -> Unit,
    onResetDefault: () -> Unit,
    onConfirmReset: () -> Unit,
    onDismissResetDialog: () -> Unit,
    onSelectProfile: (String, Float) -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val strings = LocalizedStrings.get(uiState.language)
    val isVi = uiState.language == AppLanguage.VI

    Scaffold(
        topBar = {
            TopAppBarWithLanguage(
                title = if (isVi) "Điều Chỉnh Cỡ Chữ" else "Adjust Font Size",
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
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PurpleAccent)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(2.dp))

                // Thẻ thông tin cỡ chữ hiện tại
                CurrentFontCard(
                    currentScale = uiState.currentScale,
                    currentLabel = uiState.currentLabel,
                    language = uiState.language
                )

                // Thanh chọn nhanh Profile cỡ chữ gia đình (Cá nhân, Bố mẹ, Ông bà)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isVi) "Đối tượng sử dụng:" else "User:",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FamilyProfile.defaultProfiles.forEach { profile ->
                            val isSelected = uiState.selectedProfileId == profile.id
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                    )
                                    .clickable { onSelectProfile(profile.id, profile.scale) }
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                    }
                                    Text(
                                        text = if (isVi) profile.nameVi else profile.nameEn,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // Khung xem trước trực quan (co giãn tức thì khi chọn)
                FontPreviewCard(
                    selectedOption = uiState.selectedOption,
                    language = uiState.language,
                    readingMode = uiState.readingMode,
                    isBoldPreview = uiState.isBoldPreview
                )

                // Lưới 4 Preset chọn cỡ chữ
                FontPresetGrid(
                    presets = FontScaleMapper.defaultPresets,
                    selectedOption = uiState.selectedOption,
                    language = uiState.language,
                    onSelect = onSelectOption
                )

                // Thanh trượt tự chọn cỡ chữ an toàn (0.80x - 2.00x)
                com.example.fontsizecontroller.ui.component.CustomFontSliderCard(
                    currentScale = uiState.selectedOption?.scale ?: uiState.currentScale ?: 1.0f,
                    language = uiState.language,
                    onScaleChanged = onSelectOption
                )

                // Nút hành động chính "Áp dụng cỡ chữ"
                val isCurrentPreset = uiState.selectedOption != null &&
                        uiState.currentScale != null &&
                        kotlin.math.abs(uiState.selectedOption.scale - uiState.currentScale) < 0.01f

                Button(
                    onClick = onApply,
                    enabled = !uiState.isApplying && !isCurrentPreset,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = when {
                            uiState.isApplying -> strings.btnApplying
                            isCurrentPreset ->
                                if (isVi) "Đang sử dụng cỡ chữ này" else "Currently Active"
                            else -> strings.btnApplyFontSize
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                // Nút phụ "Đặt lại mặc định (1.00x)"
                OutlinedButton(
                    onClick = onResetDefault,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = strings.btnResetDefault,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Hộp thoại xác nhận Khôi phục cỡ chữ chuẩn 1.00x
    if (uiState.showResetDialog) {
        AlertDialog(
            onDismissRequest = onDismissResetDialog,
            title = {
                Text(
                    text = strings.resetDialogTitle,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = strings.resetDialogMessage,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = onConfirmReset,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = strings.btnConfirm, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissResetDialog) {
                    Text(text = strings.btnCancel)
                }
            }
        )
    }

    // Hiển thị hộp thoại hướng dẫn OEM Fallback nếu ROM nhà sản xuất chặn ghi ngầm
    if (uiState.showOemFallbackDialog) {
        OemFallbackDialog(
            language = uiState.language,
            onOpenDisplaySettings = onOpenDisplaySettings,
            onDismiss = onDismissOemDialog
        )
    }
}

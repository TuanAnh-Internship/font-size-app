package com.example.fontsizecontroller.ui.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.FontSizeUiState
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
    onBackClick: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onOpenAccessibility: () -> Unit,
    onOpenDisplaySettings: () -> Unit,
    onDismissOemDialog: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBarWithLanguage(
                title = if (uiState.language == AppLanguage.VI) "Điều Chỉnh Cỡ Chữ" else "Adjust Font Size",
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
                            uiState.isApplying ->
                                if (uiState.language == AppLanguage.VI) "Đang áp dụng..." else "Applying..."
                            isCurrentPreset ->
                                if (uiState.language == AppLanguage.VI) "Đang sử dụng cỡ chữ này" else "Currently Active"
                            else ->
                                if (uiState.language == AppLanguage.VI) "Áp Dụng Cỡ Chữ" else "Apply Font Scale"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
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

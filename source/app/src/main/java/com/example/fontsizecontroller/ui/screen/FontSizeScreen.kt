package com.example.fontsizecontroller.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.FontSizeUiState
import com.example.fontsizecontroller.model.fontSizeOptions
import com.example.fontsizecontroller.ui.component.FontPresetCard
import com.example.fontsizecontroller.ui.component.FontPreviewCard
import com.example.fontsizecontroller.ui.component.StatusMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSizeScreen(
    uiState: FontSizeUiState,
    onSelectOption: (FontSizeOption) -> Unit,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý cỡ chữ") }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Cỡ chữ hiện tại: ${uiState.currentScale?.let { "${it}x" } ?: "Chưa rõ"}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                item {
                    FontPreviewCard(selectedOption = uiState.selectedOption)
                }

                item {
                    Text(
                        text = "Chọn cỡ chữ mong muốn:",
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                items(fontSizeOptions) { option ->
                    FontPresetCard(
                        option = option,
                        isSelected = uiState.selectedOption?.scale == option.scale,
                        onSelect = onSelectOption
                    )
                }

                item {
                    StatusMessage(
                        status = uiState.status,
                        message = uiState.message
                    )
                }

                item {
                    val isSameScale = uiState.selectedOption?.scale == uiState.currentScale
                    Button(
                        onClick = onApply,
                        enabled = !uiState.isApplying && !isSameScale,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (uiState.isApplying) "Đang áp dụng..." else "Áp dụng cỡ chữ")
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

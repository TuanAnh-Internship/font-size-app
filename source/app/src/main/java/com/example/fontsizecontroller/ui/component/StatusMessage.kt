package com.example.fontsizecontroller.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fontsizecontroller.model.ApplyUiResult

@Composable
fun StatusMessage(
    result: ApplyUiResult,
    modifier: Modifier = Modifier
) {
    if (result is ApplyUiResult.Idle) return

    val containerColor = when (result) {
        is ApplyUiResult.Success -> MaterialTheme.colorScheme.primaryContainer
        is ApplyUiResult.Error, ApplyUiResult.Unsupported -> MaterialTheme.colorScheme.errorContainer
        ApplyUiResult.PermissionRequired -> MaterialTheme.colorScheme.tertiaryContainer
        ApplyUiResult.Idle -> MaterialTheme.colorScheme.surfaceVariant
    }

    val message = when (result) {
        is ApplyUiResult.Success -> "Đã áp dụng cỡ chữ thành công"
        is ApplyUiResult.Error -> result.message ?: "Đã xảy ra lỗi"
        ApplyUiResult.Unsupported -> "Hệ thống không hỗ trợ scale này"
        ApplyUiResult.PermissionRequired -> "Cần cấp quyền WRITE_SETTINGS"
        ApplyUiResult.Idle -> ""
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}

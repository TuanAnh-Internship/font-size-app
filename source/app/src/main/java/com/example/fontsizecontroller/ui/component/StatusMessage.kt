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
import com.example.fontsizecontroller.model.FontSizeStatus

@Composable
fun StatusMessage(
    status: FontSizeStatus,
    message: String?,
    modifier: Modifier = Modifier
) {
    if (message.isNullOrBlank() && status == FontSizeStatus.Idle) return

    val containerColor = when (status) {
        FontSizeStatus.Success -> MaterialTheme.colorScheme.primaryContainer
        FontSizeStatus.Error, FontSizeStatus.Unsupported -> MaterialTheme.colorScheme.errorContainer
        FontSizeStatus.PermissionRequired -> MaterialTheme.colorScheme.tertiaryContainer
        FontSizeStatus.Idle -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Text(
            text = message ?: "",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(12.dp)
        )
    }
}

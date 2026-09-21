package com.example.fontsizecontroller.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.ui.theme.BadgeTint
import com.example.fontsizecontroller.ui.theme.BorderLight
import com.example.fontsizecontroller.ui.theme.CardHighlight
import com.example.fontsizecontroller.ui.theme.NavyPrimary
import com.example.fontsizecontroller.ui.theme.PurpleAccent

@Composable
fun FontPresetGrid(
    presets: List<FontSizeOption>,
    selectedOption: FontSizeOption?,
    language: AppLanguage,
    onSelect: (FontSizeOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = if (language == AppLanguage.VI) "Chọn cỡ chữ mong muốn" else "Choose Target Font Size",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Lưới 2x2
        val rows = presets.chunked(2)
        for (row in rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (option in row) {
                    val isSelected = selectedOption?.scale == option.scale
                    val displayLabel = when (option.label) {
                        "Small" -> if (language == AppLanguage.VI) "Nhỏ" else "Small"
                        "Default" -> if (language == AppLanguage.VI) "Mặc định" else "Default"
                        "Large" -> if (language == AppLanguage.VI) "Lớn" else "Large"
                        "Extra Large" -> if (language == AppLanguage.VI) "Rất lớn" else "Extra Large"
                        else -> option.label
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) PurpleAccent.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) PurpleAccent else MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onSelect(option) }
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = displayLabel,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) PurpleAccent else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format(java.util.Locale.US, "%.2fx", option.scale),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

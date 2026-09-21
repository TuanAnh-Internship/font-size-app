package com.example.fontsizecontroller.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.ui.theme.BadgeTint
import com.example.fontsizecontroller.ui.theme.BorderLight
import com.example.fontsizecontroller.ui.theme.NavyPrimary
import com.example.fontsizecontroller.ui.theme.PurpleAccent
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun CustomFontSliderCard(
    currentScale: Float,
    language: AppLanguage,
    onScaleChanged: (FontSizeOption) -> Unit,
    modifier: Modifier = Modifier
) {
    // Hỗ trợ tối đa lên đến 2.00x (200%) chuẩn Trợ Năng WCAG cho người lớn tuổi
    val safeScale = currentScale.coerceIn(0.80f, 2.00f)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Tune,
                            contentDescription = "Tune font scale",
                            tint = PurpleAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.VI) "Cỡ chữ tự chọn" else "Custom Font Scale",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.VI) "Chuẩn Trợ Năng: 0.80x - 2.00x" else "Accessibility Range: 0.80x - 2.00x",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // Badge hiển thị số scale hiện tại
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = String.format(Locale.US, "%.2fx", safeScale),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Slider tùy chỉnh từ 0.80x đến 2.00x với bước nhảy 0.05
            Slider(
                value = safeScale,
                onValueChange = { rawVal ->
                    val stepped = (rawVal * 20).roundToInt() / 20f
                    val label = when {
                        Math.abs(stepped - 0.85f) < 0.01f -> "Small"
                        Math.abs(stepped - 1.00f) < 0.01f -> "Default"
                        Math.abs(stepped - 1.15f) < 0.01f -> "Large"
                        Math.abs(stepped - 1.30f) < 0.01f -> "Extra Large"
                        Math.abs(stepped - 2.00f) < 0.01f -> "Maximum (2.00x)"
                        else -> String.format(Locale.US, "Custom (%.2fx)", stepped)
                    }
                    onScaleChanged(FontSizeOption(label = label, scale = stepped))
                },
                valueRange = 0.80f..2.00f,
                steps = 23, // 23 nấc trung gian: 0.85, 0.90, ..., 1.95
                colors = SliderDefaults.colors(
                    thumbColor = PurpleAccent,
                    activeTrackColor = PurpleAccent,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "0.80x (Nhỏ)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = "1.00x (Chuẩn)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Text(
                    text = "2.00x (Trợ năng)",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

package com.example.fontsizecontroller.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.fontsizecontroller.ui.theme.PurpleAccent

@Composable
fun FontPreviewCard(
    selectedOption: FontSizeOption?,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val scale = selectedOption?.scale ?: 1.0f

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.VI) "XEM TRƯỚC TRỰC QUAN" else "LIVE PREVIEW",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PurpleAccent
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.VI) "Trải nghiệm văn bản thực tế" else "Real-time Text Experience",
                fontSize = (18 * scale).sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = (24 * scale).sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (language == AppLanguage.VI)
                    "Văn bản mẫu sẽ co giãn trực quan theo tỷ lệ ${String.format(java.util.Locale.US, "%.2fx", scale)} giúp bạn dễ dàng quan sát trước khi lưu cài đặt."
                else
                    "Sample text scales smoothly at ${String.format(java.util.Locale.US, "%.2fx", scale)} allowing you to preview layout clarity before applying.",
                fontSize = (14 * scale).sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = (20 * scale).sp
            )
        }
    }
}

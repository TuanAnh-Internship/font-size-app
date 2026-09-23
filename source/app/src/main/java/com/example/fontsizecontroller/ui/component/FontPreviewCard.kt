package com.example.fontsizecontroller.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.ReadingMode
import com.example.fontsizecontroller.ui.theme.PurpleAccent

@Composable
fun FontPreviewCard(
    selectedOption: FontSizeOption?,
    language: AppLanguage,
    modifier: Modifier = Modifier,
    readingMode: ReadingMode = ReadingMode.STANDARD,
    isBoldPreview: Boolean = false
) {
    val targetScale = selectedOption?.scale ?: 1.0f

    // Hiệu ứng chuyển đổi kích thước chữ mượt mà
    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(durationMillis = 200),
        label = "fontScaleAnimation"
    )

    // Xác định bộ màu sắc tương ứng với từng chế độ đọc bảo vệ thị lực
    val containerColor: Color
    val contentColor: Color
    val subContentColor: Color
    val outlineColor: Color
    val badgeBgColor: Color
    val badgeTextColor: Color

    when (readingMode) {
        ReadingMode.STANDARD -> {
            containerColor = MaterialTheme.colorScheme.surface
            contentColor = MaterialTheme.colorScheme.onSurface
            subContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            outlineColor = MaterialTheme.colorScheme.outline
            badgeBgColor = MaterialTheme.colorScheme.surfaceVariant
            badgeTextColor = PurpleAccent
        }

        ReadingMode.SEPIA -> {
            // Nền vàng kem sách giấy cổ điển, dịu mắt, lọc ánh sáng xanh ban đêm
            containerColor = Color(0xFFFDF6E2)
            contentColor = Color(0xFF3B2D1D)
            subContentColor = Color(0xFF6E5D4B)
            outlineColor = Color(0xFFE8D7B8)
            badgeBgColor = Color(0xFFF3E5C8)
            badgeTextColor = Color(0xFF8B4513)
        }

        ReadingMode.HIGH_CONTRAST -> {
            // Chuẩn WCAG AAA: Nền đen tuyền, chữ vàng chanh rực rỡ, độ tương phản tuyệt đối
            containerColor = Color(0xFF000000)
            contentColor = Color(0xFFFDE047)
            subContentColor = Color(0xFFFEF08A)
            outlineColor = Color(0xFFFDE047)
            badgeBgColor = Color(0xFF1E293B)
            badgeTextColor = Color(0xFFFDE047)
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, outlineColor, RoundedCornerShape(16.dp))
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
                        .background(badgeBgColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (readingMode) {
                            ReadingMode.STANDARD ->
                                if (language == AppLanguage.VI) "XEM TRƯỚC TRỰC QUAN" else "LIVE PREVIEW"
                            ReadingMode.SEPIA ->
                                if (language == AppLanguage.VI) "CHẾ ĐỘ SÁCH VÀNG ẤM" else "WARM READING MODE"
                            ReadingMode.HIGH_CONTRAST ->
                                if (language == AppLanguage.VI) "ĐỘ TƯƠNG PHẢN CAO" else "HIGH CONTRAST MODE"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == AppLanguage.VI) "Văn bản mẫu" else "Sample Text",
                fontSize = (15 * animatedScale.coerceAtMost(1.25f)).sp,
                fontWeight = if (isBoldPreview) FontWeight.ExtraBold else FontWeight.Bold,
                color = contentColor,
                lineHeight = (20 * animatedScale.coerceAtMost(1.25f)).sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (language == AppLanguage.VI)
                    "Chữ hiển thị rõ nét, kích thước chuẩn dễ đọc."
                else
                    "Text renders clearly and comfortably.",
                fontSize = (14 * animatedScale).sp,
                fontWeight = if (isBoldPreview) FontWeight.Bold else FontWeight.Normal,
                color = subContentColor,
                lineHeight = (19 * animatedScale).sp
            )
        }
    }
}

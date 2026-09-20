package com.example.fontsizecontroller.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.FontSizeOption

@Composable
fun FontPreviewCard(
    selectedOption: FontSizeOption?,
    modifier: Modifier = Modifier
) {
    val scale = selectedOption?.scale ?: 1.0f

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Xem trước giao diện (Preview)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Dòng tiêu đề xem trước",
                fontSize = (20 * scale).sp,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Nội dung văn bản sẽ hiển thị theo tỷ lệ cỡ chữ ${scale}x để bạn dễ dàng đánh giá trước khi áp dụng vào toàn hệ thống.",
                fontSize = (15 * scale).sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

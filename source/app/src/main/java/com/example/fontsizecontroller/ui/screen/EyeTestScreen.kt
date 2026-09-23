package com.example.fontsizecontroller.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.FontSizeUiState
import com.example.fontsizecontroller.ui.component.TopAppBarWithLanguage
import com.example.fontsizecontroller.ui.theme.PurpleAccent

@Composable
fun EyeTestScreen(
    uiState: FontSizeUiState,
    onApplyRecommendedScale: (Float, String) -> Unit,
    onOpenSettings: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Nếu đã từng làm bài kiểm tra, mặc định hiển thị kết quả cũ
    var showPreviousResult by remember(uiState.eyeTestDone) { mutableStateOf(uiState.eyeTestDone) }

    // Tiến trình: 1, 2, 3
    var currentStep by remember { mutableIntStateOf(1) }
    var selectedOptionIndex by remember { mutableIntStateOf(1) } // 0: Quá nhỏ, 1: Vừa vặn, 2: Hơi to

    val sampleScale = when (currentStep) {
        1 -> 1.00f
        2 -> 1.15f
        else -> 1.30f
    }

    val stepTitle = when (currentStep) {
        1 -> if (uiState.language == AppLanguage.VI) "Bước 1 / 3 - Kiểm tra độ thoải mái" else "Step 1 / 3 - Reading Comfort"
        2 -> if (uiState.language == AppLanguage.VI) "Bước 2 / 3 - Thử nghiệm phóng to nhẹ" else "Step 2 / 3 - Mild Magnification"
        else -> if (uiState.language == AppLanguage.VI) "Bước 3 / 3 - Đánh giá cuối - Chọn để áp dụng" else "Step 3 / 3 - Final Evaluation - Choose to Apply"
    }

    val progressPercent = (currentStep / 3f)

    val recommendedScale = when {
        selectedOptionIndex == 0 -> (sampleScale + 0.15f).coerceAtMost(1.50f)
        selectedOptionIndex == 2 -> (sampleScale - 0.10f).coerceAtLeast(1.00f)
        else -> sampleScale
    }

    val recommendedLabel = String.format(java.util.Locale.US, "Cỡ %.2fx", recommendedScale)

    Scaffold(
        topBar = {
            TopAppBarWithLanguage(
                title = if (uiState.language == AppLanguage.VI) "Kiểm Tra Thị Lực" else "Vision Eye Test",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // -- KẾT QUẢ ĐÃ LƯU (nếu đã đo xong) --
            if (showPreviousResult && uiState.eyeTestDone) {
                // Thẻ kết quả cũ
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PurpleAccent.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, PurpleAccent.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.CheckCircle,
                                contentDescription = "Done",
                                tint = PurpleAccent,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    "ĐÃ HOÀN THÀNH KIỂM TRA"
                                else "EYE TEST COMPLETED",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PurpleAccent,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (uiState.language == AppLanguage.VI)
                                "Kết quả lần trước: Cỡ chữ ${
                                    String.format(java.util.Locale.US, "%.2fx", uiState.eyeTestResultScale)
                                } được áp dụng thành công."
                            else
                                "Previous result: Font scale ${
                                    String.format(java.util.Locale.US, "%.2fx", uiState.eyeTestResultScale)
                                } was successfully applied.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (uiState.language == AppLanguage.VI)
                                "Cỡ chữ đã được lưu lại. Không cần đo lại trừ khi bạn muốn thay đổi."
                            else
                                "Your preference has been saved. No need to redo unless you want to change.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Áp dụng lại cỡ chữ đã lưu
                        Button(
                            onClick = {
                                onApplyRecommendedScale(
                                    uiState.eyeTestResultScale,
                                    String.format(java.util.Locale.US, "Cỡ %.2fx", uiState.eyeTestResultScale)
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    "Áp dụng lại cỡ chữ này"
                                else "Re-apply This Font Size",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Đo lại từ đầu
                        TextButton(
                            onClick = {
                                showPreviousResult = false
                                currentStep = 1
                                selectedOptionIndex = 1
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = "Retake",
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    "Đo lại từ đầu"
                                else "Retake Test",
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                // -- BÀI KIỂM TRA CHƯA LÀM / ĐANG LÀM --

                // 1. THANH TIẾN ĐỘ
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (uiState.language == AppLanguage.VI) "TIẾN ĐỘ" else "PROGRESS",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF06B6D4),
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = stepTitle,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "${(progressPercent * 100).toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progressPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFF06B6D4),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }

                // 2+3+4. THẺ LỚN CHỨA: Văn bản mẫu + Lựa chọn + Nút hành động
                // → Gộp thành 1 Card để tất cả nằm gần nhau, không phải cuộn để tìm nút
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Header VĂN BẢN MẪU
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PurpleAccent.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "A",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = PurpleAccent
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (uiState.language == AppLanguage.VI) "VĂN BẢN MẪU" else "SAMPLE TEXT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PurpleAccent,
                                letterSpacing = 0.5.sp
                            )
                        }

                        // Văn bản mẫu ngắn gọn (đủ để cảm nhận, không cuộn dài)
                        Text(
                            text = if (uiState.language == AppLanguage.VI)
                                "Việc điều chỉnh kích thước chữ phù hợp giúp bảo vệ đôi mắt và tăng tốc độ đọc. Hãy đọc đoạn này tự nhiên và cho chúng tôi biết cảm nhận của bạn."
                            else
                                "Adjusting font size protects your eyes and improves reading speed. Read this text naturally and share your comfort level below.",
                            fontSize = (15 * sampleScale).sp,
                            lineHeight = (22 * sampleScale).sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Đường kẻ ngăn cách
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )

                        // CẢM NHẬN CỦA BẠN
                        Text(
                            text = if (uiState.language == AppLanguage.VI) "CẢM NHẬN CỦA BẠN" else "YOUR READING COMFORT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurpleAccent,
                            letterSpacing = 0.5.sp
                        )

                        val feelingOptions = if (uiState.language == AppLanguage.VI) {
                            listOf("Chữ quá nhỏ, bị mỏi mắt", "Vừa vặn, đọc rất thoải mái", "Chữ hơi to quá")
                        } else {
                            listOf("Too small, straining eyes", "Just right, very comfortable", "Slightly too large")
                        }

                        feelingOptions.forEachIndexed { index, optionText ->
                            val isSelected = selectedOptionIndex == index
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) PurpleAccent.copy(alpha = 0.10f)
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 0.dp,
                                        color = if (isSelected) PurpleAccent else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedOptionIndex = index }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = optionText,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = if (isSelected) 5.dp else 1.5.dp,
                                            color = if (isSelected) PurpleAccent else MaterialTheme.colorScheme.outline,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }

                        // NÚT SANG BƯỚC TIẾP / ÁP DỤNG — ngay dưới lựa chọn trong cùng card
                        if (currentStep < 3) {
                            Button(
                                onClick = { currentStep += 1 },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF06B6D4),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text(
                                    text = if (uiState.language == AppLanguage.VI)
                                        "Sang bước tiếp theo ➔ (${currentStep}/3)"
                                    else "Next Step ➔ (${currentStep}/3)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                // NÚT ÁP DỤNG - chỉ hiện ở bước 3 (bước cuối)
                AnimatedVisibility(
                    visible = currentStep == 3,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                    exit = fadeOut()
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Tóm tắt gợi ý
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF06B6D4).copy(alpha = 0.1f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFF06B6D4).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF06B6D4).copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "A",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp,
                                        color = Color(0xFF06B6D4)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (uiState.language == AppLanguage.VI)
                                            "GỢI Ý TỐI ƯU"
                                        else "RECOMMENDED",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF06B6D4),
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = if (uiState.language == AppLanguage.VI)
                                            "Cỡ chữ $recommendedLabel phù hợp với cảm nhận của bạn"
                                        else "Font scale $recommendedLabel matches your comfort level",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Nút áp dụng lớn
                        Button(
                            onClick = {
                                onApplyRecommendedScale(recommendedScale, recommendedLabel)
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PurpleAccent,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                        ) {
                            Text(
                                text = "A",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    "Áp dụng cỡ chữ phù hợp"
                                else "Apply Suitable Font Size",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

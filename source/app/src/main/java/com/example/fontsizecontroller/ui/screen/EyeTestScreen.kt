package com.example.fontsizecontroller.ui.screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.fontsizecontroller.ui.theme.MintSuccess
import com.example.fontsizecontroller.ui.theme.PurpleAccent

@Composable
fun EyeTestScreen(
    uiState: FontSizeUiState,
    onApplyRecommendedScale: (Float, String) -> Unit,
    onBackClick: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Quản lý trạng thái bài test
    var currentStep by remember { mutableIntStateOf(0) } // 0: Start, 1: Test 1, 2: Test 2, 3: Test 3, 4: Result
    var answer1 by remember { mutableStateOf<Int?>(null) } // 0: Dễ, 1: Hơi mờ, 2: Không đọc được
    var answer2 by remember { mutableStateOf<Int?>(null) } // 0: Tốt, 1: Bình thường, 2: Cần to hơn
    var answer3 by remember { mutableStateOf<Int?>(null) } // 0: Quá to, 1: Vừa vặn thoải mái, 2: Rất thích

    // Tính toán kết quả đề xuất dựa trên câu trả lời
    val recommendedScale = remember(answer1, answer2, answer3) {
        val difficultyScore = (answer1 ?: 0) + (answer2 ?: 0)
        when {
            difficultyScore >= 3 -> 1.35f
            difficultyScore >= 2 -> 1.25f
            difficultyScore >= 1 -> 1.15f
            else -> 1.00f
        }
    }

    val recommendedLabel = remember(recommendedScale) {
        when (recommendedScale) {
            1.00f -> "Default (1.00x)"
            1.15f -> "Large (1.15x)"
            1.25f -> "Custom (1.25x)"
            else -> "Extra Large (1.35x)"
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithLanguage(
                title = if (uiState.language == AppLanguage.VI) "Đo Thị Lực Thông Minh" else "Smart Eye Test",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Thanh tiến trình bài test
            if (currentStep in 1..3) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (uiState.language == AppLanguage.VI) "Câu hỏi $currentStep / 3" else "Question $currentStep / 3",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PurpleAccent
                            )
                        )
                        Text(
                            text = "${(currentStep * 33.3f).toInt()}%",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { currentStep / 3f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = PurpleAccent,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            when (currentStep) {
                0 -> {
                    // MÀN HÌNH BẮT ĐẦU
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(PurpleAccent.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RemoveRedEye,
                                    contentDescription = "Eye Test",
                                    tint = PurpleAccent,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = if (uiState.language == AppLanguage.VI) "Kiểm Tra Mắt Đọc Nhanh" else "Quick Reading Eye Test",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    "Chỉ mất 30 giây với 3 câu hỏi đọc trực quan để tìm ra kích thước chữ hoàn hảo nhất cho thị lực của bạn, giúp loại bỏ mỏi mắt khi sử dụng điện thoại."
                                else
                                    "Takes just 30 seconds with 3 visual reading questions to discover the ideal font size for your eyes, eliminating eye fatigue on your phone.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 22.sp
                                ),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { currentStep = 1 },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                            ) {
                                Text(
                                    text = if (uiState.language == AppLanguage.VI) "Bắt Đầu Đo Ngay ➔" else "Start Test Now ➔",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                1 -> {
                    // CÂU HỎI 1: Cỡ chữ nhỏ 0.85x
                    QuestionCard(
                        stepTitle = if (uiState.language == AppLanguage.VI) "Bài đọc 1: Cỡ chữ nhỏ (0.85x)" else "Test 1: Small Font (0.85x)",
                        sampleText = if (uiState.language == AppLanguage.VI)
                            "Khoảng cách đọc điện thoại lý tưởng của mắt người là từ 30cm đến 40cm. Bạn có nhìn thấy đoạn văn bản này một cách dễ dàng không?"
                        else
                            "The ideal reading distance from screen to eyes is 30cm to 40cm. Can you read this passage with complete ease?",
                        sampleScale = 0.85f,
                        options = if (uiState.language == AppLanguage.VI)
                            listOf("Đọc rất rõ ràng, thoải mái", "Hơi nhỏ, phải căng mắt", "Quá bé, mờ nhạt khó đọc")
                        else
                            listOf("Very clear and comfortable", "Slightly small, straining eyes", "Too small, blurry to read"),
                        selectedOptionIndex = answer1,
                        onOptionSelected = {
                            answer1 = it
                            currentStep = 2
                        }
                    )
                }

                2 -> {
                    // CÂU HỎI 2: Cỡ chữ chuẩn 1.05x
                    QuestionCard(
                        stepTitle = if (uiState.language == AppLanguage.VI) "Bài đọc 2: Cỡ chữ tiêu chuẩn (1.05x)" else "Test 2: Standard Font (1.05x)",
                        sampleText = if (uiState.language == AppLanguage.VI)
                            "Công nghệ sinh ra là để phục vụ cuộc sống của con người, không phân biệt độ tuổi hay tình trạng thị lực."
                        else
                            "Technology is created to serve human life, regardless of age or vision capabilities.",
                        sampleScale = 1.05f,
                        options = if (uiState.language == AppLanguage.VI)
                            listOf("Rất vừa vặn", "Đọc được nhưng muốn to hơn chút", "Vẫn còn hơi mỏi mắt")
                        else
                            listOf("Just right", "Readable but prefer larger", "Still causes eye fatigue"),
                        selectedOptionIndex = answer2,
                        onOptionSelected = {
                            answer2 = it
                            currentStep = 3
                        }
                    )
                }

                3 -> {
                    // CÂU HỎI 3: Cỡ chữ lớn 1.25x
                    QuestionCard(
                        stepTitle = if (uiState.language == AppLanguage.VI) "Bài đọc 3: Cỡ chữ phóng to (1.25x)" else "Test 3: Magnified Font (1.25x)",
                        sampleText = if (uiState.language == AppLanguage.VI)
                            "Kích thước chữ lớn và đậm giúp não bộ nhận diện ký tự nhanh hơn 25%, giảm áp lực điều tiết của mắt."
                        else
                            "Magnified, clear text allows the brain to recognize characters 25% faster, significantly relieving eye strain.",
                        sampleScale = 1.25f,
                        options = if (uiState.language == AppLanguage.VI)
                            listOf("Hơi to quá mức cần thiết", "Rất thích, cực kỳ dễ chịu và rõ nét", "Tuyệt vời, đây là mức tôi cần!")
                        else
                            listOf("Slightly too large", "Very pleasant and sharp", "Perfect, exactly what I need!"),
                        selectedOptionIndex = answer3,
                        onOptionSelected = {
                            answer3 = it
                            currentStep = 4
                        }
                    )
                }

                4 -> {
                    // MÀN HÌNH KẾT QUẢ VÀ ĐỀ XUẤT
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(MintSuccess.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = "Result",
                                    tint = MintSuccess,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = if (uiState.language == AppLanguage.VI) "Kết Quả Đánh Giá Thị Lực" else "Vision Assessment Result",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Thẻ đề xuất tỷ lệ phóng to
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = if (uiState.language == AppLanguage.VI) "MỨC PHÓNG ĐẠI TỐI ƯU CHO MẮT BẠN" else "OPTIMAL MAGNIFICATION FOR YOUR EYES",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = PurpleAccent
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = recommendedLabel,
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    "Dựa trên các câu trả lời, mắt của bạn có xu hướng mỏi khi đọc chữ nhỏ. Đặt cỡ chữ ở mức ${String.format(java.util.Locale.US, "%.2fx", recommendedScale)} sẽ giúp mắt thư giãn tối đa và không cần nheo mắt."
                                else
                                    "Based on your responses, your eyes strain on smaller text. Setting font scale to ${String.format(java.util.Locale.US, "%.2fx", recommendedScale)} will maximize eye relaxation.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 22.sp
                                ),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Nút áp dụng ngay cỡ chữ này
                            Button(
                                onClick = {
                                    onApplyRecommendedScale(recommendedScale, recommendedLabel)
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                            ) {
                                Text(
                                    text = if (uiState.language == AppLanguage.VI) "Áp Dụng Cỡ Chữ Này Ngay ➔" else "Apply Recommended Size Now ➔",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedButton(
                                onClick = {
                                    currentStep = 0
                                    answer1 = null
                                    answer2 = null
                                    answer3 = null
                                },
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.RestartAlt,
                                    contentDescription = "Restart",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = if (uiState.language == AppLanguage.VI) "Đo Lại Từ Đầu" else "Retake Test")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuestionCard(
    stepTitle: String,
    sampleText: String,
    sampleScale: Float,
    options: List<String>,
    selectedOptionIndex: Int?,
    onOptionSelected: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stepTitle,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Khung văn bản kiểm tra theo tỷ lệ mẫu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
            ) {
                Text(
                    text = sampleText,
                    fontSize = (16 * sampleScale).sp,
                    lineHeight = (24 * sampleScale).sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bạn cảm thấy thế nào?",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PurpleAccent
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Danh sách các lựa chọn cảm nhận
            options.forEachIndexed { index, optionText ->
                val isSelected = selectedOptionIndex == index
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) PurpleAccent.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) PurpleAccent else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onOptionSelected(index) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) PurpleAccent else MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        }
    }
}

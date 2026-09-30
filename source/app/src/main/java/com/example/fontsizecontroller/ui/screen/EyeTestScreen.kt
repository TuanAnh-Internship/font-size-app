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
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.Visibility
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
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
    val haptic = LocalHapticFeedback.current
    val scrollState = rememberScrollState()
    val isVi = uiState.language == AppLanguage.VI

    // Nếu đã từng làm bài kiểm tra, mặc định hiển thị kết quả cũ
    var showPreviousResult by remember(uiState.eyeTestDone) { mutableStateOf(uiState.eyeTestDone) }

    // Tiến trình: Bước 1, Bước 2, Bước 3
    var currentStep by remember { mutableIntStateOf(1) }

    // Câu trả lời cho Bước 1 (Cảm nhận khoảng cách 35-40cm): 0 = Chữ nhỏ mỏi mắt, 1 = Rõ ràng thoải mái, 2 = Chữ hơi to
    var step1Answer by remember { mutableIntStateOf(1) }

    // Câu trả lời cho Bước 2 (Bảng Snellen: dòng nhỏ nhất đọc rõ): 0 = Dòng 1 (Cỡ lớn), 1 = Dòng 2 (Cỡ vừa), 2 = Dòng 3 (Cỡ nhỏ)
    var step2Answer by remember { mutableIntStateOf(1) }

    // Đánh giá thị lực tự động qua EyeTestDiagnosticEngine (Clean Architecture)
    val diagnosticResult = remember(step1Answer, step2Answer, isVi) {
        com.example.fontsizecontroller.domain.EyeTestDiagnosticEngine.evaluate(step1Answer, step2Answer, isVi)
    }
    val recommendedScale = diagnosticResult.recommendedScale
    val recommendedLabel = diagnosticResult.recommendedLabel
    val diagnosticCategory = diagnosticResult.category
    val diagnosticAdvice = diagnosticResult.advice

    val progressPercent = currentStep / 3f

    // CỐ ĐỊNH FONTSCALE = 1.0F:
    // Đảm bảo nội dung bài đo mắt luôn hiển thị ở kích thước chữ chuẩn 1.00x,
    // không bị phóng to sẵn nếu người dùng đã chỉnh cỡ chữ ở Tab 1 trước đó.
    val currentDensity = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides remember(currentDensity.density) {
            Density(density = currentDensity.density, fontScale = 1.0f)
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBarWithLanguage(
                    title = if (isVi) "Kiểm Tra Thị Lực" else "Vision Eye Test",
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

                // THÔNG BÁO CỠ CHỮ CHUẨN DÀNH CHO BÀI TEST
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF06B6D4).copy(alpha = 0.10f))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color(0xFF06B6D4),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isVi)
                            "Màn hình này luôn giữ chữ ở kích thước chuẩn (1.00x) để kết quả đo mắt của bạn chính xác nhất."
                        else
                            "This screen maintains standard 1.00x text size so your eye test result is completely accurate.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }

            // ==========================================
            // KỊCH BẢN 1: ĐÃ CÓ KẾT QUẢ ĐO TRƯỚC ĐÓ
            // ==========================================
            if (showPreviousResult && uiState.eyeTestDone) {
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
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isVi) "KẾT QUẢ ĐO GẦN NHẤT" else "PREVIOUS RESULT",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PurpleAccent,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isVi)
                                "Lần đo trước: Cỡ chữ ${String.format(java.util.Locale.US, "%.2fx", uiState.eyeTestResultScale)} là mức nhìn dễ chịu nhất với mắt bạn."
                            else
                                "Previous test: Scale ${String.format(java.util.Locale.US, "%.2fx", uiState.eyeTestResultScale)} was the most comfortable for your eyes.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isVi)
                                "Bạn có thể bấm dùng lại ngay hoặc đo lại từ đầu nếu muốn kiểm tra lại mắt hôm nay."
                            else
                                "You can re-apply this scale directly or retake the test anytime.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nút Áp dụng lại
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onApplyRecommendedScale(
                                    uiState.eyeTestResultScale,
                                    String.format(java.util.Locale.US, "Cỡ %.2fx", uiState.eyeTestResultScale)
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isVi)
                                    "Dùng lại cỡ ${String.format(java.util.Locale.US, "%.2fx", uiState.eyeTestResultScale)}"
                                else
                                    "Use Scale ${String.format(java.util.Locale.US, "%.2fx", uiState.eyeTestResultScale)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Nút Đo lại
                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                showPreviousResult = false
                                currentStep = 1
                                step1Answer = 1
                                step2Answer = 1
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = "Retake",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isVi) "Đo lại từ đầu" else "Retake Test",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            } else {
                // ==========================================
                // KỊCH BẢN 2: QUY TRÌNH CHẨN ĐOÁN 3 BƯỚC
                // ==========================================

                // THANH TIẾN ĐỘ THÔNG MINH (CHỐNG TRÀN VĂN BẢN TRÊN FONT 200%)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF06B6D4)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$currentStep",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (isVi) "BƯỚC $currentStep TRÊN 3" else "STEP $currentStep OF 3",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF06B6D4),
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = when (currentStep) {
                                            1 -> if (isVi) "Kiểm tra độ rõ chữ" else "Reading Distance"
                                            2 -> if (isVi) "Chọn dòng chữ đọc được" else "Readability Ladder"
                                            else -> if (isVi) "Cỡ chữ phù hợp cho bạn" else "Your Ideal Font Size"
                                        },
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "${(progressPercent * 100).toInt()}%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF06B6D4)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

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
                }

                // NỘI DUNG TỪNG BƯỚC
                when (currentStep) {
                    // ------------------------------------------
                    // BƯỚC 1: KIỂM TRA ĐỘ MỎI MẮT Ở KHOẢNG CÁCH CHUẨN
                    // ------------------------------------------
                    1 -> {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.Visibility,
                                        contentDescription = null,
                                        tint = PurpleAccent,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isVi) "BƯỚC 1: ĐỌC THỬ ĐOẠN VĂN MẪU" else "STEP 1: SAMPLE TEXT READING",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = PurpleAccent,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Text(
                                    text = if (isVi)
                                        "📱 Bạn hãy cầm điện thoại cách mắt khoảng một gang tay như thói quen thường ngày, đọc thử đoạn văn này và cho biết cảm nhận nhé:"
                                    else
                                        "📱 Hold your phone comfortably as you normally do, read the sample text below and choose how your eyes feel:",
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                // Khung văn bản mẫu
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = if (isVi)
                                            "Đọc tin tức, trò chuyện cùng người thân hay xem thông báo mỗi ngày sẽ dễ chịu hơn rất nhiều khi màn hình có cỡ chữ vừa vặn với mắt của bạn."
                                        else
                                            "Reading news, chatting with family or checking messages feels much easier when your phone has the ideal text size for your eyes.",
                                        fontSize = 15.sp,
                                        lineHeight = 22.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                // 3 lựa chọn
                                val step1Options = if (isVi) {
                                    listOf(
                                        "Chữ nhỏ quá, tôi phải nheo mắt mới đọc được",
                                        "Cỡ chữ này vừa vặn, đọc rất dễ chịu",
                                        "Chữ hơi to so với mắt của tôi"
                                    )
                                } else {
                                    listOf(
                                        "Too small, I need to squint to read",
                                        "Just right, clear and very comfortable",
                                        "A bit too large for my preference"
                                    )
                                }

                                step1Options.forEachIndexed { index, text ->
                                    val isSelected = step1Answer == index
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (isSelected) PurpleAccent.copy(alpha = 0.12f)
                                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                            )
                                            .border(
                                                width = if (isSelected) 1.5.dp else 1.dp,
                                                color = if (isSelected) PurpleAccent else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                step1Answer = index
                                            }
                                            .padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = text,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .border(
                                                    width = if (isSelected) 6.dp else 1.5.dp,
                                                    color = if (isSelected) PurpleAccent else MaterialTheme.colorScheme.outline,
                                                    shape = CircleShape
                                                )
                                        )
                                    }
                                }

                                // Nút tiếp theo
                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        currentStep = 2
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06B6D4)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .defaultMinSize(minHeight = 50.dp)
                                ) {
                                    Text(
                                        text = if (isVi) "Tiếp tục kiểm tra ➔" else "Continue to Next Step ➔",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }

                    // ------------------------------------------
                    // BƯỚC 2: BẢNG ĐO THỊ LỰC SNELLEN (3 DÒNG GIẢM DẦN)
                    // ------------------------------------------
                    2 -> {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(20.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.RemoveRedEye,
                                        contentDescription = null,
                                        tint = Color(0xFF06B6D4),
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isVi) "BƯỚC 2: BẠN ĐỌC RÕ ĐẾN ĐÂU?" else "STEP 2: HOW SMALL CAN YOU READ?",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF06B6D4),
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Text(
                                    text = if (isVi)
                                        "Hãy nhìn 3 dòng chữ to nhỏ dưới đây và chọn dòng nhỏ nhất mà bạn vẫn đọc rõ ràng không bị hoa mắt:"
                                    else
                                        "Look at the 3 lines of text below and select the smallest one you can read comfortably:",
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                // Khung 3 dòng chữ to nhỏ thực tế
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (uiState.isDarkMode) Color(0xFF1E293B) else Color(0xFFF1F5F9)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // Dòng 1: Cỡ lớn (22sp)
                                        Column {
                                            Text(
                                                text = if (isVi) "DÒNG 1 (CHỮ TO NHẤT)" else "LINE 1 (LARGEST)",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PurpleAccent
                                            )
                                            Text(
                                                text = if (isVi) "1. Chúc bạn một ngày nhiều sức khỏe và niềm vui" else "1. Wishing you good health and a wonderful day",
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)))

                                        // Dòng 2: Cỡ vừa (16sp)
                                        Column {
                                            Text(
                                                text = if (isVi) "DÒNG 2 (CỠ VỪA PHẢI)" else "LINE 2 (MEDIUM)",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF06B6D4)
                                            )
                                            Text(
                                                text = if (isVi) "2. Chăm sóc và cho mắt nghỉ ngơi khi dùng máy" else "2. Remember to rest your eyes while reading on mobile",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)))

                                        // Dòng 3: Cỡ nhỏ (12sp)
                                        Column {
                                            Text(
                                                text = if (isVi) "DÒNG 3 (CỠ NHỎ HƠN)" else "LINE 3 (SMALL PRINT)",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF10B981)
                                            )
                                            Text(
                                                text = if (isVi) "3. Nhìn ra xa 20 giây và chớp mắt đều đặn để mắt không mỏi" else "3. Look away into the distance for 20 seconds to relax eyes",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }

                                // Lựa chọn dòng nhỏ nhất đọc được
                                val step2Options = if (isVi) {
                                    listOf(
                                        "Tôi chỉ đọc rõ Dòng 1 (Chữ to nhất)",
                                        "Tôi đọc tốt đến Dòng 2 (Cỡ vừa)",
                                        "Tôi nhìn rõ cả Dòng 3 (Chữ nhỏ nhất)"
                                    )
                                } else {
                                    listOf(
                                        "I can only read Line 1 (Largest)",
                                        "I can easily read down to Line 2 (Medium)",
                                        "I can clearly read Line 3 (Smallest)"
                                    )
                                }

                                step2Options.forEachIndexed { index, text ->
                                    val isSelected = step2Answer == index
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (isSelected) Color(0xFF06B6D4).copy(alpha = 0.12f)
                                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                            )
                                            .border(
                                                width = if (isSelected) 1.5.dp else 1.dp,
                                                color = if (isSelected) Color(0xFF06B6D4) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                step2Answer = index
                                            }
                                            .padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = text,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .border(
                                                    width = if (isSelected) 6.dp else 1.5.dp,
                                                    color = if (isSelected) Color(0xFF06B6D4) else MaterialTheme.colorScheme.outline,
                                                    shape = CircleShape
                                                )
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            currentStep = 1
                                        },
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .defaultMinSize(minHeight = 50.dp)
                                    ) {
                                        Text(
                                            text = if (isVi) "Quay lại" else "Back",
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            currentStep = 3
                                        },
                                        shape = RoundedCornerShape(14.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                                        modifier = Modifier
                                            .weight(2f)
                                            .defaultMinSize(minHeight = 50.dp)
                                    ) {
                                        Text(
                                            text = if (isVi) "Xem cỡ chữ cho bạn ➔" else "See Suggested Size ➔",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // ------------------------------------------
                    // BƯỚC 3: BÁO CÁO KẾT QUẢ CHẨN ĐOÁN & NÚT 1 CHẠM ÁP DỤNG
                    // ------------------------------------------
                    3 -> {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 })
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                // THẺ CHẨN ĐOÁN Y KHOA TRỰC QUAN
                                Card(
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.5.dp, Color(0xFF06B6D4).copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(20.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(RoundedCornerShape(10.dp))
                                                    .background(Color(0xFF06B6D4).copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Outlined.HealthAndSafety,
                                                    contentDescription = null,
                                                    tint = Color(0xFF06B6D4),
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = if (isVi) "GỢI Ý CỠ CHỮ DÀNH CHO BẠN" else "YOUR RECOMMENDED FONT SIZE",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color(0xFF06B6D4),
                                                    letterSpacing = 0.5.sp
                                                )
                                                Text(
                                                    text = diagnosticCategory,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFF06B6D4).copy(alpha = 0.08f))
                                                .padding(14.dp)
                                        ) {
                                            Text(
                                                text = diagnosticAdvice,
                                                fontSize = 13.sp,
                                                lineHeight = 19.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        // Mức cỡ chữ đề xuất
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(PurpleAccent.copy(alpha = 0.12f))
                                                .border(1.dp, PurpleAccent.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                                                .padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Text(
                                                    text = if (isVi) "MỨC CỠ CHỮ NÊN DÙNG" else "RECOMMENDED SIZE",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = PurpleAccent,
                                                    letterSpacing = 0.5.sp
                                                )
                                                Text(
                                                    text = recommendedLabel,
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = PurpleAccent
                                                )
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(PurpleAccent)
                                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = if (isVi) "Vừa mắt nhất" else "Best For You",
                                                    color = Color.White,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        // Khung xem trước trực tiếp cỡ chữ đề xuất
                                        Text(
                                            text = if (isVi) "XEM THỬ CHỮ TRÊN MÁY BẠN:" else "PREVIEW ON YOUR SCREEN:",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            letterSpacing = 0.5.sp
                                        )

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                                .padding(14.dp)
                                        ) {
                                            Text(
                                                text = if (isVi)
                                                    "Tin nhắn Zalo, danh bạ và các ứng dụng trên điện thoại sẽ hiển thị to rõ như thế này."
                                                else
                                                    "Messages, contacts and phone apps will appear clearly at this size.",
                                                fontSize = (15 * recommendedScale).sp,
                                                lineHeight = (22 * recommendedScale).sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }

                                // NÚT 1 CHẠM ÁP DỤNG NGAY CHO HỆ THỐNG
                                Button(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        onApplyRecommendedScale(recommendedScale, recommendedLabel)
                                    },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PurpleAccent,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .defaultMinSize(minHeight = 54.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isVi)
                                            "Dùng ngay $recommendedLabel cho máy"
                                        else
                                            "Apply $recommendedLabel Now",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }

                                // Nút đo lại
                                TextButton(
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        currentStep = 1
                                        step1Answer = 1
                                        step2Answer = 1
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isVi) "Đo lại từ đầu" else "Retake Test",
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
}

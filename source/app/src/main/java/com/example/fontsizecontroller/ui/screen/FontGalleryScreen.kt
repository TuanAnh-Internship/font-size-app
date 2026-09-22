package com.example.fontsizecontroller.ui.screen

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.FontSizeUiState
import com.example.fontsizecontroller.ui.component.TopAppBarWithLanguage
import com.example.fontsizecontroller.ui.theme.MintSuccess
import com.example.fontsizecontroller.ui.theme.PurpleAccent


// ──────────────────────────────────────────────────────────────────────────────
// Model cho font có sẵn trong hệ thống
// ──────────────────────────────────────────────────────────────────────────────
private data class SystemFontItem(
    val name: String,
    val tag: String,
    val descriptionVi: String,
    val descriptionEn: String,
    val fontFamily: FontFamily,
    val fontStyle: FontStyle = FontStyle.Normal,
    val tagColor: Color,
    val isInstalled: Boolean,          // true = font thực sự tìm thấy trên /system/fonts/
    val systemFileName: String = "",    // Tên file ttf để load Typeface (nếu cần)
    val categoryIndex: Int             // 0=Phổ biến, 1=Sans, 2=Serif, 3=Mono
)

// ──────────────────────────────────────────────────────────────────────────────
// Helper: kiểm tra font file có tồn tại trên thiết bị không và nạp Typeface thực
// ──────────────────────────────────────────────────────────────────────────────
private fun isSystemFontPresent(fileName: String): Boolean {
    return try {
        java.io.File("/system/fonts/$fileName").exists()
    } catch (_: Exception) { false }
}

private fun getSystemFontFamily(fileName: String, fallback: FontFamily = FontFamily.SansSerif): FontFamily {
    return try {
        val file = java.io.File("/system/fonts/$fileName")
        if (file.exists() && file.canRead()) {
            val androidTypeface = android.graphics.Typeface.createFromFile(file)
            FontFamily(androidTypeface)
        } else {
            fallback
        }
    } catch (_: Exception) {
        fallback
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Main Composable
// ──────────────────────────────────────────────────────────────────────────────
@Composable
fun FontGalleryScreen(
    uiState: FontSizeUiState,
    onApplyScale: (FontSizeOption) -> Unit,
    onBackClick: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var previewingFontIndex by remember { mutableStateOf<Int?>(null) }
    var showApplyDialog by remember { mutableStateOf<SystemFontItem?>(null) }
    var appliedFontName by remember { mutableStateOf<String?>(null) }

    val currentScale = uiState.selectedOption?.scale ?: uiState.currentScale ?: 1.0f

    val categories = if (uiState.language == AppLanguage.VI) {
        listOf("Tất cả", "Hiện đại", "Có chân", "Viết tay", "Máy tính")
    } else {
        listOf("All", "Modern", "Serif", "Handwriting", "Monospace")
    }

    // ── Danh sách font: Đọc trực tiếp từ file font /system/fonts/ có thật trên điện thoại ──
    val fontList = remember {
        listOf(
            SystemFontItem(
                name = "Samsung One",
                tag = if (uiState.language == AppLanguage.VI) "Chữ Samsung" else "Samsung Font",
                descriptionVi = "Kiểu chữ chính thức của Samsung, nét chữ tròn trịa, tối ưu cho màn hình điện thoại.",
                descriptionEn = "Official Samsung typeface, optimized for mobile screens.",
                fontFamily = getSystemFontFamily("SEC-Regular.ttf", FontFamily.SansSerif),
                tagColor = Color(0xFF0284C7),
                isInstalled = isSystemFontPresent("SEC-Regular.ttf"),
                systemFileName = "SEC-Regular.ttf",
                categoryIndex = 1
            ),
            SystemFontItem(
                name = "Noto Serif",
                tag = if (uiState.language == AppLanguage.VI) "Có chân" else "Serif",
                descriptionVi = "Kiểu chữ có chân trang trọng, nét chữ thanh đậm rõ ràng như trong sách báo cổ điển.",
                descriptionEn = "Classic serif typography, highly legible for reading articles and books.",
                fontFamily = getSystemFontFamily("NotoSerif-Regular.ttf", FontFamily.Serif),
                tagColor = Color(0xFF7C3AED),
                isInstalled = isSystemFontPresent("NotoSerif-Regular.ttf"),
                systemFileName = "NotoSerif-Regular.ttf",
                categoryIndex = 2
            ),
            SystemFontItem(
                name = "Dancing Script",
                tag = if (uiState.language == AppLanguage.VI) "Viết tay" else "Script",
                descriptionVi = "Kiểu chữ viết tay nghệ thuật, nét chữ uốn lượn mềm mại và duyên dáng.",
                descriptionEn = "Graceful calligraphy script with smooth and natural flowing curves.",
                fontFamily = getSystemFontFamily("DancingScript-Regular.ttf", FontFamily.Cursive),
                fontStyle = FontStyle.Italic,
                tagColor = Color(0xFFA855F7),
                isInstalled = isSystemFontPresent("DancingScript-Regular.ttf"),
                systemFileName = "DancingScript-Regular.ttf",
                categoryIndex = 3
            ),
            SystemFontItem(
                name = "Coming Soon",
                tag = if (uiState.language == AppLanguage.VI) "Nét vẽ tay" else "Handwriting",
                descriptionVi = "Kiểu chữ tự nhiên, nét vẽ mộc mạc và thân thiện, tạo cảm giác gần gũi.",
                descriptionEn = "Casual handwritten style, friendly and relaxed typography.",
                fontFamily = getSystemFontFamily("ComingSoon.ttf", FontFamily.Cursive),
                tagColor = Color(0xFFEC4899),
                isInstalled = isSystemFontPresent("ComingSoon.ttf"),
                systemFileName = "ComingSoon.ttf",
                categoryIndex = 3
            ),
            SystemFontItem(
                name = "Droid Sans Mono",
                tag = if (uiState.language == AppLanguage.VI) "Máy tính" else "Monospace",
                descriptionVi = "Kiểu chữ máy đánh chữ, các ký tự có độ rộng đều nhau, nhìn rõ từng con số.",
                descriptionEn = "Typewriter style with equal character widths, excellent for numbers and codes.",
                fontFamily = getSystemFontFamily("DroidSansMono.ttf", FontFamily.Monospace),
                tagColor = Color(0xFF475569),
                isInstalled = isSystemFontPresent("DroidSansMono.ttf"),
                systemFileName = "DroidSansMono.ttf",
                categoryIndex = 4
            ),
            SystemFontItem(
                name = "Source Sans",
                tag = if (uiState.language == AppLanguage.VI) "Thanh mảnh" else "Clean",
                descriptionVi = "Kiểu chữ thanh thoát, hiện đại, khoảng cách giữa các chữ rộng rãi dễ nhìn.",
                descriptionEn = "Elegant modern sans-serif with spacious and clear character spacing.",
                fontFamily = getSystemFontFamily("SourceSansPro-Regular.ttf", FontFamily.SansSerif),
                tagColor = Color(0xFF0D9488),
                isInstalled = isSystemFontPresent("SourceSansPro-Regular.ttf"),
                systemFileName = "SourceSansPro-Regular.ttf",
                categoryIndex = 1
            ),
            SystemFontItem(
                name = "Roboto",
                tag = if (uiState.language == AppLanguage.VI) "Mặc định" else "Default",
                descriptionVi = "Kiểu chữ tiêu chuẩn của hệ điều hành, nét thẳng gọn gàng, quen thuộc và dễ đọc.",
                descriptionEn = "Default Android typography, clean, balanced and familiar.",
                fontFamily = getSystemFontFamily("Roboto-Regular.ttf", FontFamily.SansSerif),
                tagColor = Color(0xFF6366F1),
                isInstalled = isSystemFontPresent("Roboto-Regular.ttf"),
                systemFileName = "Roboto-Regular.ttf",
                categoryIndex = 1
            )
        )
    }

    val filteredFonts = fontList.filter {
        selectedCategoryIndex == 0 || it.categoryIndex == selectedCategoryIndex
    }

    Scaffold(
        topBar = {
            TopAppBarWithLanguage(
                title = if (uiState.language == AppLanguage.VI) "Kiểu Phông Chữ" else "Font Styles",
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

            // ── HEADER thông tin ──
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PurpleAccent.copy(alpha = 0.08f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PurpleAccent.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PurpleAccent.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Aa", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = PurpleAccent)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (uiState.language == AppLanguage.VI)
                                "Kiểu chữ có sẵn trong máy"
                            else "Fonts available on your device",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (uiState.language == AppLanguage.VI)
                                "Lấy trực tiếp từ hệ thống điện thoại của bạn. Bấm Xem thử để xem các nét chữ khác nhau."
                            else "Loaded directly from your phone system. Tap Preview to test different typefaces.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // ── BỘ LỌC DANH MỤC ──
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(categories) { index, category ->
                    val isSelected = selectedCategoryIndex == index
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (isSelected) PurpleAccent else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable { selectedCategoryIndex = index }
                            .padding(horizontal = 16.dp, vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }



            // ── DANH SÁCH FONT CARDS ──
            filteredFonts.forEachIndexed { idx, fontItem ->
                val isApplied = appliedFontName == fontItem.name

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isApplied)
                            PurpleAccent.copy(alpha = 0.07f)
                        else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isApplied) 1.5.dp else 1.dp,
                            color = if (isApplied) PurpleAccent else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Watermark chữ A phía sau
                        Text(
                            text = "A",
                            fontSize = 80.sp,
                            fontFamily = fontItem.fontFamily,
                            fontStyle = fontItem.fontStyle,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 16.dp, top = 8.dp)
                        )

                        Column(modifier = Modifier.padding(20.dp)) {
                            // Tag + trạng thái installed
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(fontItem.tagColor.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = fontItem.tag,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = fontItem.tagColor,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                // Badge: INSTALLED / NOT INSTALLED
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(
                                            if (fontItem.isInstalled)
                                                MintSuccess.copy(alpha = 0.15f)
                                            else Color(0xFFEF4444).copy(alpha = 0.12f)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = if (fontItem.isInstalled) {
                                            if (uiState.language == AppLanguage.VI) "✓ Đã cài" else "✓ Installed"
                                        } else {
                                            if (uiState.language == AppLanguage.VI) "✕ Chưa có" else "✕ Not found"
                                        },
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (fontItem.isInstalled) MintSuccess else Color(0xFFEF4444)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Tên font (preview bằng chính fontFamily)
                            Text(
                                text = fontItem.name,
                                fontSize = 20.sp,
                                fontFamily = fontItem.fontFamily,
                                fontStyle = fontItem.fontStyle,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Mô tả
                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    fontItem.descriptionVi
                                else fontItem.descriptionEn,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Nút hành động
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Xem thử – toggle inline preview
                                OutlinedButton(
                                    onClick = {
                                        previewingFontIndex = if (previewingFontIndex == idx) null else idx
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                ) {
                                    Icon(
                                        imageVector = if (previewingFontIndex == idx)
                                            Icons.Outlined.VisibilityOff
                                        else Icons.Outlined.Visibility,
                                        contentDescription = "Preview",
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = if (previewingFontIndex == idx) {
                                            if (uiState.language == AppLanguage.VI) "Đóng" else "Close"
                                        } else {
                                            if (uiState.language == AppLanguage.VI) "Xem thử" else "Preview"
                                        },
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Áp dụng (hoặc tải về nếu chưa installed)
                                Button(
                                    onClick = {
                                        if (fontItem.isInstalled) {
                                            showApplyDialog = fontItem
                                        } else {
                                            openFontStore(context)
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (fontItem.isInstalled) fontItem.tagColor else Color(0xFFF59E0B),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .height(44.dp)
                                ) {
                                    if (!fontItem.isInstalled) {
                                        Icon(
                                            imageVector = Icons.Outlined.Download,
                                            contentDescription = "Download",
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (uiState.language == AppLanguage.VI) "Tải về" else "Download",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    } else if (isApplied) {
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = "Applied",
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (uiState.language == AppLanguage.VI) "Đã áp dụng" else "Applied",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    } else {
                                        Text(
                                            text = if (uiState.language == AppLanguage.VI) "Áp dụng" else "Apply",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            // ── INLINE PREVIEW DROPDOWN (xuất hiện ngay dưới nút Xem thử) ──
                            AnimatedVisibility(
                                visible = previewingFontIndex == idx,
                                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                            ) {
                                Column {
                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Đường kẻ ngăn cách
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(fontItem.tagColor.copy(alpha = 0.2f))
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = if (uiState.language == AppLanguage.VI)
                                            "XEM THỬ – ${fontItem.name.uppercase()}"
                                        else "PREVIEW – ${fontItem.name.uppercase()}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = fontItem.tagColor,
                                        letterSpacing = 0.5.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Mẫu văn bản Zalo
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Outlined.Chat,
                                                    contentDescription = "Chat",
                                                    tint = Color(0xFF0284C7),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "Zalo",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(5.dp))
                                            Text(
                                                text = if (uiState.language == AppLanguage.VI)
                                                    "\"Mẹ ơi, con đã cài phông ${fontItem.name} cho điện thoại rồi nhé!\""
                                                else
                                                    "\"Mom, I set the ${fontItem.name} font on your phone!\"",
                                                fontSize = (14 * currentScale).sp,
                                                fontFamily = fontItem.fontFamily,
                                                fontStyle = fontItem.fontStyle,
                                                lineHeight = (21 * currentScale).sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Mẫu tin tức
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                            .padding(12.dp)
                                    ) {
                                        Column {
                                            Text(
                                                text = if (uiState.language == AppLanguage.VI)
                                                    "Tiêu đề tin tức"
                                                else "News Headline",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = if (uiState.language == AppLanguage.VI)
                                                    "Công nghệ hỗ trợ người cao tuổi: Ứng dụng cỡ chữ thông minh giúp người già dùng điện thoại dễ dàng hơn."
                                                else
                                                    "Tech for seniors: Smart font size app helps elderly use smartphones with ease.",
                                                fontSize = (13 * currentScale).sp,
                                                fontFamily = fontItem.fontFamily,
                                                fontStyle = fontItem.fontStyle,
                                                lineHeight = (20 * currentScale).sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Trạng thái installed
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (fontItem.isInstalled) MintSuccess
                                                    else Color(0xFFEF4444)
                                                )
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (fontItem.isInstalled) {
                                                if (uiState.language == AppLanguage.VI)
                                                    "✓ Font này có sẵn trên thiết bị của bạn"
                                                else "✓ This font is available on your device"
                                            } else {
                                                if (uiState.language == AppLanguage.VI)
                                                    "✕ Font chưa được cài — bấm Tải về để cài"
                                                else "✕ Font not installed — tap Download to install"
                                            },
                                            fontSize = 11.sp,
                                            color = if (fontItem.isInstalled) MintSuccess else Color(0xFFEF4444)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ── THẺ MỞ CÀI ĐẶT PHÔNG CỦA MÁY ──
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .clickable { openSystemFontSettings(context) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    "Mở cài đặt phông chữ của máy"
                                else "Open Device Font Settings",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    "Chuyển đến phần Cài đặt hiển thị của điện thoại"
                                else "Go to system display settings",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Text("›", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // ── DIALOG HƯỚNG DẪN ÁP DỤNG FONT ──
    showApplyDialog?.let { font ->
        AlertDialog(
            onDismissRequest = { showApplyDialog = null },
            title = {
                Text(
                    text = if (uiState.language == AppLanguage.VI)
                        "Cài đặt phông chữ"
                    else "Set Font Style",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (uiState.language == AppLanguage.VI)
                            "Kiểu chữ \"${font.name}\" đã có sẵn trong điện thoại của bạn."
                        else
                            "Font \"${font.name}\" is already available on your phone.",
                        fontSize = 14.sp
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = if (uiState.language == AppLanguage.VI)
                                "Để đổi kiểu chữ trên toàn bộ máy, hệ điều hành yêu cầu chọn trong phần Cài đặt của điện thoại. Bấm nút bên dưới để mở ngay."
                            else
                                "To apply system-wide font style, Android requires selecting it in Settings. Tap below to proceed.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        appliedFontName = font.name
                        showApplyDialog = null
                        openSystemFontSettings(context)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent)
                ) {
                    Text(
                        text = if (uiState.language == AppLanguage.VI)
                            "Mở Cài đặt ➔"
                        else "Open Settings ➔",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showApplyDialog = null }) {
                    Text(text = if (uiState.language == AppLanguage.VI) "Đóng" else "Close")
                }
            }
        )
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Helpers
// ──────────────────────────────────────────────────────────────────────────────
private fun openSystemFontSettings(context: Context) {
    try {
        val intent = Intent().apply {
            setClassName("com.android.settings", "com.samsung.settings.FontStyleActivity")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        try {
            val intent = Intent().apply {
                action = "com.samsung.settings.FontStyleActivity"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            try {
                context.startActivity(Intent(Settings.ACTION_DISPLAY_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            } catch (_: Exception) { }
        }
    }
}

private fun openFontStore(context: Context) {
    // Thử mở Samsung Galaxy Store phần font
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setPackage("com.sec.android.app.samsungapps")
            data = android.net.Uri.parse("samsungapps://ProductDetail/fonts")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        try {
            // Fallback: mở Galaxy Store thông thường
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = android.net.Uri.parse("https://www.samsung.com/us/mobile/galaxy-store/")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) { }
    }
}

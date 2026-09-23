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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FormatColorText
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.fontsizecontroller.ui.theme.getSystemFontFamily
import com.example.fontsizecontroller.ui.theme.isSystemFontPresent

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
    val isInstalled: Boolean,
    val systemFileName: String = ""
)

// ──────────────────────────────────────────────────────────────────────────────
// Main Composable
// ──────────────────────────────────────────────────────────────────────────────
@Composable
fun FontGalleryScreen(
    uiState: FontSizeUiState,
    onApplyScale: (FontSizeOption) -> Unit,
    onApplyFontFamily: (String) -> Unit = {},
    onOpenSettings: () -> Unit,
    onToggleLanguage: () -> Unit,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val currentScale = uiState.selectedOption?.scale ?: uiState.currentScale ?: 1.0f

    // Danh sách kiểu phông hỗ trợ
    val fontList = remember {
        listOf(
            SystemFontItem(
                name = "Roboto",
                tag = if (uiState.language == AppLanguage.VI) "Mặc định" else "Default",
                descriptionVi = "Kiểu chữ chuẩn hệ điều hành, nét thẳng gọn gàng, quen thuộc và dễ đọc.",
                descriptionEn = "Default Android typography, clean, balanced and familiar.",
                fontFamily = FontFamily.Default,
                tagColor = Color(0xFF6366F1),
                isInstalled = true,
                systemFileName = "Roboto-Regular.ttf"
            ),
            SystemFontItem(
                name = "Samsung One",
                tag = if (uiState.language == AppLanguage.VI) "Chữ Samsung" else "Samsung Font",
                descriptionVi = "Kiểu chữ chính thức của Samsung, nét chữ tròn trịa, tối ưu cho màn hình điện thoại.",
                descriptionEn = "Official Samsung typeface, optimized for mobile screens.",
                fontFamily = getSystemFontFamily("SEC-Regular.ttf", FontFamily.SansSerif),
                tagColor = Color(0xFF0284C7),
                isInstalled = isSystemFontPresent("SEC-Regular.ttf"),
                systemFileName = "SEC-Regular.ttf"
            ),
            SystemFontItem(
                name = "Noto Serif",
                tag = if (uiState.language == AppLanguage.VI) "Trang trọng" else "Classic",
                descriptionVi = "Kiểu chữ trang trọng có chân, nét thanh đậm rõ ràng như trong sách báo.",
                descriptionEn = "Classic serif typography, highly legible for reading articles and books.",
                fontFamily = getSystemFontFamily("NotoSerif-Regular.ttf", FontFamily.Serif),
                tagColor = Color(0xFF7C3AED),
                isInstalled = isSystemFontPresent("NotoSerif-Regular.ttf"),
                systemFileName = "NotoSerif-Regular.ttf"
            ),
            SystemFontItem(
                name = "Dancing Script",
                tag = if (uiState.language == AppLanguage.VI) "Nghệ thuật" else "Artistic",
                descriptionVi = "Kiểu chữ viết tay nghệ thuật, nét chữ uốn lượn mềm mại và duyên dáng.",
                descriptionEn = "Graceful calligraphy script with smooth and natural flowing curves.",
                fontFamily = getSystemFontFamily("DancingScript-Regular.ttf", FontFamily.Cursive),
                fontStyle = FontStyle.Italic,
                tagColor = Color(0xFFA855F7),
                isInstalled = isSystemFontPresent("DancingScript-Regular.ttf"),
                systemFileName = "DancingScript-Regular.ttf"
            ),
            SystemFontItem(
                name = "Coming Soon",
                tag = if (uiState.language == AppLanguage.VI) "Thân thiện" else "Friendly",
                descriptionVi = "Kiểu chữ tự nhiên, nét vẽ mộc mạc và thân thiện, tạo cảm giác gần gũi.",
                descriptionEn = "Casual handwritten style, friendly and relaxed typography.",
                fontFamily = getSystemFontFamily("ComingSoon.ttf", FontFamily.Cursive),
                tagColor = Color(0xFFEC4899),
                isInstalled = isSystemFontPresent("ComingSoon.ttf"),
                systemFileName = "ComingSoon.ttf"
            ),
            SystemFontItem(
                name = "Droid Sans Mono",
                tag = if (uiState.language == AppLanguage.VI) "Rõ số" else "Numbers",
                descriptionVi = "Các ký tự có độ rộng đều nhau, nhìn rất rõ ràng từng con số.",
                descriptionEn = "Equal character widths, excellent for numbers and codes.",
                fontFamily = getSystemFontFamily("DroidSansMono.ttf", FontFamily.Monospace),
                tagColor = Color(0xFF475569),
                isInstalled = isSystemFontPresent("DroidSansMono.ttf"),
                systemFileName = "DroidSansMono.ttf"
            ),
            SystemFontItem(
                name = "Source Sans",
                tag = if (uiState.language == AppLanguage.VI) "Thanh mảnh" else "Clean",
                descriptionVi = "Kiểu chữ thanh thoát, hiện đại, khoảng cách giữa các chữ rộng rãi dễ nhìn.",
                descriptionEn = "Elegant modern sans-serif with spacious and clear character spacing.",
                fontFamily = getSystemFontFamily("SourceSansPro-Regular.ttf", FontFamily.SansSerif),
                tagColor = Color(0xFF0D9488),
                isInstalled = isSystemFontPresent("SourceSansPro-Regular.ttf"),
                systemFileName = "SourceSansPro-Regular.ttf"
            )
        )
    }

    // Kiểu chữ đang được chọn xem trước trong dropdown
    var selectedFontName by remember(uiState.selectedFontName) {
        mutableStateOf(uiState.selectedFontName)
    }

    val currentSelectedFont = remember(selectedFontName, fontList) {
        fontList.find { it.name.equals(selectedFontName, ignoreCase = true) } ?: fontList[0]
    }

    var dropdownExpanded by remember { mutableStateOf(false) }
    var isPreviewExpanded by remember { mutableStateOf(false) }

    // Kiểm tra xem font đang chọn xem có phải là font đang áp dụng toàn app hay không
    val isCurrentlyActive = uiState.selectedFontName.equals(currentSelectedFont.name, ignoreCase = true)

    Scaffold(
        topBar = {
            TopAppBarWithLanguage(
                title = if (uiState.language == AppLanguage.VI) "Kiểu Phông Chữ" else "Font Styles",
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

            // ── CARD DUY NHẤT: BỘ CHỌN DROPDOWN, XEM THỬ & ÁP DỤNG ──
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isCurrentlyActive) 2.dp else 1.dp,
                        color = if (isCurrentlyActive) MintSuccess else MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(22.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // ── TIÊU ĐỀ CARD & TRẠNG THÁI HIỆN TẠI ──
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(PurpleAccent.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FormatColorText,
                                    contentDescription = null,
                                    tint = PurpleAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (uiState.language == AppLanguage.VI)
                                        "Tùy chỉnh kiểu chữ"
                                    else "Customize Font Style",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (uiState.language == AppLanguage.VI)
                                        "Đang dùng: ${uiState.selectedFontName}"
                                    else "In use: ${uiState.selectedFontName}",
                                    fontSize = 12.sp,
                                    color = MintSuccess,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        if (isCurrentlyActive) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(MintSuccess)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (uiState.language == AppLanguage.VI) "✓ ĐANG DÙNG" else "✓ IN USE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                    // ── BỘ LỌC DROPDOWN DUY NHẤT ──
                    Column {
                        Text(
                            text = if (uiState.language == AppLanguage.VI)
                                "Chọn kiểu chữ:"
                            else "Select font style:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Box(modifier = Modifier.fillMaxWidth()) {
                            // Ô hiển thị dropdown có thể bấm vào
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .border(
                                        width = 1.dp,
                                        color = PurpleAccent.copy(alpha = 0.4f),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable { dropdownExpanded = true }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(currentSelectedFont.tagColor.copy(alpha = 0.15f))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = currentSelectedFont.tag,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = currentSelectedFont.tagColor
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = currentSelectedFont.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = currentSelectedFont.fontFamily,
                                        fontStyle = currentSelectedFont.fontStyle,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Open dropdown",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            // Menu thả xuống
                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                fontList.forEach { item ->
                                    val isItemCurrent = item.name.equals(selectedFontName, ignoreCase = true)
                                    val isItemActiveInApp =
                                        item.name.equals(uiState.selectedFontName, ignoreCase = true)

                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(
                                                        text = item.name,
                                                        fontSize = 15.sp,
                                                        fontWeight = if (isItemCurrent) FontWeight.Bold else FontWeight.Normal,
                                                        fontFamily = item.fontFamily,
                                                        fontStyle = item.fontStyle,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Text(
                                                        text = item.tag,
                                                        fontSize = 11.sp,
                                                        color = item.tagColor
                                                    )
                                                }

                                                if (isItemActiveInApp) {
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .background(MintSuccess.copy(alpha = 0.15f))
                                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                                    ) {
                                                        Text(
                                                            text = if (uiState.language == AppLanguage.VI) "Đang dùng" else "Active",
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = MintSuccess
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        onClick = {
                                            selectedFontName = item.name
                                            dropdownExpanded = false
                                        }
                                    )
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                                // Mục: + Cài đặt kiểu chữ mới...
                                DropdownMenuItem(
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Add,
                                            contentDescription = null,
                                            tint = PurpleAccent,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    },
                                    text = {
                                        Text(
                                            text = if (uiState.language == AppLanguage.VI)
                                                "+ Cài đặt kiểu chữ mới..."
                                            else
                                                "+ Install new font style...",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PurpleAccent
                                        )
                                    },
                                    onClick = {
                                        dropdownExpanded = false
                                        openSystemFontSettings(context)
                                    }
                                )
                            }
                        }
                    }

                    // ── PHẦN XEM THỰC TẾ (LIVE PREVIEW) ──
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (uiState.language == AppLanguage.VI)
                                        "MẪU XEM TRƯỚC"
                                    else "PREVIEW SAMPLE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = currentSelectedFont.tagColor,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Chữ cái mẫu kích thước lớn
                            Text(
                                text = "Aa Bb Cc 12345",
                                fontSize = 28.sp,
                                fontFamily = currentSelectedFont.fontFamily,
                                fontStyle = currentSelectedFont.fontStyle,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Câu văn mẫu co giãn theo cỡ chữ hệ thống
                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    "Cỡ chữ thông minh giúp người cao tuổi và gia đình đọc tin tức, nhắn tin dễ dàng hơn."
                                else
                                    "Smart font controller helps seniors and families read news and messages comfortably.",
                                fontSize = (14 * currentScale).sp,
                                lineHeight = (21 * currentScale).sp,
                                fontFamily = currentSelectedFont.fontFamily,
                                fontStyle = currentSelectedFont.fontStyle,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (uiState.language == AppLanguage.VI)
                                    currentSelectedFont.descriptionVi
                                else currentSelectedFont.descriptionEn,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // ── MẪU MỞ RỘNG (KHI NHẤN NÚT XEM THỬ) ──
                    AnimatedVisibility(
                        visible = isPreviewExpanded,
                        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                        exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            // Mẫu tin nhắn Zalo
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(PurpleAccent.copy(alpha = 0.08f))
                                    .padding(12.dp)
                            ) 
                            }
                        }

                        // ── HÀNG NÚT BẤM DUY NHẤT: XEM THỬ & ÁP DỤNG ──
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Nút Xem thử
                            OutlinedButton(
                                onClick = { isPreviewExpanded = !isPreviewExpanded },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPreviewExpanded)
                                        Icons.Outlined.VisibilityOff
                                    else Icons.Outlined.Visibility,
                                    contentDescription = "Preview",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isPreviewExpanded) {
                                        if (uiState.language == AppLanguage.VI) "Thu gọn" else "Collapse"
                                    } else {
                                        if (uiState.language == AppLanguage.VI) "Xem thử" else "Preview"
                                    },
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // Nút Áp dụng
                            Button(
                                onClick = {
                                    onApplyFontFamily(currentSelectedFont.name)
                                    android.widget.Toast.makeText(
                                        context,
                                        if (uiState.language == AppLanguage.VI)
                                            "Đã áp dụng kiểu chữ \"${currentSelectedFont.name}\" thành công!"
                                        else
                                            "Applied font \"${currentSelectedFont.name}\" successfully!",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCurrentlyActive) MintSuccess else PurpleAccent,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .weight(1.3f)
                                    .height(46.dp)
                            ) {
                                if (isCurrentlyActive) {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = "Applied",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (uiState.language == AppLanguage.VI) "Đã áp dụng" else "✓ Applied",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                } else {
                                    Text(
                                        text = if (uiState.language == AppLanguage.VI) "Áp dụng" else "Apply",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
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
                } catch (_: Exception) {
                }
            }
        }
    }

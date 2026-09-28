package com.example.fontsizecontroller.ui.component

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat

/**
 * Kính lúp soi chữ nhỏ bằng camera thật của thiết bị.
 * Tối ưu trải nghiệm cho người lớn tuổi, phụ huynh đọc vỉ thuốc, hóa đơn, giấy tờ.
 */
@Composable
fun RealCameraLoupeDialog(
    isVi: Boolean,
    fontScale: Float = 1.0f,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val haptic = LocalHapticFeedback.current

    // Kiểm tra và xin quyền Camera
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    // Điều khiển Camera
    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }

    // Mức thu phóng (Zoom ratio)
    var currentZoom by remember { mutableFloatStateOf(1.5f) }
    var maxZoomRatio by remember { mutableFloatStateOf(8.0f) }

    // Trạng thái đèn pin
    var isFlashlightOn by remember { mutableStateOf(false) }

    // Trạng thái đóng băng hình ảnh (Freeze frame để đọc không run tay)
    var frozenBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Chế độ tương phản cao (Đen - Vàng / Đảo màu cho mắt yếu)
    var isHighContrast by remember { mutableStateOf(false) }

    // Ma trận màu tương phản cao (Inverted contrast)
    val highContrastMatrix = remember {
        ColorMatrix(
            floatArrayOf(
                -1f, 0f, 0f, 0f, 255f,
                0f, -1f, 0f, 0f, 255f,
                0f, 0f, -1f, 0f, 255f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    }

    // Tự động tắt đèn pin khi đóng kính lúp
    DisposableEffect(Unit) {
        onDispose {
            try {
                cameraControl?.enableTorch(false)
            } catch (_: Exception) {}
        }
    }

    // Hệ số zoom kỹ thuật số khi thiết bị không hỗ trợ phần cứng vượt quá maxZoomRatio
    val digitalZoom = remember(currentZoom, maxZoomRatio) {
        if (maxZoomRatio <= 1.05f) currentZoom
        else if (currentZoom > maxZoomRatio) currentZoom / maxZoomRatio
        else 1.0f
    }

    Dialog(
        onDismissRequest = {
            try {
                cameraControl?.enableTorch(false)
            } catch (_: Exception) {}
            onDismissRequest()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        val baseDensity = LocalDensity.current
        val effectiveScale = if (fontScale > 0.5f) fontScale else baseDensity.fontScale
        CompositionLocalProvider(
            LocalDensity provides remember(baseDensity.density, effectiveScale) {
                Density(
                    density = baseDensity.density,
                    fontScale = effectiveScale
                )
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black
            ) {
            if (!hasCameraPermission) {
                // MÀN HÌNH YÊU CẦU QUYỀN CAMERA THÂN THIỆN
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF06B6D4).copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Videocam,
                                    contentDescription = null,
                                    tint = Color(0xFF06B6D4),
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Text(
                                text = if (isVi) "Cho phép dùng máy ảnh" else "Camera Permission Needed",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = if (isVi)
                                    "Kính lúp cần mở máy ảnh của điện thoại để phóng to các dòng chữ nhỏ trên vỉ thuốc, hóa đơn hoặc sách báo cho bạn."
                                else
                                    "The magnifier uses your device camera to zoom in on small medicine labels, receipts, and documents.",
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = Color(0xFF94A3B8),
                                textAlign = TextAlign.Center
                            )

                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    permissionLauncher.launch(Manifest.permission.CAMERA)
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06B6D4)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .defaultMinSize(minHeight = 52.dp)
                            ) {
                                Text(
                                    text = if (isVi) "Bật quyền máy ảnh ngay" else "Grant Camera Access",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            OutlinedButton(
                                onClick = onDismissRequest,
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF94A3B8)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = if (isVi) "Đóng lại" else "Cancel")
                            }
                        }
                    }
                }
            } else {
                // GIAO DIỆN KÍNH LÚP CAMERA THỰC TẾ
                Box(modifier = Modifier.fillMaxSize()) {
                    // KHUNG HÌNH CAMERA / ĐÓNG BĂNG HÌNH
                    if (frozenBitmap != null) {
                        // Hiển thị khung hình đã đóng băng để đọc không bị mỏi tay
                        Image(
                            bitmap = frozenBitmap!!.asImageBitmap(),
                            contentDescription = "Frozen Camera View",
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer(
                                    scaleX = digitalZoom,
                                    scaleY = digitalZoom
                                ),
                            contentScale = ContentScale.Crop,
                            colorFilter = if (isHighContrast) ColorFilter.colorMatrix(highContrastMatrix) else null
                        )
                    } else {
                        // Live CameraX Preview
                        AndroidView(
                            factory = { ctx ->
                                val previewView = PreviewView(ctx).apply {
                                    scaleType = PreviewView.ScaleType.FILL_CENTER
                                }
                                previewViewRef = previewView

                                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                                cameraProviderFuture.addListener({
                                    val cameraProvider = cameraProviderFuture.get()

                                    val preview = Preview.Builder()
                                        .build()
                                        .also {
                                            it.setSurfaceProvider(previewView.surfaceProvider)
                                        }

                                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                                    try {
                                        cameraProvider.unbindAll()
                                        val camera = cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            cameraSelector,
                                            preview
                                        )
                                        cameraControl = camera.cameraControl

                                        // Thiết lập zoom ban đầu
                                        camera.cameraControl.setZoomRatio(currentZoom)

                                        // Lấy max zoom của thiết bị
                                        val zoomState = camera.cameraInfo.zoomState.value
                                        if (zoomState != null) {
                                            maxZoomRatio = zoomState.maxZoomRatio.coerceAtMost(10f)
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }, ContextCompat.getMainExecutor(ctx))

                                previewView
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer(
                                    scaleX = digitalZoom,
                                    scaleY = digitalZoom
                                )
                        )

                        // Lớp phủ màu tương phản cao (nếu đang bật khi live)
                        if (isHighContrast) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0x33FDE047))
                            )
                        }
                    }

                    // THANH TIÊU ĐỀ TRÊN CÙNG
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xCC000000))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = null,
                                tint = Color(0xFF06B6D4),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isVi) "Kính Lúp Soi Chữ" else "Magnifier",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Nút Đóng
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                try {
                                    cameraControl?.enableTorch(false)
                                } catch (_: Exception) {}
                                onDismissRequest()
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xCC000000))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // THÔNG BÁO KHI ĐANG GIỮ YÊN HÌNH
                    AnimatedVisibility(
                        visible = frozenBitmap != null,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 80.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xE610B981))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = if (isVi) "Đang giữ yên hình ảnh — Đọc dễ dàng không rung tay" else "Image Paused — Easy reading without hand shake",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // BẢNG ĐIỀU KHIỂN DƯỚI CÙNG DÀNH CHO NGƯỜI LỚN TUỔI
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // KHỐI CHỌN CỠ PHÓNG TO (QUICK ZOOM BUTTONS)
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xDD1E293B)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isVi) "MỨC PHÓNG TO" else "ZOOM LEVEL",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "${String.format(java.util.Locale.US, "%.1f", currentZoom)}x",
                                        color = Color(0xFF06B6D4),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }

                                // 4 Nút phóng to nhanh cực to
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(IntrinsicSize.Min),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val zoomLevels = listOf(
                                        1.0f to (if (isVi) "1x\nChuẩn" else "1x\nNorm"),
                                        2.0f to (if (isVi) "2x\nTo" else "2x\nBig"),
                                        3.0f to (if (isVi) "3x\nRất to" else "3x\nLarge"),
                                        5.0f to (if (isVi) "5x\nSiêu to" else "5x\nHuge")
                                    )

                                    zoomLevels.forEach { (zoom, label) ->
                                        val isSelected = kotlin.math.abs(currentZoom - zoom) < 0.2f
                                        Button(
                                            onClick = {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                currentZoom = zoom
                                                val hwZoom = zoom.coerceIn(1.0f, maxZoomRatio)
                                                try {
                                                    cameraControl?.setZoomRatio(hwZoom)
                                                } catch (_: Exception) {}
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isSelected) Color(0xFF06B6D4) else Color(0xFF334155),
                                                contentColor = Color.White
                                            ),
                                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxHeight()
                                                .defaultMinSize(minHeight = 50.dp)
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                textAlign = TextAlign.Center,
                                                lineHeight = 14.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // CÁC NÚT TIỆN ÍCH HỖ TRỢ ĐỌC (ĐÈN PIN, GIỮ YÊN HÌNH, CHẾ ĐỘ MÀU)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // 1. NÚT ĐÈN PIN (TORCH)
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    val nextFlash = !isFlashlightOn
                                    isFlashlightOn = nextFlash
                                    cameraControl?.enableTorch(nextFlash)
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isFlashlightOn) Color(0xFFF59E0B) else Color(0xDD1E293B),
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .defaultMinSize(minHeight = 56.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = if (isFlashlightOn) Icons.Outlined.FlashOff else Icons.Outlined.FlashOn,
                                        contentDescription = "Torch",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isFlashlightOn)
                                            (if (isVi) "Tắt đèn" else "Light Off")
                                        else
                                            (if (isVi) "Bật đèn" else "Light On"),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                        lineHeight = 14.sp
                                    )
                                }
                            }

                            // 2. NÚT ĐÓNG BĂNG KHUNG HÌNH (FREEZE FRAME ĐỂ ĐỌC KHÔNG RUN TAY)
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    if (frozenBitmap != null) {
                                        // Mở lại live camera
                                        frozenBitmap = null
                                    } else {
                                        // Đóng băng khung hình hiện tại
                                        previewViewRef?.let { preview ->
                                            val bmp = preview.bitmap
                                            if (bmp != null) {
                                                frozenBitmap = bmp
                                            }
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (frozenBitmap != null) Color(0xFF10B981) else Color(0xDD1E293B),
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .defaultMinSize(minHeight = 56.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = if (frozenBitmap != null) Icons.Outlined.PlayArrow else Icons.Outlined.Pause,
                                        contentDescription = "Freeze",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (frozenBitmap != null)
                                            (if (isVi) "Soi tiếp" else "Resume")
                                        else
                                            (if (isVi) "Giữ hình" else "Pause Frame"),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                        lineHeight = 14.sp
                                    )
                                }
                            }

                            // 3. NÚT ĐỘ TƯƠNG PHẢN (ĐEN - VÀNG CHỐNG LÓA)
                            Button(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    isHighContrast = !isHighContrast
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isHighContrast) Color(0xFF8B5CF6) else Color(0xDD1E293B),
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .defaultMinSize(minHeight = 56.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Contrast,
                                        contentDescription = "Contrast",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isHighContrast)
                                            (if (isVi) "Màu chuẩn" else "Standard")
                                        else
                                            (if (isVi) "Tương phản" else "Contrast"),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
}

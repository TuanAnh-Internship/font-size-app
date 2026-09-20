package com.example.fontsizecontroller

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.findFontOption
import com.example.fontsizecontroller.model.fontSizeOptions
import com.example.fontsizecontroller.model.getDefaultFontOption
import com.example.fontsizecontroller.model.isLargeFont
import com.example.fontsizecontroller.ui.screen.PocScreen
import com.example.fontsizecontroller.ui.theme.FontSizeControllerTheme

private const val TAG = "FontSizeTest"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            FontSizeControllerTheme {
                var showPoc by remember { mutableStateOf(true) }

                if (showPoc) {
                    PocScreen(
                        onNavigateBack = { showPoc = false }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        FontSizeDemoScreen()
                        Button(
                            onClick = { showPoc = true },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(24.dp)
                        ) {
                            Text("Mở POC Screen")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSizeDemoScreen() {
    // State: lưu option đang được chọn, mặc định là "Normal"
    var selected by remember { mutableStateOf(getDefaultFontOption()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Font Size Controller") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            // ── Phần preview chữ theo scale đang chọn ────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Xin chào! Đây là cỡ chữ mẫu.",
                        fontSize = (16 * selected.scale).sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Scale: ${selected.scale}x  |  ${selected.label}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    if (isLargeFont(selected.scale)) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "⚡ Cỡ chữ lớn",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Chọn cỡ chữ:",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ── Danh sách 4 options ───────────────────────────────────
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(fontSizeOptions) { option ->
                    FontSizeCard(
                        option = option,
                        isSelected = option.label == selected.label,
                        onClick = {
                            selected = option
                            Log.d(TAG, "Selected: $option | isLarge: ${isLargeFont(option.scale)}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FontSizeCard(
    option: FontSizeOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surface

    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = option.label,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp
                )
                Text(
                    text = "scale = ${option.scale}",
                    fontSize = 12.sp,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
            if (isSelected) {
                Text(text = "✓", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}
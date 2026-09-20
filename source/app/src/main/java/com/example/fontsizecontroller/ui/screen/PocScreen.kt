package com.example.fontsizecontroller.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Screen phục vụ kiểm thử POC các tính năng thực nghiệm (như System API testbed).
 */
@Composable
fun PocScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Màn hình POC (Proof of Concept)",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Dành cho kiểm thử trực tiếp quyền WRITE_SETTINGS và System API FONT_SCALE.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

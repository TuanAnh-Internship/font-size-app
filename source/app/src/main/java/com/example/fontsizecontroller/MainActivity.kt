package com.example.fontsizecontroller

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fontsizecontroller.model.findFontOption
import com.example.fontsizecontroller.model.getDefaultFontOption
import com.example.fontsizecontroller.model.isLargeFont
import com.example.fontsizecontroller.ui.theme.FontSizeControllerTheme

private const val TAG = "FontSizeTest"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ── TEST các hàm trong FontSizeUtils ──────────────────────
        Log.d(TAG, "=== TEST FontSizeUtils ===")

        // Test isLargeFont
        Log.d(TAG, "isLargeFont(0.85f) = ${isLargeFont(0.85f)}")   // false
        Log.d(TAG, "isLargeFont(1.0f)  = ${isLargeFont(1.0f)}")    // false
        Log.d(TAG, "isLargeFont(1.15f) = ${isLargeFont(1.15f)}")   // true
        Log.d(TAG, "isLargeFont(1.30f) = ${isLargeFont(1.30f)}")   // true

        // Test findFontOption
        val found = findFontOption("Large")
        Log.d(TAG, "findFontOption(\"Large\")   = $found")          // FontSizeOption(label=Large, scale=1.15)
        val notFound = findFontOption("Unknown")
        Log.d(TAG, "findFontOption(\"Unknown\") = $notFound")       // null

        // Test getDefaultFontOption
        val default = getDefaultFontOption()
        Log.d(TAG, "getDefaultFontOption()    = $default")          // FontSizeOption(label=Normal, scale=1.0)

        Log.d(TAG, "=== TEST DONE ===")
        // ─────────────────────────────────────────────────────────

        enableEdgeToEdge()
        setContent {
            FontSizeControllerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FontSizeControllerTheme {
        Greeting("Android")
    }
}
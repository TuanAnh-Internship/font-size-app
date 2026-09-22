package com.example.fontsizecontroller

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fontsizecontroller.model.ScreenDestination
import com.example.fontsizecontroller.repository.SystemFontSettingsRepository
import com.example.fontsizecontroller.repository.SystemFontSettingsRepositoryImpl
import com.example.fontsizecontroller.repository.UserPreferencesRepository
import com.example.fontsizecontroller.repository.UserPreferencesRepositoryImpl
import com.example.fontsizecontroller.ui.component.AppBottomNavigationBar
import com.example.fontsizecontroller.ui.screen.AccessibilityScreen
import com.example.fontsizecontroller.ui.screen.EyeTestScreen
import com.example.fontsizecontroller.ui.screen.FontGalleryScreen
import com.example.fontsizecontroller.ui.screen.FontSizeScreen
import com.example.fontsizecontroller.ui.screen.OnboardingScreen
import com.example.fontsizecontroller.ui.screen.PermissionScreen
import com.example.fontsizecontroller.ui.screen.ResultScreen
import com.example.fontsizecontroller.ui.theme.FontSizeControllerTheme
import com.example.fontsizecontroller.viewmodel.FontSizeViewModel

class MainActivity : ComponentActivity() {

    private val repository: SystemFontSettingsRepository by lazy {
        SystemFontSettingsRepositoryImpl(applicationContext)
    }

    private val preferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepositoryImpl(applicationContext)
    }

    private val viewModel: FontSizeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FontSizeViewModel(repository, preferencesRepository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            val sharedBottomBar: @Composable () -> Unit = {
                AppBottomNavigationBar(
                    selectedTab = uiState.selectedTab,
                    language = uiState.language,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            }

            FontSizeControllerTheme(darkTheme = uiState.isDarkMode) {
                when (uiState.currentScreen) {
                    ScreenDestination.ONBOARDING -> {
                        OnboardingScreen(
                            language = uiState.language,
                            onStartClick = { viewModel.navigateTo(ScreenDestination.MAIN_FONT) },
                            onToggleLanguage = { viewModel.toggleLanguage() }
                        )
                    }

                    ScreenDestination.MAIN_FONT -> {
                        FontSizeScreen(
                            uiState = uiState,
                            onSelectOption = { viewModel.selectOption(it) },
                            onApply = { viewModel.applySelectedScale() },
                            onBackClick = { viewModel.navigateTo(ScreenDestination.ONBOARDING) },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onToggleDarkMode = { viewModel.toggleDarkMode() },
                            onOpenAccessibility = { viewModel.navigateTo(ScreenDestination.ACCESSIBILITY) },
                            onOpenDisplaySettings = { openDisplaySettings(this@MainActivity) },
                            onDismissOemDialog = { viewModel.dismissOemFallbackDialog() },
                            bottomBar = sharedBottomBar
                        )
                    }

                    ScreenDestination.EYE_TEST -> {
                        EyeTestScreen(
                            uiState = uiState,
                            onApplyRecommendedScale = { scale, label ->
                                viewModel.applyRecommendedScale(scale, label)
                            },
                            onBackClick = { viewModel.selectTab(0) },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onToggleDarkMode = { viewModel.toggleDarkMode() },
                            bottomBar = sharedBottomBar
                        )
                    }

                    ScreenDestination.FONT_GALLERY -> {
                        FontGalleryScreen(
                            uiState = uiState,
                            onApplyScale = { viewModel.selectAndApplyOption(it) },
                            onBackClick = { viewModel.selectTab(0) },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onToggleDarkMode = { viewModel.toggleDarkMode() },
                            bottomBar = sharedBottomBar
                        )
                    }

                    ScreenDestination.ACCESSIBILITY -> {
                        AccessibilityScreen(
                            uiState = uiState,
                            onReadingModeChange = { viewModel.setReadingMode(it) },
                            onToggleBold = { viewModel.toggleBoldPreview() },
                            onSelectScale = { viewModel.selectOption(it) },
                            onApplyScale = { viewModel.applySelectedScale() },
                            onBackClick = { viewModel.selectTab(0) },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onToggleDarkMode = { viewModel.toggleDarkMode() },
                            bottomBar = sharedBottomBar
                        )
                    }

                    ScreenDestination.PERMISSION -> {
                        PermissionScreen(
                            language = uiState.language,
                            onOpenSettings = { openManageWriteSettings(this@MainActivity) },
                            onDismiss = { viewModel.navigateTo(ScreenDestination.MAIN_FONT) },
                            onToggleLanguage = { viewModel.toggleLanguage() }
                        )
                    }

                    ScreenDestination.RESULT -> {
                        val currentScale = uiState.currentScale ?: 1.0f
                        val label = uiState.currentLabel
                        ResultScreen(
                            scale = currentScale,
                            label = label,
                            language = uiState.language,
                            onBackHome = { viewModel.navigateTo(ScreenDestination.MAIN_FONT) },
                            onTryAnother = { viewModel.navigateTo(ScreenDestination.MAIN_FONT) },
                            onToggleLanguage = { viewModel.toggleLanguage() }
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Khi người dùng cấp quyền từ Cài đặt hệ thống và quay lại ứng dụng
        viewModel.loadCurrentSettings()
    }

    private fun openManageWriteSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            // Fallback nếu máy không mở trực tiếp được package URI
            try {
                context.startActivity(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS))
            } catch (_: Exception) {
                context.startActivity(Intent(Settings.ACTION_SETTINGS))
            }
        }
    }

    private fun openDisplaySettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
            context.startActivity(intent)
        } catch (_: Exception) {
            try {
                context.startActivity(Intent(Settings.ACTION_SETTINGS))
            } catch (_: Exception) {}
        }
    }
}
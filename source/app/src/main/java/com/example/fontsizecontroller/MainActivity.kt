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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.fontsizecontroller.service.QuickControlNotificationManager
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

            LaunchedEffect(uiState.currentScale, uiState.isNotificationEnabled) {
                if (uiState.isNotificationEnabled) {
                    QuickControlNotificationManager.showNotification(
                        this@MainActivity,
                        uiState.currentScale
                    )
                }
            }

            val sharedBottomBar: @Composable () -> Unit = {
                AppBottomNavigationBar(
                    selectedTab = uiState.selectedTab,
                    language = uiState.language,
                    onTabSelected = { viewModel.selectTab(it) }
                )
            }

            val currentFontFamily = androidx.compose.runtime.remember(uiState.selectedFontName) {
                com.example.fontsizecontroller.ui.theme.getFontFamilyFromName(uiState.selectedFontName)
            }

            FontSizeControllerTheme(
                darkTheme = uiState.isDarkMode,
                fontFamily = currentFontFamily
            ) {
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
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onToggleDarkMode = { viewModel.toggleDarkMode() },
                            onOpenAccessibility = { viewModel.navigateTo(ScreenDestination.ACCESSIBILITY) },
                            onOpenDisplaySettings = { openDisplaySettings(this@MainActivity) },
                            onDismissOemDialog = { viewModel.dismissOemFallbackDialog() },
                            onOpenSettings = { viewModel.navigateTo(ScreenDestination.SETTINGS_AND_HELP) },
                            onResetDefault = { viewModel.showResetConfirmation() },
                            onConfirmReset = { viewModel.confirmResetDefault() },
                            onDismissResetDialog = { viewModel.dismissResetConfirmation() },
                            onSelectProfile = { id, scale -> viewModel.selectFamilyProfile(id, scale) },
                            bottomBar = sharedBottomBar
                        )
                    }

                    ScreenDestination.SETTINGS_AND_HELP -> {
                        com.example.fontsizecontroller.ui.screen.SettingsAndHelpScreen(
                            viewModel = viewModel,
                            uiState = uiState
                        )
                    }

                    ScreenDestination.UNSUPPORTED_ERROR -> {
                        com.example.fontsizecontroller.ui.screen.UnsupportedErrorScreen(
                            language = uiState.language,
                            isDarkMode = uiState.isDarkMode,
                            onCloseClick = { viewModel.navigateTo(ScreenDestination.MAIN_FONT) },
                            onBackClick = { viewModel.navigateTo(ScreenDestination.MAIN_FONT) },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onToggleDarkMode = { viewModel.toggleDarkMode() }
                        )
                    }

                    ScreenDestination.EYE_TEST -> {
                        EyeTestScreen(
                            uiState = uiState,
                            onApplyRecommendedScale = { scale, label ->
                                viewModel.applyRecommendedScale(scale, label)
                            },
                            onOpenSettings = { viewModel.navigateTo(ScreenDestination.SETTINGS_AND_HELP) },
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onToggleDarkMode = { viewModel.toggleDarkMode() },
                            bottomBar = sharedBottomBar
                        )
                    }

                    ScreenDestination.FONT_GALLERY -> {
                        FontGalleryScreen(
                            uiState = uiState,
                            onApplyScale = { viewModel.selectAndApplyOption(it) },
                            onApplyFontFamily = { viewModel.selectFontFamily(it) },
                            onOpenSettings = { viewModel.navigateTo(ScreenDestination.SETTINGS_AND_HELP) },
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
                            onOpenSettings = { viewModel.navigateTo(ScreenDestination.SETTINGS_AND_HELP) },
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
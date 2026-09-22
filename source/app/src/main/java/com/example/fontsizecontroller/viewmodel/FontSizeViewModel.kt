package com.example.fontsizecontroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.ApplyUiResult
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.FontSizeUiState
import com.example.fontsizecontroller.model.FontScaleApplyResult
import com.example.fontsizecontroller.model.ReadingMode
import com.example.fontsizecontroller.model.ScreenDestination
import com.example.fontsizecontroller.repository.SystemFontSettingsRepository
import com.example.fontsizecontroller.repository.UserPreferencesRepository
import com.example.fontsizecontroller.util.FontScaleMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel điều phối StateFlow và các hành động người dùng cho ứng dụng FontM.
 * Tuân thủ UDF: UI chỉ phát sự kiện (events), ViewModel điều phối logic và phát ra immutable State.
 * Tích hợp DataStore lưu trữ bền vững các cài đặt người dùng.
 */
class FontSizeViewModel(
    private val repository: SystemFontSettingsRepository,
    private val preferencesRepository: UserPreferencesRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(FontSizeUiState())
    val uiState: StateFlow<FontSizeUiState> = _uiState.asStateFlow()

    init {
        loadCurrentSettings()
        observePreferences()
    }

    /**
     * Lắng nghe và khôi phục các thiết lập cá nhân hóa từ DataStore.
     */
    private fun observePreferences() {
        val prefs = preferencesRepository ?: return
        viewModelScope.launch {
            prefs.userPreferencesFlow.collect { userPref ->
                _uiState.update { current ->
                    current.copy(
                        language = userPref.language,
                        isDarkMode = userPref.isDarkMode,
                        readingMode = userPref.readingMode,
                        isBoldPreview = userPref.isBoldPreview
                    )
                }
            }
        }
    }

    /**
     * Tải cấu hình cỡ chữ hiện tại từ Android Framework và kiểm tra quyền WRITE_SETTINGS.
     */
    fun loadCurrentSettings() {
        _uiState.update { it.copy(isLoading = true) }

        val scaleResult = repository.readFontScale()
        val canWrite = repository.canWriteSettings()

        scaleResult.fold(
            onSuccess = { scale ->
                val label = FontScaleMapper.toDisplayLabel(scale)
                val mappedOption = FontScaleMapper.mapScaleToOption(scale)
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        currentScale = scale,
                        currentLabel = label,
                        selectedOption = current.selectedOption ?: mappedOption,
                        canWriteSettings = canWrite,
                        result = ApplyUiResult.Idle
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        canWriteSettings = canWrite,
                        result = ApplyUiResult.Error(error.message ?: "Không đọc được cài đặt font")
                    )
                }
            }
        )
    }

    /**
     * Người dùng chọn một preset hoặc kéo thanh trượt (chỉ đổi State in-memory và thẻ Preview).
     */
    fun selectOption(option: FontSizeOption) {
        _uiState.update {
            it.copy(
                selectedOption = option,
                result = ApplyUiResult.Idle
            )
        }
    }

    /**
     * Bắt đầu quy trình ghi cỡ chữ vào hệ thống Android và xác thực lại (read-back verify).
     */
    fun applySelectedScale() {
        val option = _uiState.value.selectedOption ?: return

        // Nếu chưa có quyền, mở ngay màn hình hướng dẫn cấp quyền
        if (!_uiState.value.canWriteSettings) {
            _uiState.update {
                it.copy(
                    result = ApplyUiResult.PermissionRequired,
                    currentScreen = ScreenDestination.PERMISSION
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isApplying = true) }

            when (val result = repository.applyFontScale(option.scale)) {
                is FontScaleApplyResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isApplying = false,
                            currentScale = result.verifiedScale,
                            currentLabel = option.label,
                            result = ApplyUiResult.Success(result.verifiedScale, option.label),
                            currentScreen = ScreenDestination.RESULT
                        )
                    }
                    preferencesRepository?.setLastAppliedScale(result.verifiedScale)
                }

                FontScaleApplyResult.PermissionRequired -> {
                    _uiState.update {
                        it.copy(
                            isApplying = false,
                            canWriteSettings = false,
                            result = ApplyUiResult.PermissionRequired,
                            currentScreen = ScreenDestination.PERMISSION
                        )
                    }
                }

                FontScaleApplyResult.Unsupported -> {
                    _uiState.update {
                        it.copy(
                            isApplying = false,
                            result = ApplyUiResult.Unsupported,
                            showOemFallbackDialog = true
                        )
                    }
                }

                is FontScaleApplyResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isApplying = false,
                            result = ApplyUiResult.Error(result.throwable?.message ?: "Lỗi ghi hệ thống")
                        )
                    }
                }
            }
        }
    }

    /**
     * Chuyển đổi ngôn ngữ hiển thị (VI <-> EN) và lưu vào DataStore.
     */
    fun toggleLanguage() {
        val next = if (_uiState.value.language == AppLanguage.VI) {
            AppLanguage.EN
        } else {
            AppLanguage.VI
        }
        _uiState.update { it.copy(language = next) }
        viewModelScope.launch {
            preferencesRepository?.setLanguage(next)
        }
    }

    /**
     * Chuyển đổi chế độ sáng / tối (Dark mode) và lưu vào DataStore.
     */
    fun toggleDarkMode() {
        val next = !_uiState.value.isDarkMode
        _uiState.update { it.copy(isDarkMode = next) }
        viewModelScope.launch {
            preferencesRepository?.setDarkMode(next)
        }
    }

    /**
     * Thiết lập chế độ đọc bảo vệ thị lực (Chuẩn / Sepia / Tương phản cao) và lưu vào DataStore.
     */
    fun setReadingMode(mode: ReadingMode) {
        _uiState.update { it.copy(readingMode = mode) }
        viewModelScope.launch {
            preferencesRepository?.setReadingMode(mode)
        }
    }

    /**
     * Bật/tắt mô phỏng chữ đậm (Bold text) và lưu vào DataStore.
     */
    fun toggleBoldPreview() {
        val next = !_uiState.value.isBoldPreview
        _uiState.update { it.copy(isBoldPreview = next) }
        viewModelScope.launch {
            preferencesRepository?.setBoldPreview(next)
        }
    }

    /**
     * Đóng hộp thoại hướng dẫn OEM Fallback.
     */
    fun dismissOemFallbackDialog() {
        _uiState.update { it.copy(showOemFallbackDialog = false) }
    }

    /**
     * Điều hướng chuyển đổi tab trên Bottom Navigation Bar.
     */
    fun selectTab(index: Int) {
        val destination = when (index) {
            0 -> ScreenDestination.MAIN_FONT
            1 -> ScreenDestination.EYE_TEST
            2 -> ScreenDestination.FONT_GALLERY
            3 -> ScreenDestination.ACCESSIBILITY
            else -> ScreenDestination.MAIN_FONT
        }
        _uiState.update {
            it.copy(
                selectedTab = index,
                currentScreen = destination
            )
        }
    }

    /**
     * Áp dụng cỡ chữ được đề xuất từ Bài kiểm tra thị lực thông minh.
     */
    fun applyRecommendedScale(scale: Float, label: String) {
        val option = FontSizeOption(label = label, scale = scale)
        _uiState.update {
            it.copy(
                selectedOption = option,
                selectedTab = 0,
                currentScreen = ScreenDestination.MAIN_FONT
            )
        }
    }

    /**
     * Điều hướng màn hình trong ứng dụng.
     */
    fun navigateTo(destination: ScreenDestination) {
        val tab = when (destination) {
            ScreenDestination.MAIN_FONT -> 0
            ScreenDestination.EYE_TEST -> 1
            ScreenDestination.FONT_GALLERY -> 2
            ScreenDestination.ACCESSIBILITY -> 3
            else -> _uiState.value.selectedTab
        }
        _uiState.update {
            it.copy(
                currentScreen = destination,
                selectedTab = tab
            )
        }
    }
}

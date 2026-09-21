package com.example.fontsizecontroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.ApplyUiResult
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.FontSizeUiState
import com.example.fontsizecontroller.model.FontScaleApplyResult
import com.example.fontsizecontroller.model.ScreenDestination
import com.example.fontsizecontroller.repository.SystemFontSettingsRepository
import com.example.fontsizecontroller.util.FontScaleMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel điều phối StateFlow và các hành động người dùng cho ứng dụng FontMaster.
 * Tuân thủ UDF: UI chỉ bắn events, ViewModel xử lý logic và phát ra immutable State.
 */
class FontSizeViewModel(
    private val repository: SystemFontSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FontSizeUiState())
    val uiState: StateFlow<FontSizeUiState> = _uiState.asStateFlow()

    init {
        loadCurrentSettings()
    }

    /**
     * Tải cấu hình cỡ chữ hiện tại và kiểm tra capability quyền WRITE_SETTINGS.
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
     * Người dùng chọn một preset (chỉ cập nhật State in-memory và thẻ Preview, KHÔNG ghi vào hệ thống).
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

        // Nếu chưa có quyền, mở ngay màn hình giải thích cấp quyền
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
                            result = ApplyUiResult.Unsupported
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
     * Chuyển đổi ngôn ngữ hiển thị (VI <-> EN).
     */
    fun toggleLanguage() {
        val next = if (_uiState.value.language == AppLanguage.VI) {
            AppLanguage.EN
        } else {
            AppLanguage.VI
        }
        _uiState.update { it.copy(language = next) }
    }

    /**
     * Chuyển đổi chế độ sáng / tối (Dark mode).
     */
    fun toggleDarkMode() {
        _uiState.update { it.copy(isDarkMode = !it.isDarkMode) }
    }

    /**
     * Điều hướng màn hình trong ứng dụng.
     */
    fun navigateTo(destination: ScreenDestination) {
        _uiState.update { it.copy(currentScreen = destination) }
    }
}

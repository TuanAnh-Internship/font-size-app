package com.example.fontsizecontroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.FontSizeStatus
import com.example.fontsizecontroller.model.FontSizeUiState
import com.example.fontsizecontroller.model.FontScaleResult
import com.example.fontsizecontroller.repository.FontSettingsRepository
import com.example.fontsizecontroller.util.FontScaleMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel điều phối StateFlow và các hành động người dùng cho màn hình quản lý cỡ chữ.
 */
class FontSizeViewModel(
    private val repository: FontSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FontSizeUiState())
    val uiState: StateFlow<FontSizeUiState> = _uiState.asStateFlow()

    init {
        loadCurrentSettings()
    }

    /**
     * Tải cấu hình cỡ chữ hiện tại và kiểm tra capability quyền.
     */
    fun loadCurrentSettings() {
        val scale = repository.getCurrentFontScale()
        val canWrite = repository.canWriteSettings()
        val mappedOption = FontScaleMapper.mapScaleToOption(scale)

        _uiState.update {
            it.copy(
                isLoading = false,
                currentScale = scale,
                selectedOption = it.selectedOption ?: mappedOption,
                canWriteSettings = canWrite,
                status = FontSizeStatus.Idle
            )
        }
    }

    /**
     * Người dùng chọn một preset (chỉ cập nhật State in-memory và Preview, không ghi hệ thống).
     */
    fun selectOption(option: FontSizeOption) {
        _uiState.update { it.copy(selectedOption = option) }
    }

    /**
     * Bắt đầu quy trình ghi cỡ chữ vào hệ thống và cập nhật kết quả.
     */
    fun applySelectedScale() {
        val target = _uiState.value.selectedOption ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isApplying = true) }
            val result = repository.applyFontScale(target.scale)
            _uiState.update {
                when (result) {
                    is FontScaleResult.Success -> it.copy(
                        isApplying = false,
                        currentScale = result.scale,
                        status = FontSizeStatus.Success,
                        message = "Đã áp dụng cỡ chữ thành công"
                    )
                    is FontScaleResult.PermissionRequired -> it.copy(
                        isApplying = false,
                        status = FontSizeStatus.PermissionRequired,
                        message = "Cần cấp quyền WRITE_SETTINGS để thay đổi cài đặt hệ thống"
                    )
                    is FontScaleResult.Error -> it.copy(
                        isApplying = false,
                        status = FontSizeStatus.Error,
                        message = result.message
                    )
                    is FontScaleResult.Unsupported -> it.copy(
                        isApplying = false,
                        status = FontSizeStatus.Unsupported,
                        message = "Thiết bị không hỗ trợ ghi trực tiếp FONT_SCALE"
                    )
                }
            }
        }
    }
}

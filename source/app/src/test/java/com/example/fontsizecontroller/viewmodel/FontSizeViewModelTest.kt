package com.example.fontsizecontroller.viewmodel

import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.ApplyUiResult
import com.example.fontsizecontroller.model.FontSizeOption
import com.example.fontsizecontroller.model.FontScaleApplyResult
import com.example.fontsizecontroller.model.ReadingMode
import com.example.fontsizecontroller.model.ScreenDestination
import com.example.fontsizecontroller.repository.SystemFontSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FontSizeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeSystemFontSettingsRepository
    private lateinit var viewModel: FontSizeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeSystemFontSettingsRepository(
            initialScale = 1.00f,
            canWrite = true
        )
        viewModel = FontSizeViewModel(
            repository = fakeRepository,
            preferencesRepository = null
        )
        testDispatcher.scheduler.advanceUntilIdle()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_loadsCurrentScaleAndPermission() = runTest(testDispatcher) {
        val state = viewModel.uiState.value
        assertEquals(1.00f, state.currentScale ?: 0f, 0.001f)
        assertTrue(state.canWriteSettings)
        assertFalse(state.isLoading)
    }

    @Test
    fun selectOption_updatesSelectedOption() = runTest(testDispatcher) {
        val option = FontSizeOption("Large", 1.15f)
        viewModel.selectOption(option)

        val state = viewModel.uiState.value
        assertNotNull(state.selectedOption)
        assertEquals(1.15f, state.selectedOption?.scale ?: 0f, 0.001f)
    }

    @Test
    fun toggleLanguage_switchesBetweenViAndEn() = runTest(testDispatcher) {
        assertEquals(AppLanguage.VI, viewModel.uiState.value.language)

        viewModel.toggleLanguage()
        assertEquals(AppLanguage.EN, viewModel.uiState.value.language)

        viewModel.toggleLanguage()
        assertEquals(AppLanguage.VI, viewModel.uiState.value.language)
    }

    @Test
    fun toggleDarkMode_togglesState() = runTest(testDispatcher) {
        assertFalse(viewModel.uiState.value.isDarkMode)

        viewModel.toggleDarkMode()
        assertTrue(viewModel.uiState.value.isDarkMode)

        viewModel.toggleDarkMode()
        assertFalse(viewModel.uiState.value.isDarkMode)
    }

    @Test
    fun selectTab_updatesTabIndex() = runTest(testDispatcher) {
        viewModel.selectTab(1)
        assertEquals(1, viewModel.uiState.value.selectedTab)

        viewModel.selectTab(3)
        assertEquals(3, viewModel.uiState.value.selectedTab)
    }

    @Test
    fun navigateTo_updatesCurrentScreen() = runTest(testDispatcher) {
        viewModel.navigateTo(ScreenDestination.SETTINGS_AND_HELP)
        assertEquals(ScreenDestination.SETTINGS_AND_HELP, viewModel.uiState.value.currentScreen)

        viewModel.navigateTo(ScreenDestination.MAIN_FONT)
        assertEquals(ScreenDestination.MAIN_FONT, viewModel.uiState.value.currentScreen)
    }

    @Test
    fun setReadingMode_updatesReadingMode() = runTest(testDispatcher) {
        viewModel.setReadingMode(ReadingMode.HIGH_CONTRAST)
        assertEquals(ReadingMode.HIGH_CONTRAST, viewModel.uiState.value.readingMode)

        viewModel.setReadingMode(ReadingMode.SEPIA)
        assertEquals(ReadingMode.SEPIA, viewModel.uiState.value.readingMode)
    }

    @Test
    fun toggleBoldPreview_togglesBoolean() = runTest(testDispatcher) {
        assertFalse(viewModel.uiState.value.isBoldPreview)

        viewModel.toggleBoldPreview()
        assertTrue(viewModel.uiState.value.isBoldPreview)

        viewModel.toggleBoldPreview()
        assertFalse(viewModel.uiState.value.isBoldPreview)
    }

    @Test
    fun applySelectedScale_success_updatesCurrentScale() = runTest(testDispatcher) {
        val option = FontSizeOption("Large", 1.25f)
        viewModel.selectOption(option)
        viewModel.applySelectedScale()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1.25f, state.currentScale ?: 0f, 0.001f)
        assertTrue(state.result is ApplyUiResult.Success)
    }

    @Test
    fun confirmResetDefault_appliesScale1_00x() = runTest(testDispatcher) {
        val option = FontSizeOption("Large", 1.40f)
        viewModel.selectOption(option)
        viewModel.applySelectedScale()
        advanceUntilIdle()
        assertEquals(1.40f, viewModel.uiState.value.currentScale ?: 0f, 0.001f)

        viewModel.confirmResetDefault()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1.00f, state.currentScale ?: 0f, 0.001f)
        assertTrue(state.result is ApplyUiResult.Success)
    }
}

/**
 * Fake implementation của SystemFontSettingsRepository phục vụ Unit Tests.
 */
class FakeSystemFontSettingsRepository(
    private var initialScale: Float = 1.0f,
    private var canWrite: Boolean = true
) : SystemFontSettingsRepository {

    override fun readFontScale(): Result<Float> = Result.success(initialScale)

    override fun canWriteSettings(): Boolean = canWrite

    override suspend fun applyFontScale(targetScale: Float): FontScaleApplyResult {
        if (!canWrite) {
            return FontScaleApplyResult.PermissionRequired
        }
        initialScale = targetScale
        return FontScaleApplyResult.Success(
            requestedScale = targetScale,
            verifiedScale = targetScale
        )
    }
}

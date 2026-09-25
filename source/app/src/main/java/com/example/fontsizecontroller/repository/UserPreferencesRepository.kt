package com.example.fontsizecontroller.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.fontsizecontroller.model.AppLanguage
import com.example.fontsizecontroller.model.ReadingMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "fontm_user_preferences")

data class UserPreferences(
    val language: AppLanguage = AppLanguage.VI,
    val isDarkMode: Boolean = false,
    val readingMode: ReadingMode = ReadingMode.STANDARD,
    val isBoldPreview: Boolean = false,
    val lastAppliedScale: Float? = null,
    val eyeTestDone: Boolean = false,
    val eyeTestResultScale: Float = 1.0f,
    val eyeTestResultStep: Int = 3,
    val selectedProfileId: String = "myself",
    val selectedFontName: String = "Roboto",
    val isNotificationEnabled: Boolean = true
)

interface UserPreferencesRepository {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun setLanguage(language: AppLanguage)
    suspend fun setDarkMode(isDarkMode: Boolean)
    suspend fun setReadingMode(mode: ReadingMode)
    suspend fun setBoldPreview(isBold: Boolean)
    suspend fun setLastAppliedScale(scale: Float)
    suspend fun setEyeTestResult(done: Boolean, scale: Float, step: Int)
    suspend fun setSelectedProfileId(profileId: String)
    suspend fun setSelectedFontName(fontName: String)
    suspend fun setNotificationEnabled(enabled: Boolean)
    suspend fun resetAllPreferences()
}

class UserPreferencesRepositoryImpl(
    private val context: Context
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val KEY_LANGUAGE = stringPreferencesKey("key_language")
        val KEY_DARK_MODE = booleanPreferencesKey("key_dark_mode")
        val KEY_READING_MODE = stringPreferencesKey("key_reading_mode")
        val KEY_BOLD_PREVIEW = booleanPreferencesKey("key_bold_preview")
        val KEY_LAST_APPLIED_SCALE = floatPreferencesKey("key_last_applied_scale")
        val KEY_EYE_TEST_DONE = booleanPreferencesKey("key_eye_test_done")
        val KEY_EYE_TEST_SCALE = floatPreferencesKey("key_eye_test_scale")
        val KEY_EYE_TEST_STEP = intPreferencesKey("key_eye_test_step")
        val KEY_SELECTED_PROFILE = stringPreferencesKey("key_selected_profile")
        val KEY_SELECTED_FONT_NAME = stringPreferencesKey("key_selected_font_name")
        val KEY_NOTIFICATION_ENABLED = booleanPreferencesKey("key_notification_enabled")
    }

    override val userPreferencesFlow: Flow<UserPreferences> = context.userDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val langString = preferences[PreferencesKeys.KEY_LANGUAGE] ?: AppLanguage.VI.name
            val language = try {
                AppLanguage.valueOf(langString)
            } catch (_: Exception) {
                AppLanguage.VI
            }

            val isDarkMode = preferences[PreferencesKeys.KEY_DARK_MODE] ?: false

            val readingModeString = preferences[PreferencesKeys.KEY_READING_MODE] ?: ReadingMode.STANDARD.name
            val readingMode = try {
                ReadingMode.valueOf(readingModeString)
            } catch (_: Exception) {
                ReadingMode.STANDARD
            }

            val isBold = preferences[PreferencesKeys.KEY_BOLD_PREVIEW] ?: false
            val lastScale = preferences[PreferencesKeys.KEY_LAST_APPLIED_SCALE]
            val eyeTestDone = preferences[PreferencesKeys.KEY_EYE_TEST_DONE] ?: false
            val eyeTestScale = preferences[PreferencesKeys.KEY_EYE_TEST_SCALE] ?: 1.0f
            val eyeTestStep = preferences[PreferencesKeys.KEY_EYE_TEST_STEP] ?: 3
            val profileId = preferences[PreferencesKeys.KEY_SELECTED_PROFILE] ?: "myself"
            val fontName = preferences[PreferencesKeys.KEY_SELECTED_FONT_NAME] ?: "Roboto"
            val isNotificationEnabled = preferences[PreferencesKeys.KEY_NOTIFICATION_ENABLED] ?: true

            UserPreferences(
                language = language,
                isDarkMode = isDarkMode,
                readingMode = readingMode,
                isBoldPreview = isBold,
                lastAppliedScale = lastScale,
                eyeTestDone = eyeTestDone,
                eyeTestResultScale = eyeTestScale,
                eyeTestResultStep = eyeTestStep,
                selectedProfileId = profileId,
                selectedFontName = fontName,
                isNotificationEnabled = isNotificationEnabled
            )
        }

    override suspend fun setLanguage(language: AppLanguage) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_LANGUAGE] = language.name
        }
    }

    override suspend fun setDarkMode(isDarkMode: Boolean) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_DARK_MODE] = isDarkMode
        }
    }

    override suspend fun setReadingMode(mode: ReadingMode) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_READING_MODE] = mode.name
        }
    }

    override suspend fun setBoldPreview(isBold: Boolean) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_BOLD_PREVIEW] = isBold
        }
    }

    override suspend fun setLastAppliedScale(scale: Float) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_LAST_APPLIED_SCALE] = scale
        }
    }

    override suspend fun setEyeTestResult(done: Boolean, scale: Float, step: Int) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_EYE_TEST_DONE] = done
            preferences[PreferencesKeys.KEY_EYE_TEST_SCALE] = scale
            preferences[PreferencesKeys.KEY_EYE_TEST_STEP] = step
        }
    }

    override suspend fun setSelectedProfileId(profileId: String) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_SELECTED_PROFILE] = profileId
        }
    }

    override suspend fun setSelectedFontName(fontName: String) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_SELECTED_FONT_NAME] = fontName
        }
    }

    override suspend fun setNotificationEnabled(enabled: Boolean) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_NOTIFICATION_ENABLED] = enabled
        }
    }

    override suspend fun resetAllPreferences() {
        context.userDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

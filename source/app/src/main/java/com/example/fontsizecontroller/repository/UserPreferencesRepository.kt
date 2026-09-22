package com.example.fontsizecontroller.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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
    val lastAppliedScale: Float? = null
)

interface UserPreferencesRepository {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun setLanguage(language: AppLanguage)
    suspend fun setDarkMode(isDarkMode: Boolean)
    suspend fun setReadingMode(mode: ReadingMode)
    suspend fun setBoldPreview(isBold: Boolean)
    suspend fun setLastAppliedScale(scale: Float)
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

            UserPreferences(
                language = language,
                isDarkMode = isDarkMode,
                readingMode = readingMode,
                isBoldPreview = isBold,
                lastAppliedScale = lastScale
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
}

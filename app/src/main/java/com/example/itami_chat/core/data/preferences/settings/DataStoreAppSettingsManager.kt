package com.example.itami_chat.core.data.preferences.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.itami_chat.core.domain.model.Theme
import com.example.itami_chat.core.domain.preferences.AppSettingsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreAppSettingsManager @Inject constructor(
    @ApplicationContext context: Context
): AppSettingsManager {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        PREFERENCES_DATASTORE_NAME
    )

    private val dataStore: DataStore<Preferences> = context.dataStore


    override suspend fun changeDarkModeState(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE_KEY] = enabled
        }
    }

    override suspend fun changeTheme(theme: Theme) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    override val isDarkMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_DARK_MODE_KEY] ?: false
    }

    override val currentTheme: Flow<Theme> = dataStore.data.map { preferences ->
        preferences[THEME_KEY]?.let { Theme.valueOf(it) } ?: Theme.Default
    }



    companion object {

        private const val PREFERENCES_DATASTORE_NAME = "app_settings_preferences"

        private val IS_DARK_MODE_KEY = booleanPreferencesKey("is_dark_theme")

        private val THEME_KEY = stringPreferencesKey("theme")

    }

}
package com.menak.login.data

import android.content.Context
import androidx.constraintlayout.motion.widget.Key
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val context: Context) {
    private object Keys {
        val DARK_MODE: Preferences.Key<Boolean> = booleanPreferencesKey("dark_mode")
    }

    val darkModeFlow: Flow<Boolean> =
        context.dataStore.data
            .catch { e ->
                if (e is IOException) emit(androidx.datastore.preferences.core.emptyPreferences())
                else throw e
            }
            .map { prefs ->
                prefs[Keys.DARK_MODE] ?: false
            }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DARK_MODE] = enabled
        }
    }
}
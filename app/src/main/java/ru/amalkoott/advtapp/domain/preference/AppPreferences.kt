package ru.amalkoott.advtapp.domain.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences(val context: Context){
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("appPreferences")
        private val APP_THEME_KEY = booleanPreferencesKey("appTheme")
        private val APP_NOTIFICATIONS = booleanPreferencesKey("notifications")
        private val APP_DAILY_NOTIFICATION = booleanPreferencesKey("dailyNotification")
        private val APP_PUSHES = booleanPreferencesKey("pushes")
    }

    val getAccessToken: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[APP_THEME_KEY] ?: true
    }

    val getNotificationToken: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[APP_NOTIFICATIONS] ?: true
    }
    val getDailyNotificationToken: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[APP_DAILY_NOTIFICATION] ?: false
    }
    val getPushesToken: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[APP_PUSHES] ?: true
    }

    suspend fun setTheme(token: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[APP_THEME_KEY] = token
        }
    }

    suspend fun setNotifications(token: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[APP_NOTIFICATIONS] = token
        }
    }
    suspend fun setDailyNotifications(token: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[APP_DAILY_NOTIFICATION] = token
        }
    }
    suspend fun setPushes(token: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[APP_PUSHES] = token
        }
    }

}

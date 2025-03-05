package fr.isen.straudo.smartcompanion2.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension sur Context pour créer DataStore (remplace createDataStore)
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferencesManager(private val context: Context) {

    // Récupération du DataStore depuis l'extension
    private val dataStore: DataStore<Preferences> = context.dataStore

    // Clé de préférence pour l'état de notification d'un événement
    private val EVENT_NOTIFICATION_KEY = booleanPreferencesKey("event_notification_key")

    // Fonction pour obtenir l'état de notification pour un événement
    fun getNotificationStatus(eventId: String): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey("event_notification_key_$eventId")] ?: false
        }
    }

    // Fonction pour définir l'état de notification pour un événement
    suspend fun setNotificationStatus(eventId: String, isNotified: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey("event_notification_key_$eventId")] = isNotified
        }
    }
}

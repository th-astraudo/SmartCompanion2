package fr.isen.straudo.smartcompanion2

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.isen.straudo.smartcompanion2.data.PreferencesManager
import fr.isen.straudo.smartcompanion2.data.NotificationHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = PreferencesManager(application.applicationContext)

    fun isEventNotified(eventId: String): Flow<Boolean> {
        return userPreferences.getNotificationStatus(eventId)
    }

    fun toggleEventNotification(eventId: String, eventName: String, context: Context) {
        viewModelScope.launch {
            val isCurrentlyNotified = isEventNotified(eventId).firstOrNull() ?: false
            userPreferences.setNotificationStatus(eventId, !isCurrentlyNotified)

            if (!isCurrentlyNotified) {
                NotificationHelper.scheduleNotification(eventId, eventName, context)
            }
        }
    }
}

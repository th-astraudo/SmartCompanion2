package fr.isen.straudo.smartcompanion2.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

object NotificationHelper {

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event Reminders"
            val descriptionText = "Notifications pour les événements"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("event_channel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun scheduleNotification(eventId: String, eventTitle: String, context: Context) {
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "eventId" to eventId,
                    "eventName" to eventTitle
                )
            )
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}

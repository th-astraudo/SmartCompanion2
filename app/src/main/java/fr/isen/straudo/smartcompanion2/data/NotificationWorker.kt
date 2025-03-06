package fr.isen.straudo.smartcompanion2.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import fr.isen.straudo.smartcompanion2.R

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val eventTitle = inputData.getString("eventTitle") ?: "Événement inconnu"
        Log.d("NotificationWorker", "Titre reçu : $eventTitle") // Debugging
        showNotification(eventTitle)
        return Result.success()
    }

    private fun showNotification(eventTitle: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crée le canal si nécessaire (Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "event_channel",
                "Notifications d'événements",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Rappels d'événements"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, "event_channel")
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("📅 Rappel d'événement")
            .setContentText("Ne manquez pas : $eventTitle")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}

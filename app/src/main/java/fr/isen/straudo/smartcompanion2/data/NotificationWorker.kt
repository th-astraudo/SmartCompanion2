package fr.isen.straudo.smartcompanion2.data

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import fr.isen.straudo.smartcompanion2.R

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val eventTitle = inputData.getString("eventTitle") ?: "Événement"
        showNotification(eventTitle)
        return Result.success()
    }

    private fun showNotification(eventTitle: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(applicationContext, "event_channel")
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("Rappel d'événement")
            .setContentText("Ne manquez pas : $eventTitle")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}

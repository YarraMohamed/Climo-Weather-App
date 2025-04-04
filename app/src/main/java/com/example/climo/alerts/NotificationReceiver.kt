package com.example.climo.alerts

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.climo.R
import com.example.climo.view.MainActivity
import java.util.concurrent.TimeUnit

class NotificationReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = NotificationManagerCompat.from(context)
        when(intent.action){
            "CANCEL_NOTIFICATION" -> {
                notificationManager.cancel(1)
            }
            "DISMISS_NOTIFICATION" -> {
                val description = intent.getStringExtra("description") ?: "Tap to open the app"

                val updatedBuilder = NotificationCompat.Builder(context, "Weather")
                    .setSmallIcon(R.mipmap.rounded_climo_logo)
                    .setContentTitle("Check Weather Condition!")
                    .setContentText(description)
                    .setContentIntent(
                        PendingIntent.getActivity(
                            context, 0,
                            Intent(context, MainActivity::class.java),
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    )
                    .setOnlyAlertOnce(true)
                    .setSound(null)
                    .setSilent(true)
                    .setAutoCancel(true)
                notificationManager.notify(1, updatedBuilder.build())
            }
            "SNOOZE_NOTIFICATION" -> {
                notificationManager.cancel(1)
                val description = intent.getStringExtra("description") ?: "Check the weather again!"
                Log.i("Worker", "onReceive: $description ")
                val snoozeWorkRequest = OneTimeWorkRequestBuilder<AlertsWorker>()
                    .setInitialDelay(1, TimeUnit.MINUTES)
                    .setInputData(workDataOf("description" to description))
                    .build()
                WorkManager.getInstance(context).enqueue(snoozeWorkRequest)
                Log.i("Worker", "onReceive: bulid the request ")
            }
        }
    }
}
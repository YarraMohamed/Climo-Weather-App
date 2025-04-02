package com.example.climo.alerts

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.climo.R
import com.example.climo.view.MainActivity

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
        }
    }
}
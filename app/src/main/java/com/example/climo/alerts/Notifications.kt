package com.example.climo.alerts

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.climo.R
import com.example.climo.view.MainActivity
import java.util.concurrent.TimeUnit

@SuppressLint("MissingPermission")
fun createNotification(context: Context, description: String) {
    createNotificationChannel(context)

    //Open intent
    val intent = Intent(context, MainActivity::class.java)
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    //Cancel intent
    val cancelIntent = Intent(context, NotificationReceiver::class.java).apply {
        action = "CANCEL_NOTIFICATION"
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    val cancelPendingIntent =
        PendingIntent.getBroadcast(context, 0, cancelIntent, PendingIntent.FLAG_IMMUTABLE)


    //Dismiss Intent
    val dismissIntent = Intent(context, NotificationReceiver::class.java).apply {
        action = "DISMISS_NOTIFICATION"
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    dismissIntent.putExtra("description", description)
    val dismissPendingIntent =
        PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_IMMUTABLE)

    //Snooze
    val snoozeIntent = Intent(context, NotificationReceiver::class.java).apply {
        action = "SNOOZE_NOTIFICATION"
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    snoozeIntent.putExtra("description",description)

    val snoozePendingIntent = PendingIntent.getBroadcast(
        context, 1, snoozeIntent,  PendingIntent.FLAG_IMMUTABLE)

    //Notification
    val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "Weather")
        .setSmallIcon(R.mipmap.rounded_climo_logo)
        .setContentTitle("Check Weather Condition Now!")
        .setContentText(description)
        .setFullScreenIntent(pendingIntent, true)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setDefaults(Notification.DEFAULT_VIBRATE)
        .setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.weather_news_sound))
        .addAction(R.drawable.alert_icon, "Cancel", cancelPendingIntent)
        .addAction(R.drawable.alert_icon, "Dismiss", dismissPendingIntent)
        .addAction(R.drawable.alert_icon, "Snooze", snoozePendingIntent)
    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(1, builder.build())
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Weather"
        val descriptionText = "Channel for my app notifications"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("Weather", name, importance).apply {
            description = descriptionText
            enableLights(true)
            enableVibration(true)
            val soundUri =
                Uri.parse("android.resource://" + context.packageName + "/" + R.raw.weather_news_sound)
            setSound(
                soundUri,
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
        }

        val notificationManager: NotificationManager =
            context.getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}


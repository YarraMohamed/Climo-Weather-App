package com.example.climo.alerts

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.climo.R
import com.example.climo.alerts.viewmodel.AlertsViewModel
import com.example.climo.data.Repository
import com.example.climo.data.RepositoryImp
import com.example.climo.data.db.AppDatabase
import com.example.climo.data.local.AlertsLocalDataSourceImp
import com.example.climo.data.local.FavouritesLocalDataSourceImp
import com.example.climo.data.local.WeatherLocalDataSourceImp
import com.example.climo.data.remote.RetrofitHelper
import com.example.climo.data.remote.WeatherRemoteDataSourceImp
import com.example.climo.utilities.parseDateTime
import com.example.climo.utilities.toAlerts
import com.example.climo.view.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class AlertsWorker(private val context: Context , private  val workerParameters: WorkerParameters)
    : CoroutineWorker(context,workerParameters) {

    val alertsDAO = AppDatabase.getInstance(context).getAlertsDao()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        try{
            val alertString = inputData.getString("alert")
                ?: return Result.failure(workDataOf("Error" to "No alert found"))
            val alert = toAlerts(alertString)
            Log.i("Worker", "doWork: alert is $alert ")
            val address = alert.address
            val location = convertAddress(address)
            if(location==null){
                alertsDAO.deleteSomeAlert(alert.startTime, alert.endTime, alert.date)
                return Result.failure(workDataOf("Error" to "No location found"))
            } else{
                val now = LocalDateTime.now()
                val endDateTime = parseDateTime(alert.date, alert.endTime)
                if(now.isAfter(endDateTime)){
                    return Result.failure(workDataOf("Error" to "Outside allowed time range"))
                }else {
                    val apiResult = RetrofitHelper.weatherService.getCurrentWeatherDetails(location.latitude,location.longitude)
                    val weatherStatus = apiResult.weather.get(0).description
                    Log.i("Worker", "doWork: $weatherStatus")
                    createNotification(weatherStatus)
                    alertsDAO.deleteSomeAlert(alert.startTime, alert.endTime, alert.date)
                    return Result.success(workDataOf("Status" to weatherStatus))
                }
            }
        }catch (ex:Exception){
            Log.i("Worker", "doWork: ${ex.message} ")
            return Result.failure(workDataOf("Error" to ex.message))
        }
    }

    private fun convertAddress(address: String): Location? {
        val geocoder = Geocoder(context)
        val locationList= geocoder.getFromLocationName(address, 1)

        return if (!locationList.isNullOrEmpty()) {
            val firstAddress = locationList[0]
            val location = Location("Geocoder").apply {
                latitude = firstAddress.latitude
                longitude = firstAddress.longitude
            }
            location
        } else {
            null
        }
    }

    @SuppressLint("MissingPermission")
    private fun createNotification(description:String){
        createNotificationChannel()

        //Open intent
        val intent = Intent(context, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        //Cancel intent
        val cancelIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = "CANCEL_NOTIFICATION"
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val cancelPendingIntent = PendingIntent.getBroadcast(context, 0, cancelIntent, PendingIntent.FLAG_IMMUTABLE)


        //Dismiss Intent
        val dismissIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = "DISMISS_NOTIFICATION"
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        dismissIntent.putExtra("description",description)
        val dismissPendingIntent = PendingIntent.getBroadcast(context, 0, dismissIntent, PendingIntent.FLAG_IMMUTABLE)


        //Notification
        val builder : NotificationCompat.Builder = NotificationCompat.Builder(context,"Weather")
            .setSmallIcon(R.mipmap.rounded_climo_logo)
            .setContentTitle("Check Weather Condition Now!")
            .setContentText(description)
            .setFullScreenIntent(pendingIntent, true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.weather_news_sound))
            .addAction(R.drawable.alert_icon,"Cancel",cancelPendingIntent)
            .addAction(R.drawable.alert_icon,"Dismiss",dismissPendingIntent)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, builder.build())
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Weather"
            val descriptionText = "Channel for my app notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Weather", name, importance).apply {
                description = descriptionText
                enableLights(true)
                enableVibration(true)
                val soundUri = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.weather_news_sound)
                setSound(
                    soundUri,
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
            }

            val notificationManager: NotificationManager = context.getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


    }
}
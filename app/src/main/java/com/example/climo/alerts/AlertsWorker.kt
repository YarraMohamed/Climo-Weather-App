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
        try {
            val alertString = inputData.getString("alert")
            val description = inputData.getString("description")
            if(alertString==null && description==null){
                Log.i("Worker", "doWork:all is null ")
                return Result.failure(workDataOf("Error" to "No alert found"))
            }
            else if(description == null){
                Log.i("Worker", "doWork:description is null ")
                val alert = toAlerts(alertString!!)
                Log.i("Worker", "doWork: alert is $alert ")
                val address = alert.address
                val location = convertAddress(address)
                if (location == null) {
                    alertsDAO.deleteSomeAlert(alert.startTime, alert.endTime, alert.date)
                    return Result.failure(workDataOf("Error" to "No location found"))
                } else {
                    val now = LocalDateTime.now()
                    val endDateTime = parseDateTime(alert.date, alert.endTime)
                    if (now.isAfter(endDateTime)) {
                        return Result.failure(workDataOf("Error" to "Outside allowed time range"))
                    } else {
                        val apiResult = RetrofitHelper.weatherService.getCurrentWeatherDetails(
                            location.latitude,
                            location.longitude,
                            units = ""
                        )
                        val weatherStatus = apiResult.weather.get(0).description
                        Log.i("Worker", "doWork: $weatherStatus")
                        createNotification(context, weatherStatus)
                        alertsDAO.deleteSomeAlert(alert.startTime, alert.endTime, alert.date)
                        return Result.success(workDataOf("Status" to weatherStatus))
                    }
                }
            }else{
                Log.i("Worker", "doWork:else is null ")
                createNotification(context,description)
                return Result.success()
            }
        } catch (ex: Exception) {
            Log.i("Worker", "doWork: ${ex.message} ")
            return Result.failure(workDataOf("Error" to ex.message))
        }
    }

    private fun convertAddress(address: String): Location? {
        val geocoder = Geocoder(context)
        val locationList = geocoder.getFromLocationName(address, 1)

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

}

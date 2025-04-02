package com.example.climo.alerts

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.climo.data.remote.RetrofitHelper
import com.example.climo.utilities.parseDateTime
import com.example.climo.utilities.toAlerts
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime

class AlertsWorker(private val context: Context , private  val workerParameters: WorkerParameters)
    : CoroutineWorker(context,workerParameters) {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        try{
            val alertString = inputData.getString("alert")
                ?: return Result.failure(workDataOf("Error" to "No alert found"))
            val alert = toAlerts(alertString)
            val address = alert.address
            val location = convertAddress(address)
            if(location==null){
                return Result.failure(workDataOf("Error" to "No location found"))
            } else{

                val now = LocalDateTime.now()
                val alertDateTime = parseDateTime(alert.date, alert.startTime)
                val endDateTime = parseDateTime(alert.date, alert.endTime)
                if(now.isBefore(alertDateTime) && now.isAfter(endDateTime)){
                    return Result.failure(workDataOf("Error" to "Outside allowed time range"))
                }else{
                    val apiResult = RetrofitHelper.weatherService.getCurrentWeatherDetails(location.latitude,location.longitude)
                    val weatherStatus = apiResult.weather.get(0).description
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

}
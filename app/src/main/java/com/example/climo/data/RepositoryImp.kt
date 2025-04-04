package com.example.climo.data

import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.climo.alerts.AlertsWorker
import com.example.climo.data.local.AlertsLocalDataSource
import com.example.climo.data.local.FavouritesLocalDataSource
import com.example.climo.data.local.OptionsLocalDataSource
import com.example.climo.data.local.WeatherLocalDataSource
import com.example.climo.data.remote.WeatherRemoteDataSource
import com.example.climo.model.Alerts
import com.example.climo.model.CurrentWeather
import com.example.climo.model.DailyDetails
import com.example.climo.model.Deatils
import com.example.climo.model.Favourites
import com.example.climo.model.HorulyDetails
import com.example.climo.model.WeatherList
import com.example.climo.model.WeatherStatus
import com.example.climo.utilities.fromAlerts
import com.example.climo.utilities.parseDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class RepositoryImp private constructor (
    private val weatherRemoteDataSourceImp: WeatherRemoteDataSource,
    private val favouritesLocalDataSourceImp: FavouritesLocalDataSource,
    private val weatherLocalDataSourceImp:WeatherLocalDataSource,
    private val alertsLocalDataSourceImp:AlertsLocalDataSource,
    private val optionsLocalDataSourceImp: OptionsLocalDataSource):Repository {


    override suspend fun getCurrentWeather(lat:Double,lon:Double,unit:String): Flow<CurrentWeather> {
        return weatherRemoteDataSourceImp.getCurrentWeather(lat,lon,unit)
    }

    override suspend fun getCurrentForecast(lat: Double, lon: Double,unit: String): Flow<WeatherList> {
        return weatherRemoteDataSourceImp.getCurrentWeatherForecast(lat,lon,unit)
    }

    override suspend fun getFavourites(): Flow<List<Favourites>> {
        return favouritesLocalDataSourceImp.getFavouriteList()
    }

    override suspend fun addFavourite(favourite: Favourites) {
        return favouritesLocalDataSourceImp.addFav(favourite)
    }

    override suspend fun deleteFavourite(favourite: Favourites) {
        return favouritesLocalDataSourceImp.deleteFav(favourite)
    }

    override suspend fun getWeatherStatus(lat: Double, lon: Double): Flow<WeatherStatus?> {
        return weatherLocalDataSourceImp.getWeatherStatus(lat,lon)
    }

    override suspend fun insertWeatherStatus(weatherStatus: WeatherStatus) {
        return weatherLocalDataSourceImp.insertWeatherStatus(weatherStatus)
    }

    override suspend fun deleteWeatherStatus(lat: Double, lon: Double) {
        return weatherLocalDataSourceImp.deleteWeatherStatus(lat,lon)
    }

    override suspend fun getHourlyDetails(lat: Double, lon: Double): Flow<HorulyDetails?> {
        return weatherLocalDataSourceImp.getHourlyDetails(lat, lon)
    }

    override suspend fun insertHourlyDetails(horulyDetails: HorulyDetails) {
        return weatherLocalDataSourceImp.insertHourlyDetails(horulyDetails)
    }

    override suspend fun deleteHourlyDetails(lat: Double, lon: Double) {
        return weatherLocalDataSourceImp.deleteHourlyDetails(lat,lon)
    }

    override suspend fun getDailyDetails(lat: Double, lon: Double): Flow<DailyDetails?> {
        return weatherLocalDataSourceImp.getDailyDetails(lat,lon)
    }

    override suspend fun insertDailyDetails(dailyDetails: DailyDetails) {
        weatherLocalDataSourceImp.insertDailyDetails(dailyDetails)
    }

    override suspend fun deleteDailyDetails(lat: Double, lon: Double) {
        return weatherLocalDataSourceImp.deleteDailyDetails(lat, lon)
    }

    override suspend fun getAlerts(): Flow<List<Alerts>> {
        return alertsLocalDataSourceImp.getAlerts()
    }

    override suspend fun addAlert(alert: Alerts) {
        return alertsLocalDataSourceImp.addAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alerts) {
       return alertsLocalDataSourceImp.deleteAlert(alert)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun makeAlert(worker: WorkManager,alert: Alerts) {

        val now = LocalDateTime.now()
        val alertTime = parseDateTime(alert.date,alert.startTime)
        Log.i("Worker", "makeAlert: $alertTime ")
        val delay = Duration.between(now, alertTime).toMillis()
        if(delay<0) return

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val alertString = fromAlerts(alert)
        val inputData = Data.Builder()
            .putString("alert",alertString)
            .build()

        val request = OneTimeWorkRequestBuilder<AlertsWorker>()
            .setInputData(inputData)
            .setInitialDelay(delay,TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .addTag("WorkManager ${alert.date} between ${alert.startTime} and ${alert.endTime}")
            .build()

        worker.enqueue(request)
    }

    override suspend fun cancelAlert(worker: WorkManager,alert: Alerts) {
        Log.i("Worker", "cancelAlert: ")
        worker.cancelAllWorkByTag("WorkManager ${alert.date} between ${alert.startTime} and ${alert.endTime}")
    }

    override fun saveTempUnit(tempUnit: String) {
        return optionsLocalDataSourceImp.saveTempUnit(tempUnit)
    }

    override fun getTempUnit(): Flow<String> {
       return flowOf( optionsLocalDataSourceImp.getTempUnit() )
    }

    override fun saveWindSpeedUnit(windSpeedUnit: String) {
        return optionsLocalDataSourceImp.saveWindSpeedUnit(windSpeedUnit)
    }

    override fun getWindSpeedUnit(): Flow<String> {
        return flowOf( optionsLocalDataSourceImp.getWindSpeedUnit() )
    }

    override fun saveLocation(lat:Double,lon:Double) {
        return optionsLocalDataSourceImp.saveLocation(lat,lon)
    }

    override fun getSavedLocation(): Flow<Location> {
        return flowOf(optionsLocalDataSourceImp.getSavedLocation())
    }

    override fun saveLocationOption(location: String) {
        return optionsLocalDataSourceImp.saveLocationOption(location)
    }

    override fun getSavedLocationOption(): Flow<String> {
        return flowOf(optionsLocalDataSourceImp.getSavedLocationOption())
    }

    override fun saveLanguage(language: String) {
        return optionsLocalDataSourceImp.saveLanguage(language)
    }

    override fun getLanguage(): Flow<String> {
        return flowOf( optionsLocalDataSourceImp.getLanguage())
    }


    companion object {
        private val repository: Repository? = null

        fun getInstance(
            weatherRemoteDataSourceImp: WeatherRemoteDataSource,
            favouritesLocalDataSourceImp: FavouritesLocalDataSource,
            weatherLocalDataSourceImp:WeatherLocalDataSource,
            alertsLocalDataSourceImp:AlertsLocalDataSource,
            optionsLocalDataSourceImp: OptionsLocalDataSource
        ): Repository {
            if (repository == null) {
                return RepositoryImp(weatherRemoteDataSourceImp,
                    favouritesLocalDataSourceImp,weatherLocalDataSourceImp,alertsLocalDataSourceImp,
                    optionsLocalDataSourceImp)
            }
            return repository
        }
    }

}
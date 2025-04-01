package com.example.climo.data

import com.example.climo.model.Alerts
import com.example.climo.model.CurrentWeather
import com.example.climo.model.DailyDetails
import com.example.climo.model.Deatils
import com.example.climo.model.Favourites
import com.example.climo.model.HorulyDetails
import com.example.climo.model.WeatherList
import com.example.climo.model.WeatherStatus
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getCurrentWeather(lat:Double,lon:Double) : Flow<CurrentWeather>
    suspend fun getCurrentForecast(lat:Double,lon:Double) : Flow<WeatherList>

    suspend fun getFavourites() : Flow<List<Favourites>>
    suspend fun addFavourite(favourite: Favourites)
    suspend fun deleteFavourite(favourite: Favourites)

    suspend fun getWeatherStatus(lat:Double,lon:Double) :Flow<WeatherStatus?>
    suspend fun insertWeatherStatus(weatherStatus: WeatherStatus)
    suspend fun deleteWeatherStatus(lat: Double,lon: Double)

    suspend fun getHourlyDetails(lat: Double,lon: Double) : Flow<HorulyDetails?>
    suspend fun insertHourlyDetails(horulyDetails: HorulyDetails)
    suspend fun deleteHourlyDetails(lat: Double,lon: Double)

    suspend fun getDailyDetails(lat: Double,lon: Double) : Flow<DailyDetails?>
    suspend fun insertDailyDetails(dailyDetails: DailyDetails)
    suspend fun deleteDailyDetails(lat: Double,lon: Double)

    suspend fun getAlerts() : Flow<List<Alerts>>
    suspend fun addAlert(alert:Alerts)
    suspend fun deleteAlert(alert: Alerts)


}
package com.example.climo.data.local

import com.example.climo.model.DailyDetails
import com.example.climo.model.Deatils
import com.example.climo.model.HorulyDetails
import com.example.climo.model.WeatherStatus
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {

    suspend fun getWeatherStatus(lat:Double,lon:Double) :Flow<WeatherStatus?>
    suspend fun insertWeatherStatus(weatherStatus: WeatherStatus)
    suspend fun deleteWeatherStatus(lat: Double,lon: Double)

    suspend fun getHourlyDetails(lat: Double,lon: Double) : Flow<HorulyDetails?>
    suspend fun insertHourlyDetails(horulyDetails: HorulyDetails)
    suspend fun deleteHourlyDetails(lat: Double,lon: Double)

    suspend fun getDailyDetails(lat: Double,lon: Double) : Flow<DailyDetails?>
    suspend fun insertDailyDetails(dailyDetails: DailyDetails)
    suspend fun deleteDailyDetails(lat: Double,lon: Double)


}
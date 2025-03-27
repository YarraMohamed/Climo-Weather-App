package com.example.climo.data.remote

import com.example.climo.model.CurrentWeather
import com.example.climo.model.WeatherList
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat:Double,lon:Double) : Flow<CurrentWeather>
    suspend fun getCurrentWeatherForecast(lat:Double,lon:Double) : Flow<WeatherList>
}
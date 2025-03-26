package com.example.climo.data.remote

import com.example.climo.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat:Double,lon:Double) : Flow<CurrentWeather>
}
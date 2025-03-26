package com.example.climo.data

import com.example.climo.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getCurrentWeather(lat:Double,lon:Double) : Flow<CurrentWeather>
}
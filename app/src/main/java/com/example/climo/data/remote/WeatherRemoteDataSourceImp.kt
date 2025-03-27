package com.example.climo.data.remote

import com.example.climo.model.CurrentWeather
import com.example.climo.model.WeatherList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRemoteDataSourceImp(private val service: WeatherService) : WeatherRemoteDataSource{

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Flow<CurrentWeather>{
        val result = service.getCurrentWeatherDetails(lat,lon)
        return flowOf(result)
    }
    override suspend fun getCurrentWeatherForecast(lat: Double, lon: Double): Flow<WeatherList> {
        val result = service.getWeatherForecast(lat,lon)
        return flowOf(result)
    }
}
package com.example.climo.data.remote

import com.example.climo.model.CurrentWeather
import com.example.climo.model.WeatherList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRemoteDataSourceImp(private val service: WeatherService) : WeatherRemoteDataSource{

    override suspend fun getCurrentWeather(lat: Double, lon: Double,unit:String,lang:String): Flow<CurrentWeather>{
        val result = service.getCurrentWeatherDetails(lat,lon, units = unit, lang = lang)
        return flowOf(result)
    }
    override suspend fun getCurrentWeatherForecast(lat: Double, lon: Double,unit:String): Flow<WeatherList> {
        val result = service.getWeatherForecast(lat,lon, units = unit)
        return flowOf(result)
    }
}
package com.example.climo.data.remote

import com.example.climo.model.CurrentWeather
import com.example.climo.model.WeatherList
import com.example.climo.utilities.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherDetails(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") appid:String = API_KEY,
        @Query("units") units:String = "metric"
    ) : CurrentWeather

    @GET("data/2.5/forecast")
    suspend fun getWeatherForecast(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") appid:String = API_KEY,
        @Query("units") units:String = "metric"
    ) : WeatherList
}

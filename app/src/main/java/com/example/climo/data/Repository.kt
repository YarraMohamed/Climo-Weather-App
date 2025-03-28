package com.example.climo.data

import com.example.climo.model.CurrentWeather
import com.example.climo.model.Favourites
import com.example.climo.model.WeatherList
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getCurrentWeather(lat:Double,lon:Double) : Flow<CurrentWeather>
    suspend fun getCurrentForecast(lat:Double,lon:Double) : Flow<WeatherList>
    suspend fun getFavourites() : Flow<List<Favourites>>
    suspend fun addFavourite(favourite: Favourites)
    suspend fun deleteFavourite(favourite: Favourites)
}
package com.example.climo.model

import android.telecom.Call.Details
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_details", primaryKeys = ["lat", "lon"])
data class WeatherStatus(
    val lat:Double,
    val lon:Double,
    val currentWeather: CurrentWeather,
)

@Entity(tableName = "horuly_deatils",primaryKeys = ["lat", "lon"])
data class HorulyDetails(
    val lat:Double,
    val lon:Double,
    val list: List<Deatils>
)

@Entity(tableName = "daily_deatils",primaryKeys = ["lat", "lon"])
data class DailyDetails(
    val lat:Double,
    val lon:Double,
    val list: List<Deatils>
)




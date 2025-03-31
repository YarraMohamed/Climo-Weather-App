package com.example.climo.data.local

import com.example.climo.model.Clouds
import com.example.climo.model.Coord
import com.example.climo.model.CurrentWeather
import com.example.climo.model.DailyDetails
import com.example.climo.model.Deatils
import com.example.climo.model.Favourites
import com.example.climo.model.HorulyDetails
import com.example.climo.model.Main
import com.example.climo.model.Weather
import com.example.climo.model.WeatherList
import com.example.climo.model.WeatherStatus
import com.example.climo.model.Wind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeWeatherLocalDataSource : WeatherLocalDataSource {

    override suspend fun getWeatherStatus(lat: Double, lon: Double): Flow<WeatherStatus?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertWeatherStatus(weatherStatus: WeatherStatus) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWeatherStatus(lat: Double, lon: Double) {
        TODO("Not yet implemented")
    }

    override suspend fun getHourlyDetails(lat: Double, lon: Double): Flow<HorulyDetails?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertHourlyDetails(horulyDetails: HorulyDetails) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHourlyDetails(lat: Double, lon: Double) {
        TODO("Not yet implemented")
    }

    override suspend fun getDailyDetails(lat: Double, lon: Double): Flow<DailyDetails?> {
        TODO("Not yet implemented")
    }

    override suspend fun insertDailyDetails(dailyDetails: DailyDetails) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDailyDetails(lat: Double, lon: Double) {
        TODO("Not yet implemented")
    }
}
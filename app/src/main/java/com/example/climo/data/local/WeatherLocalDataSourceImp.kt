package com.example.climo.data.local

import android.telecom.Call
import com.example.climo.data.db.WeatherDAO
import com.example.climo.model.DailyDetails
import com.example.climo.model.Deatils
import com.example.climo.model.HorulyDetails

import com.example.climo.model.WeatherStatus
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSourceImp(private val weatherDAO: WeatherDAO) : WeatherLocalDataSource {
    override suspend fun getWeatherStatus(lat: Double, lon: Double): Flow<WeatherStatus?> {
        return weatherDAO.getWeatherStatus(lat, lon)
    }

    override suspend fun insertWeatherStatus(weatherStatus: WeatherStatus) {
        return weatherDAO.insertWeatherStatus(weatherStatus)
    }

    override suspend fun deleteWeatherStatus(lat: Double, lon: Double) {
        return weatherDAO.deleteWeatherStatus(lat,lon)
    }

    override suspend fun getHourlyDetails(lat: Double, lon: Double): Flow<HorulyDetails?> {
        return weatherDAO.getHourlyDetails(lat,lon)
    }

    override suspend fun insertHourlyDetails(horulyDetails: HorulyDetails) {
        return weatherDAO.insertHourlyDetails(horulyDetails)
    }

    override suspend fun deleteHourlyDetails(lat: Double, lon: Double) {
        return weatherDAO.deleteHourlyDetails(lat,lon)
    }

    override suspend fun getDailyDetails(lat: Double, lon: Double): Flow<DailyDetails?> {
       return weatherDAO.getDailyDetails(lat, lon)
    }

    override suspend fun insertDailyDetails(dailyDetails: DailyDetails) {
        return weatherDAO.insertDailyDetails(dailyDetails)
    }

    override suspend fun deleteDailyDetails(lat: Double, lon: Double) {
        return weatherDAO.deleteDailyDetails(lat, lon)
    }

}
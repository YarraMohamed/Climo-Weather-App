package com.example.climo.data

import com.example.climo.data.remote.WeatherRemoteDataSourceImp
import com.example.climo.model.CurrentWeather
import com.example.climo.model.WeatherList
import kotlinx.coroutines.flow.Flow

class RepositoryImp private constructor (private val weatherRemoteDataSourceImp: WeatherRemoteDataSourceImp) :Repository {

    override suspend fun getCurrentWeather(lat:Double,lon:Double): Flow<CurrentWeather> {
        return weatherRemoteDataSourceImp.getCurrentWeather(lat,lon)
    }

    override suspend fun getCurrentForecast(lat: Double, lon: Double): Flow<WeatherList> {
        return weatherRemoteDataSourceImp.getCurrentWeatherForecast(lat,lon)
    }

    companion object {
        private val repository: Repository? = null

        fun getInstance(
            weatherRemoteDataSourceImp: WeatherRemoteDataSourceImp,
        ): Repository {
            if (repository == null) {
                return RepositoryImp(weatherRemoteDataSourceImp)
            }
            return repository
        }
    }

}
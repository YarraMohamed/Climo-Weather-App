package com.example.climo.data

import com.example.climo.data.local.FavouritesLocalDataSource
import com.example.climo.data.local.WeatherLocalDataSource
import com.example.climo.data.remote.WeatherRemoteDataSource
import com.example.climo.model.CurrentWeather
import com.example.climo.model.DailyDetails
import com.example.climo.model.Deatils
import com.example.climo.model.Favourites
import com.example.climo.model.HorulyDetails
import com.example.climo.model.WeatherList
import com.example.climo.model.WeatherStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositoryImp private constructor (
    private val weatherRemoteDataSourceImp: WeatherRemoteDataSource,
    private val favouritesLocalDataSourceImp: FavouritesLocalDataSource,
    private val weatherLocalDataSourceImp:WeatherLocalDataSource) :Repository {


    override suspend fun getCurrentWeather(lat:Double,lon:Double): Flow<CurrentWeather> {
        return weatherRemoteDataSourceImp.getCurrentWeather(lat,lon)
    }

    override suspend fun getCurrentForecast(lat: Double, lon: Double): Flow<WeatherList> {
        return weatherRemoteDataSourceImp.getCurrentWeatherForecast(lat,lon)
    }

    override suspend fun getFavourites(): Flow<List<Favourites>> {
        return favouritesLocalDataSourceImp.getFavouriteList()
    }

    override suspend fun addFavourite(favourite: Favourites) {
        return favouritesLocalDataSourceImp.addFav(favourite)
    }

    override suspend fun deleteFavourite(favourite: Favourites) {
        return favouritesLocalDataSourceImp.deleteFav(favourite)
    }

    override suspend fun getWeatherStatus(lat: Double, lon: Double): Flow<WeatherStatus?> {
        return weatherLocalDataSourceImp.getWeatherStatus(lat,lon)
    }

    override suspend fun insertWeatherStatus(weatherStatus: WeatherStatus) {
        return weatherLocalDataSourceImp.insertWeatherStatus(weatherStatus)
    }

    override suspend fun deleteWeatherStatus(lat: Double, lon: Double) {
        return weatherLocalDataSourceImp.deleteWeatherStatus(lat,lon)
    }

    override suspend fun getHourlyDetails(lat: Double, lon: Double): Flow<HorulyDetails?> {
        return weatherLocalDataSourceImp.getHourlyDetails(lat, lon)
    }

    override suspend fun insertHourlyDetails(horulyDetails: HorulyDetails) {
        return weatherLocalDataSourceImp.insertHourlyDetails(horulyDetails)
    }

    override suspend fun deleteHourlyDetails(lat: Double, lon: Double) {
        return weatherLocalDataSourceImp.deleteHourlyDetails(lat,lon)
    }

    override suspend fun getDailyDetails(lat: Double, lon: Double): Flow<DailyDetails?> {
        return weatherLocalDataSourceImp.getDailyDetails(lat,lon)
    }

    override suspend fun insertDailyDetails(dailyDetails: DailyDetails) {
        weatherLocalDataSourceImp.insertDailyDetails(dailyDetails)
    }

    override suspend fun deleteDailyDetails(lat: Double, lon: Double) {
        return weatherLocalDataSourceImp.deleteDailyDetails(lat, lon)
    }

    companion object {
        private val repository: Repository? = null

        fun getInstance(
            weatherRemoteDataSourceImp: WeatherRemoteDataSource,
            favouritesLocalDataSourceImp: FavouritesLocalDataSource,
            weatherLocalDataSourceImp:WeatherLocalDataSource
        ): Repository {
            if (repository == null) {
                return RepositoryImp(weatherRemoteDataSourceImp,favouritesLocalDataSourceImp,weatherLocalDataSourceImp)
            }
            return repository
        }
    }

}
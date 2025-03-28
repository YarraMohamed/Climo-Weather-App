package com.example.climo.data

import com.example.climo.data.local.FavouritesLocalDataSourceImp
import com.example.climo.data.remote.WeatherRemoteDataSourceImp
import com.example.climo.model.CurrentWeather
import com.example.climo.model.Favourites
import com.example.climo.model.WeatherList
import kotlinx.coroutines.flow.Flow

class RepositoryImp private constructor (
    private val weatherRemoteDataSourceImp: WeatherRemoteDataSourceImp,
    private val favouritesLocalDataSourceImp: FavouritesLocalDataSourceImp) :Repository {

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

    companion object {
        private val repository: Repository? = null

        fun getInstance(
            weatherRemoteDataSourceImp: WeatherRemoteDataSourceImp,
            favouritesLocalDataSourceImp: FavouritesLocalDataSourceImp
        ): Repository {
            if (repository == null) {
                return RepositoryImp(weatherRemoteDataSourceImp,favouritesLocalDataSourceImp)
            }
            return repository
        }
    }

}
package com.example.climo.home.viewmodel

import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.climo.data.Repository
import com.example.climo.model.CurrentWeather
import com.example.climo.model.Deatils
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.climo.model.Response
import com.example.climo.model.WeatherList
import com.example.climo.utilities.getFormattedDate
import com.example.climo.utilities.getFromattedTime
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class HomeViewModel(private val repo:Repository) : ViewModel(){

    private var weatherDetails : MutableStateFlow<Response<CurrentWeather>> = MutableStateFlow(Response.Loading)
    var currentWeather = weatherDetails.asStateFlow()

    private var weatherHourlyForecastDetails : MutableStateFlow<Response<List<Deatils>>> = MutableStateFlow(Response.Loading)
    var currentWeatherForecast = weatherHourlyForecastDetails.asStateFlow()

    private var weatherDailyForecastDetails : MutableStateFlow<Response<List<Deatils>>> = MutableStateFlow(Response.Loading)
    var currentDailyForecast = weatherDailyForecastDetails.asStateFlow()

    private var timeFlow = MutableStateFlow("")
    var time = timeFlow.asStateFlow()

    private var dateFlow = MutableStateFlow("")
    var date = dateFlow.asStateFlow()

    fun getCurrentTime() {
        val formattedTime = getFromattedTime()
        timeFlow.value = formattedTime
    }

    fun getCurrentDate(){
        val formattedDate = getFormattedDate()
        dateFlow.value=formattedDate
    }

    fun getAddressFromLocation(lat: Double,lon:Double, geocoder: Geocoder): String {
        return try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (addresses!!.isNotEmpty()) {
                var address : String? = addresses[0]?.adminArea
                if(address.isNullOrEmpty()){
                    address = addresses[0]?.countryName
                }
                address?:"Address not found"
            } else {
                "Address not found"
            }
        } catch (e: Exception) {
            "Error retrieving address"
        }
    }

    fun getCurrentWeather(lat:Double,lon:Double){
        viewModelScope.launch {
            try {
                repo.getCurrentWeather(lat,lon)
                    .catch { weatherDetails.value = Response.Failure(Throwable("Error getting data")) }
                    .collect{
                        weatherDetails.value = Response.Success(it)
                    }
            }catch (ex:Exception){
                weatherDetails.value = Response.Failure(Throwable("Error calling the api"))

            }
        }
    }


    fun getHourlyWeatherForecast(lat:Double,lon:Double){
        viewModelScope.launch {
            try {
                repo.getCurrentForecast(lat,lon)
                    .catch { weatherHourlyForecastDetails.value = Response.Failure(Throwable("Error getting data")) }
                    .map{it.list.take(9)}
                    .collect{
                        weatherHourlyForecastDetails.value = Response.Success(it)
                    }
            }catch (ex:Exception){
                weatherHourlyForecastDetails.value = Response.Failure(Throwable("Error calling the api"))
            }
        }
    }


    fun getDailyWeatherForecast(lat:Double,lon:Double){
        viewModelScope.launch {
            try {
                repo.getCurrentForecast(lat,lon)
                    .catch { weatherDailyForecastDetails.value = Response.Failure(Throwable("Error getting data")) }
                    .map { newList ->
                        newList.list
                            .groupBy { it.dt_txt.split(" ")[0] }
                            .map{it.value.first()}
                    }
                    .collect{
                        weatherDailyForecastDetails.value = Response.Success(it)
                    }

            }catch (ex:Exception){
                weatherDailyForecastDetails.value = Response.Failure(Throwable("Error calling the api"))
            }
        }
    }

    class HomeFactory(private val repo: Repository ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repo) as T
        }
    }
}
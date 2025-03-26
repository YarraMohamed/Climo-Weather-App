package com.example.climo.home.viewmodel

import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.climo.data.Repository
import com.example.climo.model.CurrentWeather
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.climo.model.Response
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class HomeViewModel(private val repo:Repository, private val location: Location) : ViewModel(){

    init {
        getCurrentWeather(location.latitude,location.longitude)
    }

    private var weatherDetails : MutableStateFlow<Response<CurrentWeather>> = MutableStateFlow(Response.Loading)
    var currentWeather = weatherDetails.asStateFlow()

    fun getAddressFromLocation(lat: Double,lon:Double, geocoder: Geocoder): String {
        return try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (addresses!!.isNotEmpty()) {
                Log.i("TAG", "getAddressFromLocation: ")
                val address : String? = addresses[0]?.locality +", "+ addresses[0]?.countryName
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
                        Log.i("TAG", "getCurrentWeather: ${weatherDetails.value} ")
                    }
            }catch (ex:Exception){
                weatherDetails.value = Response.Failure(Throwable("Error calling the api"))

            }
        }
    }

    class HomeFactory(private val repo: Repository, private val location: Location) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repo,location) as T
        }
    }
}
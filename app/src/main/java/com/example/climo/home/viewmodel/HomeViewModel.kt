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
import com.example.climo.model.DailyDetails
import com.example.climo.model.Deatils
import com.example.climo.model.HorulyDetails
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.climo.model.Response
import com.example.climo.model.WeatherList
import com.example.climo.model.WeatherStatus
import com.example.climo.utilities.ConnectivityListener
import com.example.climo.utilities.getFormattedDate
import com.example.climo.utilities.getFromattedTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class HomeViewModel(private val repo:Repository,private val connectivityListener: ConnectivityListener) : ViewModel(){

    val isConnected = connectivityListener.networkStatus
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

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

    fun getCurrentWeather(lat: Double, lon: Double, isOnline: Boolean) {

        if (isOnline) {
            getRemoteCurrentWeather(lat, lon)
        } else {
            getStoredWeather(lat, lon)
        }

    }

    fun getHourlyWeatherForecast(lat: Double, lon: Double, isOnline: Boolean) {

        if (isOnline) {
            getRemoteHourlyWeatherForecast(lat, lon)
        } else {
            getStoredHourlyDetails(lat, lon)
        }

    }

    fun getDailyWeatherForecast(lat: Double, lon: Double, isOnline: Boolean) {
        if (isOnline) {
            getRemoteDailyWeatherForecast(lat, lon)
        } else {
            getStoredDailyDetails(lat, lon)
        }
    }

    fun getRemoteCurrentWeather(lat:Double,lon:Double){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getCurrentWeather(lat,lon)
                    .catch { weatherDetails.value = Response.Failure(Throwable(it.message)) }
                    .collect{
                        weatherDetails.value = Response.Success(it)
                        storeCurrentWeather(lat,lon,it)
                    }
                Log.i("TAG", "${weatherDetails.value}: ")
            }catch (ex:Exception){
                weatherDetails.value = Response.Failure(Throwable(ex.cause))

            }
        }

    }

     private fun getRemoteHourlyWeatherForecast(lat:Double,lon:Double){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getCurrentForecast(lat,lon)
                    .catch { weatherHourlyForecastDetails.value = Response.Failure(Throwable("Error getting data")) }
                    .map{it.list.take(9)}
                    .collect{
                        weatherHourlyForecastDetails.value = Response.Success(it)
                        storeHourlyForecast(lat, lon,it)
                    }
            }catch (ex:Exception){
                weatherHourlyForecastDetails.value = Response.Failure(Throwable("Error calling the api"))
            }
        }
    }

     private fun getRemoteDailyWeatherForecast(lat:Double,lon:Double){
        viewModelScope.launch(Dispatchers.IO) {
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
                        storeDailyForecast(lat,lon,it)
                    }

            }catch (ex:Exception){
                weatherDailyForecastDetails.value = Response.Failure(Throwable("Error calling the api"))
            }
        }
    }

    private fun storeCurrentWeather(lat: Double,lon: Double,currentWeather: CurrentWeather){
        if(lat==0.0 && lon==0.0) return
        viewModelScope.launch(Dispatchers.IO) {
            val weatherStatus = WeatherStatus(lat,lon,currentWeather)
            try {
                repo.insertWeatherStatus(weatherStatus)

            }catch (ex:Exception){
                Log.i("TAG", "${ex.message} ")
            }
        }
    }

    private fun storeHourlyForecast(lat: Double,lon: Double,list:List<Deatils>){
        if(lat==0.0 && lon==0.0) return
        viewModelScope.launch(Dispatchers.IO) {
            val hourlyDeatils = HorulyDetails(lat,lon, list)
            try {
                repo.insertHourlyDetails(hourlyDeatils)

            }catch (ex:Exception){
                Log.i("TAG", "${ex.message} ")
            }
        }
    }

    private fun storeDailyForecast(lat: Double,lon: Double,list: List<Deatils>){
        if(lat==0.0 && lon==0.0) return
        viewModelScope.launch(Dispatchers.IO) {
            val dailyDetails = DailyDetails(lat, lon, list)
            try {
                repo.insertDailyDetails(dailyDetails)
            }catch (ex:Exception){
                Log.i("TAG", "${ex.message} ")
            }
        }
    }

    private fun getStoredWeather(lat: Double,lon: Double){
        viewModelScope.launch(Dispatchers.IO){
            try {
                repo.getWeatherStatus(lat,lon)
                    .catch { weatherDetails.value = Response.Failure(Throwable("Error getting data")) }
                    .collect{
                        if(it!=null){
                            weatherDetails.value = Response.Success(it.currentWeather)
                        }else{
                            weatherDetails.value = Response.Failure(Throwable("Error getting data"))
                        }
                    }
            }catch (ex:Exception){
                weatherDetails.value = Response.Failure(Throwable("Error calling the api"))

            }
        }
    }

    private fun getStoredHourlyDetails(lat: Double,lon: Double){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getHourlyDetails(lat,lon)
                    .catch { weatherHourlyForecastDetails.value = Response.Failure(Throwable("Error getting data")) }
                    .collect{
                        if(it!=null){
                            weatherHourlyForecastDetails.value = Response.Success(it.list)
                        }else{
                            weatherHourlyForecastDetails.value = Response.Failure(Throwable("Error calling the api"))
                        }
                    }
            }catch (ex:Exception){
                weatherHourlyForecastDetails.value = Response.Failure(Throwable("Error calling the api"))
            }
        }
    }

    private fun getStoredDailyDetails(lat: Double,lon: Double){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getDailyDetails(lat,lon)
                    .catch { weatherDailyForecastDetails.value = Response.Failure(Throwable("Error getting data")) }
                    .collect{
                        if(it!=null){
                            weatherDailyForecastDetails.value = Response.Success(it.list)
                        }else{
                            weatherDailyForecastDetails.value = Response.Failure(Throwable("Error calling the api"))
                        }
                    }
            }catch (ex:Exception){
                weatherDailyForecastDetails.value = Response.Failure(Throwable("Error calling the api"))
            }
        }
    }

    class HomeFactory(private val repo: Repository , private  val connectivityListener: ConnectivityListener): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repo, connectivityListener) as T
        }
    }
}
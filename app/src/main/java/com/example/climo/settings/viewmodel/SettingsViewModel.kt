package com.example.climo.settings.viewmodel

import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.climo.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(private val repo:Repository) : ViewModel() {

    private var tempUnitFlow = MutableStateFlow("")
    var tempUnit = tempUnitFlow.asStateFlow()

    private var windSpeedFlow = MutableStateFlow("")
    var windSpeed = windSpeedFlow.asStateFlow()

    private var languageFlow = MutableStateFlow("default")
    var language = languageFlow.asStateFlow()

    private var locationOptionFlow = MutableStateFlow("GPS")
    var savedLocationOption = locationOptionFlow.asStateFlow()



    init {
        getTempUnit()
        getLanguage()
        getLocationOption()
        saveWindSpeedUnit()
        getWindSpeedUnit()
    }

    fun saveTempUnit(unit:String){
        repo.saveTempUnit(unit)
    }

    fun getTempUnit(){
        viewModelScope.launch {
            repo.getTempUnit()
                .collect{
                    tempUnitFlow.value = it
                }
        }
    }
    fun saveWindSpeedUnit(){
        viewModelScope.launch {
            val newUnit = if (repo.getTempUnit().first() == "imperial") "m/h" else ""
            repo.saveWindSpeedUnit(newUnit)
            windSpeedFlow.value = newUnit
        }
    }

    fun getWindSpeedUnit(){
        viewModelScope.launch {
            repo.getWindSpeedUnit()
                .collect{
                    windSpeedFlow.value=it
                }
        }
    }

    fun saveLanguage(lang:String){
        repo.saveLanguage(lang)
    }

    fun getLanguage() {
        viewModelScope.launch {
            repo.getLanguage()
                .collect {
                    Log.i("", "getLanguage: $it ")
                    languageFlow.value = it
                }
        }
    }

    fun saveLocationOption(location:String){
        repo.saveLocationOption(location)
    }

    fun getLocationOption() {
        viewModelScope.launch {
            repo.getSavedLocationOption()
                .collect {
                    locationOptionFlow.value=it
                }
        }
    }

    fun saveLocation(lat:Double,lon:Double){
        repo.saveLocation(lat,lon)
    }

    class SettingsFactory(private val repo: Repository) :ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(repo) as T

        }
    }
}
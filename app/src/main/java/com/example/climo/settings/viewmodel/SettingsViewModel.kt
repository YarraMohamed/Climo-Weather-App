package com.example.climo.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.climo.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(private val repo:Repository) : ViewModel() {

    private var tempUnitFlow = MutableStateFlow("")
    var tempUnit = tempUnitFlow.asStateFlow()

    private var windSpeedFlow = MutableStateFlow("")
    var windSpeed = windSpeedFlow.asStateFlow()

    private var languageFlow = MutableStateFlow("en")
    var language = languageFlow.asStateFlow()

    private var locationFlow = MutableStateFlow("")
    var savedLocation = locationFlow.asStateFlow()


    init {
        getTempUnit()
        getLanguage()
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
    private fun saveWindSpeedUnit(){
        viewModelScope.launch {
            if(repo.getTempUnit().first()=="imperial"){
                repo.saveWindSpeedUnit("m/h")
            }else{
                repo.saveWindSpeedUnit("m/s")
            }
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
                    languageFlow.value = it
                }
        }
    }

    fun saveLocation(location:String){
        repo.saveLocation(location)
    }

    fun getLocation() {
        viewModelScope.launch {
            repo.getSavedLocation()
                .collect {
                    locationFlow.value=it
                }
        }
    }

    class SettingsFactory(private val repo: Repository) :ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(repo) as T

        }
    }
}
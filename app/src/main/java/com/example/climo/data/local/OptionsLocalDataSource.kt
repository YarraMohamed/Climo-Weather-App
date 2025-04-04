package com.example.climo.data.local

interface OptionsLocalDataSource {
    fun saveTempUnit(tempUnit: String)
    fun getTempUnit(): String
    fun saveWindSpeedUnit(windSpeedUnit: String)
    fun getWindSpeedUnit(): String
    fun saveLocation(location: String)
    fun getSavedLocation(): String
    fun saveLanguage(language: String)
    fun getLanguage(): String
}
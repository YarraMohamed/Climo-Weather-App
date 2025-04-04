package com.example.climo.data.local

import android.location.Location

interface OptionsLocalDataSource {
    fun saveTempUnit(tempUnit: String)
    fun getTempUnit(): String
    fun saveWindSpeedUnit(windSpeedUnit: String)
    fun getWindSpeedUnit(): String
    fun saveLocation(lat:Double,lon:Double)
    fun getSavedLocation(): Location
    fun saveLocationOption(location: String)
    fun getSavedLocationOption(): String
    fun saveLanguage(language: String)
    fun getLanguage(): String
}
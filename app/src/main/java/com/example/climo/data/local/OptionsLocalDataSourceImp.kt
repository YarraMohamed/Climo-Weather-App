package com.example.climo.data.local

import android.content.SharedPreferences
import android.location.Location

class OptionsLocalDataSourceImp(private val sharedPreferences: SharedPreferences) :
    OptionsLocalDataSource {

    override fun saveTempUnit(tempUnit:String){
        sharedPreferences.edit().putString("temp_unit",tempUnit).apply()
    }

    override fun getTempUnit():String{
        return sharedPreferences.getString("temp_unit","metric")?:"metric"
    }

    override fun saveWindSpeedUnit(windSpeedUnit:String){
        sharedPreferences.edit().putString("wind_speed_unit",windSpeedUnit).apply()
    }

    override fun getWindSpeedUnit():String{
        return sharedPreferences.getString("wind_speed_unit",",ms")?:"ms"
    }

    override fun saveLocation(lat:Double,lon:Double){
        sharedPreferences.edit().putFloat("lat",lat.toFloat()).apply()
        sharedPreferences.edit().putFloat("lon",lon.toFloat()).apply()
    }

    override fun getSavedLocation():Location{
        val lat = sharedPreferences.getFloat("lat", 0f).toDouble()
        val lon = sharedPreferences.getFloat("lon", 0f).toDouble()
        return Location("").apply {
            latitude = lat
            longitude = lon
        }
    }

    override fun saveLocationOption(location: String) {
        sharedPreferences.edit().putString("locationOption",location).apply()
    }

    override fun getSavedLocationOption(): String {
        return sharedPreferences.getString("locationOption","GPS")?:"GPS"
    }

    override fun saveLanguage(language: String) {
        sharedPreferences.edit().putString("language", language).apply()
    }

    override fun getLanguage(): String {
        return sharedPreferences.getString("language", "default") ?: "deafult"
    }
}
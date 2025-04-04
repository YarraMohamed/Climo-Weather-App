package com.example.climo.data.local

import android.content.SharedPreferences

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

    override fun saveLocation(location: String){
        sharedPreferences.edit().putString("location",location).apply()
    }

    override fun getSavedLocation():String{
        return sharedPreferences.getString("location","")?:""
    }

    override fun saveLanguage(language: String) {
        sharedPreferences.edit().putString("language", language).apply()
    }

    override fun getLanguage(): String {
        return sharedPreferences.getString("language", "en") ?: "en"
    }
}
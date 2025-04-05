package com.example.climo.data.local

import android.location.Location

class FakeOptionsLocalDataSource : OptionsLocalDataSource {
    override fun saveTempUnit(tempUnit: String) {
        TODO("Not yet implemented")
    }

    override fun getTempUnit(): String {
        TODO("Not yet implemented")
    }

    override fun saveWindSpeedUnit(windSpeedUnit: String) {
        TODO("Not yet implemented")
    }

    override fun getWindSpeedUnit(): String {
        TODO("Not yet implemented")
    }

    override fun saveLocation(lat: Double, lon: Double) {
        TODO("Not yet implemented")
    }

    override fun getSavedLocation(): Location {
        TODO("Not yet implemented")
    }

    override fun saveLocationOption(location: String) {
        TODO("Not yet implemented")
    }

    override fun getSavedLocationOption(): String {
        TODO("Not yet implemented")
    }

    override fun saveLanguage(language: String) {
        TODO("Not yet implemented")
    }

    override fun getLanguage(): String {
        TODO("Not yet implemented")
    }

}
package com.example.climo.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherTypeConverter {
    private val gson = Gson()

    // CurrentWeather
    @TypeConverter
    fun fromCurrentWeather(currentWeather: CurrentWeather): String = gson.toJson(currentWeather)

    @TypeConverter
    fun toCurrentWeather(data: String): CurrentWeather {
        val type = object : TypeToken<CurrentWeather>() {}.type
        return gson.fromJson(data, type)
    }

    // Coord
    @TypeConverter
    fun fromCoord(coord: Coord): String = gson.toJson(coord)

    @TypeConverter
    fun toCoord(data: String): Coord {
        val type = object : TypeToken<Coord>() {}.type
        return gson.fromJson(data, type)
    }

    // Weather
    @TypeConverter
    fun fromWeather(weather: Weather): String = gson.toJson(weather)

    @TypeConverter
    fun toWeather(data: String): Weather {
        val type = object : TypeToken<Weather>() {}.type
        return gson.fromJson(data, type)
    }

    // Weather List
    @TypeConverter
    fun fromWeatherListDetails(weatherList: List<Weather>): String = gson.toJson(weatherList)

    @TypeConverter
    fun toWeatherListDetails(data: String): List<Weather> {
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(data, type)
    }

    // Main
    @TypeConverter
    fun fromMain(main: Main): String = gson.toJson(main)

    @TypeConverter
    fun toMain(data: String): Main {
        val type = object : TypeToken<Main>() {}.type
        return gson.fromJson(data, type)
    }

    // Wind
    @TypeConverter
    fun fromWind(wind: Wind): String = gson.toJson(wind)

    @TypeConverter
    fun toWind(data: String): Wind {
        val type = object : TypeToken<Wind>() {}.type
        return gson.fromJson(data, type)
    }

    // Clouds
    @TypeConverter
    fun fromClouds(clouds: Clouds): String = gson.toJson(clouds)

    @TypeConverter
    fun toClouds(data: String): Clouds {
        val type = object : TypeToken<Clouds>() {}.type
        return gson.fromJson(data, type)
    }

    // WeatherList
    @TypeConverter
    fun fromWeatherList(weatherList: WeatherList): String = gson.toJson(weatherList)

    @TypeConverter
    fun toWeatherList(data: String): WeatherList {
        val type = object : TypeToken<WeatherList>() {}.type
        return gson.fromJson(data, type)
    }

    // Details List
    @TypeConverter
    fun fromDetailsList(detailsList: List<Deatils>): String = gson.toJson(detailsList)

    @TypeConverter
    fun toDetailsList(data: String): List<Deatils> {
        val type = object : TypeToken<List<Deatils>>() {}.type
        return gson.fromJson(data, type)
    }

    // Details
    @TypeConverter
    fun fromDetails(details: Deatils): String = gson.toJson(details)

    @TypeConverter
    fun toDetails(data: String): Deatils {
        val type = object : TypeToken<Deatils>() {}.type
        return gson.fromJson(data, type)
    }

    // WeatherStatus
    @TypeConverter
    fun fromWeatherStatus(weatherStatus: WeatherStatus): String = gson.toJson(weatherStatus)

    @TypeConverter
    fun toWeatherStatus(data: String): WeatherStatus {
        val type = object : TypeToken<WeatherStatus>() {}.type
        return gson.fromJson(data, type)
    }

    // HourlyDetails
    @TypeConverter
    fun fromHourlytDetails(horulyDetails: HorulyDetails): String = gson.toJson(horulyDetails)

    @TypeConverter
    fun toHourlyDetails(data: String): HorulyDetails {
        val type = object : TypeToken<HorulyDetails>() {}.type
        return gson.fromJson(data, type)
    }

    // DailyDetails
    @TypeConverter
    fun fromDailyDetails(dailyDetails: DailyDetails): String = gson.toJson(dailyDetails)

    @TypeConverter
    fun toDailyDetails(data: String): DailyDetails {
        val type = object : TypeToken<DailyDetails>() {}.type
        return gson.fromJson(data, type)
    }

}

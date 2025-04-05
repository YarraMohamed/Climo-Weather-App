package com.example.climo.data.remote

import com.example.climo.model.Clouds
import com.example.climo.model.Coord
import com.example.climo.model.CurrentWeather
import com.example.climo.model.Deatils
import com.example.climo.model.Main
import com.example.climo.model.Weather
import com.example.climo.model.WeatherList
import com.example.climo.model.Wind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherRemoteDataSource():
    WeatherRemoteDataSource {
    private val fakeCurrentWeather = CurrentWeather(
        Coord(30.1,32.5),
        listOf(
            Weather(
                2,
                "rain",
                "Description",
                "01d")
        ),
        Main(17.5,35,60),
        Wind(35.4),
        Clouds(30)
    )

    private val fakeWeatherForecast = WeatherList(
        list = listOf(
            Deatils(
                Main(45.0,675,10),
                listOf(
                    Weather(
                        2,
                        "rain1",
                        "Description1",
                        "01d")
                ),
                "3456677"
            ),
            Deatils(
                Main(35.0,55,10),
                listOf(
                    Weather(
                        2,
                        "rain1",
                        "Description1",
                        "05d")
                ),
                "3456677"
            ),
        )
    )

    override suspend fun getCurrentWeather(lat: Double, lon: Double,unit:String,lang:String): Flow<CurrentWeather> {
        return flowOf(fakeCurrentWeather)
    }

    override suspend fun getCurrentWeatherForecast(lat: Double, lon: Double,unit:String): Flow<WeatherList> {
        return flowOf(fakeWeatherForecast)
    }

}
package com.example.climo.model

data class CurrentWeather(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds)

data class Coord(val lon:Double,val lat:Double)
data class Weather(val id:Int,val main:String,val description:String,val icon:String)
data class Main(val temp:Double,val pressure:Int,val humidity:Int)
data class Wind(val speed:Double)
data class Clouds(val all:Int)
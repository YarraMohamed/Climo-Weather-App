package com.example.climo.model

data class Coord(val lon:Double,val lat:Double)
data class Weather(val id:Int,val main:String,val description:String,val icon:String)
data class Main(val temp:Double,val pressure:Int,val humidity:Int)
data class Wind(val speed:Double)
data class Clouds(val all:Int)
data class Deatils(
    val main: Main,
    val weather: List<Weather>,
    val dt_txt : String
)
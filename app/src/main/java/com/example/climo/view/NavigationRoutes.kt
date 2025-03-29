package com.example.climo.view

import com.example.climo.model.Alerts
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoutes {
    @Serializable
    object Splash : NavigationRoutes()
    @Serializable
    data class Home(val lat:Double=0.000,val lon:Double=0.000) : NavigationRoutes()
    @Serializable
    object Settings : NavigationRoutes()
    @Serializable
    object Alerts : NavigationRoutes()
    @Serializable
    object Favourites : NavigationRoutes()
    @Serializable
    object FavMap : NavigationRoutes()
}

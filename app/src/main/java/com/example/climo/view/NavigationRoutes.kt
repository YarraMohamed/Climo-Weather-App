package com.example.climo.view

import com.example.climo.model.Alerts
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoutes {
    @Serializable
    object Home : NavigationRoutes()
    @Serializable
    object Settings : NavigationRoutes()
    @Serializable
    object Alerts : NavigationRoutes()
    @Serializable
    object Favourites : NavigationRoutes()
}
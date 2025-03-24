package com.example.climo.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.climo.alerts.view.AlertView
import com.example.climo.favourites.view.FavouritesView
import com.example.climo.home.view.HomeView
import com.example.climo.model.Alerts
import com.example.climo.model.Favourites
import com.example.climo.settings.view.SettingsView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           ClimoApp()
        }
    }
}
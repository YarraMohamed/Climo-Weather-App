package com.example.climo.view

import FavMapScreen
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.climo.R
import com.example.climo.settings.view.SettingsView
import com.example.climo.utilities.ApplicationUtils
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.launch

const val REQUEST_LOCATION_CODE = 2005
class MainActivity : ComponentActivity() {

    private val applicationUtils = ApplicationUtils(this@MainActivity)
    private var locationState: MutableState<Location> = mutableStateOf(Location(LocationManager.GPS_PROVIDER))
//    private var location : Location = Location(LocationManager.GPS_PROVIDER)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!applicationUtils.isLocationEnabled()) applicationUtils.enableLocationService(this)
        Places.initialize(applicationContext, getString(R.string.API_KEY))
        setContent {
            ClimoApp(locationState.value)
        }
    }
    override fun onStart() {
        super.onStart()
        if(applicationUtils.checkPermissions()){
            if(applicationUtils.isLocationEnabled()){
                applicationUtils.getLocation()
                lifecycleScope.launch {
                    applicationUtils.locationFlow.collect { updatedLocation ->
                        locationState.value = updatedLocation
                    }
                }
            }else{
                applicationUtils.enableLocationService(this)
            }
        }else{
            requestLocationPermissions()
        }
    }

    fun requestLocationPermissions(){
        ActivityCompat.requestPermissions(this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            ),
            REQUEST_LOCATION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.isNotEmpty() &&
                (grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED ||
                        grantResults.getOrNull(1) == PackageManager.PERMISSION_GRANTED)
            ) {
                if (applicationUtils.isLocationEnabled()) {
                    applicationUtils.getLocation()
                    lifecycleScope.launch {
                        applicationUtils.locationFlow.collect { updatedLocation ->
                            locationState.value = updatedLocation
                        }
                    }
                } else {
                    applicationUtils.enableLocationService(this)
                }
            } else {
                val shouldShowRationale = permissions.any { shouldShowRequestPermissionRationale(it) }

                if (shouldShowRationale) {
                    requestLocationPermissions()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Permission Required")
                        .setMessage("Location permission is necessary. Please enable it in settings.")
                        .setPositiveButton("Go to Settings") { _, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", packageName, null)
                            }
                            startActivity(intent)
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            }
        }

    }
}

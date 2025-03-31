package com.example.climo.utilities

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ApplicationUtils(private val context: Context){

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationState = MutableStateFlow(Location(LocationManager.GPS_PROVIDER))
    public var locationFlow = locationState.asStateFlow()


    fun checkPermissions() : Boolean{
        var result = false
        if((ContextCompat.
            checkSelfPermission(context, ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        ){
            result = true
        }
        return result
    }

    fun isLocationEnabled() : Boolean{
        val locationManager : LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(5000).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .setMinUpdateDistanceMeters(1000f)
            }.build(),
            object :LocationCallback(){
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    locationState.value = locationResult.lastLocation?:Location(LocationManager.GPS_PROVIDER)
                }
            },
            Looper.myLooper()
        )
    }

    fun enableLocationService(context: Context){
        Toast.makeText(context,"Turn on Location Please",Toast.LENGTH_SHORT).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }
    companion object{
        fun getAddressFromLocation(lat: Double,lon:Double, geocoder: Geocoder): String {
            return try {
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                if (addresses!!.isNotEmpty()) {
                    val address = addresses[0].subAdminArea + ", " + addresses[0].countryName
                    address ?: "Address not found"
                } else {
                    "Address not found"
                }
            } catch (e: Exception) {
                "Error retrieving address"
            }
        }
    }

}

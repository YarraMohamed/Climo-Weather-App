package com.example.climo.view

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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
import com.example.climo.utilities.ApplicationUtils
import kotlinx.coroutines.launch

const val REQUEST_LOCATION_CODE = 2005
class MainActivity : ComponentActivity() {

    val applicationUtils = ApplicationUtils(this@MainActivity)
    lateinit var location: Location
    var address : MutableState<String>  = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!applicationUtils.isLocationEnabled()) applicationUtils.enableLocationService(this)
        setContent {
           ClimoApp()
        }
    }

    override fun onStart() {
        super.onStart()
        if(applicationUtils.checkPermissions()){
            if(applicationUtils.isLocationEnabled()){
                applicationUtils.getLocation()
                lifecycleScope.launch {
                    applicationUtils.locationFlow.collect { updatedLocation ->
                        location = updatedLocation
                        address.value = applicationUtils.getAddressFromLocation(location,this@MainActivity)
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
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if(requestCode == REQUEST_LOCATION_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED||grantResults[1]==PackageManager.PERMISSION_GRANTED){
                if(applicationUtils.isLocationEnabled()){
                    applicationUtils.getLocation()
                    location = applicationUtils.locationFlow.value
                }else{
                    applicationUtils.enableLocationService(this)
                }
            }else{
                val shouldShowRationale = shouldShowRequestPermissionRationale(permissions[0]) ||
                        shouldShowRequestPermissionRationale(permissions[1])
                Log.i("TAG", "$shouldShowRationale")
                if (shouldShowRationale) {
                    requestLocationPermissions()
                } else {
                    Toast.makeText(this,"You should allow loaction permission from the settings",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

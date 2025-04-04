package com.example.climo.settings.view

import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.climo.R
import com.example.climo.settings.viewmodel.SettingsViewModel
import com.example.climo.utilities.ApplicationUtils
import com.example.climo.view.ui.theme.InterMedium
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(viewModel: SettingsViewModel) {

    val context = LocalContext.current
    var markerPosition by remember { mutableStateOf(LatLng(30.015214, 31.178947)) }
    var address by remember { mutableStateOf("") }

    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, 15f)
    }

    // Google Map
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPosition,
        onMapClick = { latLng ->
            markerPosition = latLng
            address = ApplicationUtils.getAddressFromLocation(
                latLng.latitude,
                latLng.longitude,
                Geocoder(context)
            )
        }
    ) {
        Marker(
            state = MarkerState(position = markerPosition),
            title = "Selected Location",
        )
    }

    Column(
        modifier = Modifier.run {
            fillMaxSize()
                .padding(bottom = 20.dp)
        },
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(colorResource(R.color.grey), RoundedCornerShape(12.dp))
                .fillMaxWidth(0.8f)
                .padding(12.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (address.isNotEmpty()) address else "Selected Location",
                textAlign = TextAlign.Center,
                fontFamily = InterMedium,
                color = colorResource(R.color.black)
            )
        }
        Button(
            onClick = {
                viewModel.saveLocation(markerPosition.latitude,markerPosition.longitude)
                Toast.makeText(context,"Switched to that location",Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))
        ) {
            Text(stringResource(R.string.choose_location), fontFamily = InterMedium)
        }
    }
}
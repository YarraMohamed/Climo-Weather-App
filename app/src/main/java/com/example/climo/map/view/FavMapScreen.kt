//import android.location.Geocoder
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.*
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.*
//import com.example.climo.R
//import com.example.climo.map.viewmodel.MapViewModel
//import com.example.climo.model.Favourites
//import com.example.climo.utilities.ApplicationUtils
//import com.example.climo.view.ui.theme.InterMedium
//import com.google.android.gms.maps.model.*
//import com.google.maps.android.compose.*
//
//@Composable
//fun FavMapScreen(viewModel : MapViewModel) {
//    val context = LocalContext.current
//    var markerPosition by remember { mutableStateOf(LatLng(30.9971, 31.1007)) }
//    var address by remember { mutableStateOf("") }
//    val cameraPosition = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(markerPosition, 15f)
//    }
//    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
//    var properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        GoogleMap(
//            modifier = Modifier.fillMaxSize(),
//            cameraPositionState = cameraPosition,
//            properties = properties,
//            uiSettings = uiSettings,
//            onMapClick = { latLng ->
//                markerPosition = latLng
//                address = ApplicationUtils.getAddressFromLocation(
//                    markerPosition.latitude,
//                    markerPosition.longitude,
//                    Geocoder(context)
//                )
//            }
//        ) {
//            Marker(
//                state = MarkerState(position = markerPosition),
//                title = "Selected Location",
//            )
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(bottom = 20.dp),
//            verticalArrangement = Arrangement.Bottom,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Box(
//                modifier = Modifier
//                    .background(color = colorResource(R.color.grey), shape = RoundedCornerShape(12.dp))
//                    .fillMaxWidth(0.8f)
//                    .padding(12.dp)
//            ) {
//                Text(
//                    modifier = Modifier.fillMaxWidth(),
//                    text = if (address.isNotEmpty()) address else "Selected Location",
//                    textAlign = TextAlign.Center,
//                    fontFamily = InterMedium,
//                    color = colorResource(R.color.black)
//                )
//            }
//            Button(
//                onClick = {viewModel.addFav(Favourites(markerPosition.latitude,markerPosition.longitude,address))},
//                modifier = Modifier.fillMaxWidth(0.8f),
//                colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))
//            ) {
//                Text(stringResource(R.string.add_to_favourites), fontFamily = InterMedium)
//            }
//        }
//    }
//}

import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.climo.R
import com.example.climo.map.viewmodel.MapViewModel
import com.example.climo.model.Favourites
import com.example.climo.utilities.ApplicationUtils
import com.example.climo.view.ui.theme.InterMedium
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*

@Composable
fun FavMapScreen(viewModel: MapViewModel) {
    var messageState = viewModel.message.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var markerPosition by remember { mutableStateOf(LatLng(30.9971, 31.1007)) }
    var address by remember { mutableStateOf("") }
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, 15f)
    }
    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
    var properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(messageState.value) {
        if(!messageState.value.isNullOrBlank())
            snackbarHostState.showSnackbar(messageState.value!!, duration = SnackbarDuration.Short)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPosition,
                properties = properties,
                uiSettings = uiSettings,
                onMapClick = { latLng ->
                    markerPosition = latLng
                    address = ApplicationUtils.getAddressFromLocation(
                        markerPosition.latitude,
                        markerPosition.longitude,
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .background(color = colorResource(R.color.grey), shape = RoundedCornerShape(12.dp))
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
                        viewModel.addFav(Favourites(markerPosition.latitude, markerPosition.longitude, address))
                    },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))
                ) {
                    Text(stringResource(R.string.add_to_favourites), fontFamily = InterMedium)
                }
            }
        }
    }
}

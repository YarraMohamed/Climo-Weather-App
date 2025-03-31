import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.climo.R
import com.example.climo.map.viewmodel.MapViewModel
import com.example.climo.model.Favourites
import com.example.climo.view.ui.theme.InterMedium
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.*

@Composable
fun FavMapScreen(viewModel: MapViewModel) {
    val context = LocalContext.current
    var messageState = viewModel.message.collectAsStateWithLifecycle()
    val markerPosition by viewModel.markerPosition.collectAsState()
    val address by viewModel.address.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val placesClient = remember { Places.createClient(context) }
    val snackbarHostState = remember { SnackbarHostState() }

    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, 15f)
    }

        LaunchedEffect(messageState.value) {
        if(!messageState.value.isNullOrBlank())
            snackbarHostState.showSnackbar(messageState.value!!, duration = SnackbarDuration.Short)
    }
    Scaffold (
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPosition,
                onMapClick = { latLng ->
                    viewModel.updateMarkerPosition(latLng)
                    cameraPosition.move(CameraUpdateFactory.newLatLng(latLng))
                }
            ) {
                Marker(
                    state = MarkerState(position = markerPosition),
                    title = "Selected Location",
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        if (it.length > 2) viewModel.searchPlaces(it,placesClient)
                    },
                    placeholder = { Text("Search location") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = colorResource(R.color.grey), RoundedCornerShape(16.dp))
                )

                if (suggestions.isNotEmpty()) {
                    DropdownMenu(
                        modifier = Modifier.fillMaxWidth(0.9f),
                        expanded = suggestions.isNotEmpty(),
                        onDismissRequest = { viewModel.searchPlaces("",placesClient) }
                    ) {
                        suggestions.forEach { suggestion ->
                            DropdownMenuItem(
                                text = { Text(suggestion) },
                                onClick = {
                                    viewModel.selectPlace(suggestion)
                                    searchQuery = suggestion
                                    cameraPosition.move(CameraUpdateFactory.newLatLng(viewModel.getLatLngFromAddress(suggestion)!!))
                                }
                            )
                        }
                    }
                }
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
                    onClick = { viewModel.addFav(Favourites(markerPosition.latitude, markerPosition.longitude, address)) },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.blue))
                ) {
                    Text("Add to Favourites", fontFamily = InterMedium)
                }
            }
        }
    }
}


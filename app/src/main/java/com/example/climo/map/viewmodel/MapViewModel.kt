package com.example.climo.map.viewmodel

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.climo.data.Repository
import com.example.climo.favourites.viewmodel.FavouritesViewModel
import com.example.climo.model.Favourites
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel(private val repo: Repository,private val geocoder: Geocoder) : ViewModel() {

    private var messageFlow = MutableStateFlow("")
    val message = messageFlow.asStateFlow()

    private val markerPositionFlow = MutableStateFlow(LatLng(30.015214, 31.178947))
    val markerPosition = markerPositionFlow.asStateFlow()

    private val addressFlow = MutableStateFlow("")
    val address = addressFlow.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")
    val searchQuery = searchQueryFlow.asStateFlow()

    private val suggestionsFlow = MutableStateFlow<List<String>>(emptyList())
    val suggestions = suggestionsFlow.asStateFlow()

    private val token = AutocompleteSessionToken.newInstance()

    fun addFav(favourite: Favourites) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.addFavourite(favourite)
                messageFlow.value = "${favourite.address} is added to favourites"
                Log.i("TAG", "addedFav: ")
            } catch (ex: Exception) {
                Log.i("TAG", "Error adding")
            }
        }
    }

    fun updateMarkerPosition(latLng: LatLng) {
        viewModelScope.launch {
            markerPositionFlow.value = latLng
            addressFlow.value = getAddressFromLatLng(latLng)
        }
    }

    fun searchPlaces(query: String, placesClient: PlacesClient) {
        val request = com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                suggestionsFlow.value = response.autocompletePredictions.map { it.getFullText(null).toString() }
            }
            .addOnFailureListener {
                suggestionsFlow.value = emptyList()
            }
    }

    fun selectPlace(selectedAddress: String) {
        viewModelScope.launch {
            val latLng = getLatLngFromAddress(selectedAddress)
            if (latLng != null) {
                markerPositionFlow.value = latLng
                addressFlow.value = selectedAddress
                searchQueryFlow.value = selectedAddress
                suggestionsFlow.value = emptyList()
            }
        }
    }

    fun getLatLngFromAddress(address: String): LatLng? {
        val location = geocoder.getFromLocationName(address, 1)
        return if (!location.isNullOrEmpty()) {
            LatLng(location[0].latitude, location[0].longitude)
        } else {
            null
        }
    }

    private fun getAddressFromLatLng(latLng: LatLng): String {
        val location = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return location?.get(0)?.locality +","+location?.get(0)?.countryName
    }

    class MapFactory(private val repo: Repository, private val geocoder: Geocoder) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MapViewModel(repo, geocoder) as T
        }
    }
}

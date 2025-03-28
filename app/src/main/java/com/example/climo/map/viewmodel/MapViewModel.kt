package com.example.climo.map.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.climo.data.Repository
import com.example.climo.favourites.viewmodel.FavouritesViewModel
import com.example.climo.model.Favourites
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel(private val repo: Repository) : ViewModel() {

    private var messageFlow = MutableStateFlow<String>("")
    var message = messageFlow.asStateFlow()

    fun addFav(favourite: Favourites) {
        viewModelScope.launch {
            try {
                repo.addFavourite(favourite)
                messageFlow.value = "${favourite.address} is added to favourites"
                Log.i("TAG", "addedFav: ")
            } catch (ex: Exception) {
                Log.i("TAG", "Error adding")
            }
        }
    }

    class MapFactory(private val repo: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MapViewModel(repo) as T
        }
    }
}
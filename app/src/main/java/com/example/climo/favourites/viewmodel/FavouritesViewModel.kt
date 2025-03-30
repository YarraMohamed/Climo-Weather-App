package com.example.climo.favourites.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.climo.data.Repository
import com.example.climo.home.viewmodel.HomeViewModel
import com.example.climo.model.Deatils
import com.example.climo.model.Favourites
import com.example.climo.model.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesViewModel(private val repo: Repository) : ViewModel(){

    init{
        getFav()
    }

    private var favouritesFlow : MutableStateFlow<Response<List<Favourites>>> = MutableStateFlow(Response.Loading)
    var favList = favouritesFlow.asStateFlow()

    private var messageFlow = MutableStateFlow<String>("")
    var message = messageFlow.asStateFlow()

    fun getFav() {
        viewModelScope.launch(Dispatchers.IO){
            try {
                repo.getFavourites()
                    .catch { favouritesFlow.value = Response.Failure(Throwable("Error getting list")) }
                    .collect{
                        favouritesFlow.value = Response.Success(it)
                    }
            }catch (ex:Exception){
                favouritesFlow.value = Response.Failure(Throwable("Error getting list"))
            }
        }
    }

    fun deleteFav(favourite: Favourites){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteFavourite(favourite)
                messageFlow.value = "${favourite.address} is deleted Successfully"
            }catch (ex:Exception){

            }
        }
    }

    class FavouritesFactory(private val repo: Repository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavouritesViewModel(repo) as T
        }
    }
}
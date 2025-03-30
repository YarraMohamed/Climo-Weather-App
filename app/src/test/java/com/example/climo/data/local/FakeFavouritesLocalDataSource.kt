package com.example.climo.data.local

import com.example.climo.model.Favourites
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class FakeFavouritesLocalDataSource ():
    FavouritesLocalDataSource {

    private val favs = MutableStateFlow<List<Favourites>>(emptyList())

    override suspend fun getFavouriteList(): Flow<List<Favourites>> {
        return favs
    }

    override suspend fun addFav(favourite: Favourites) {
        favs.update { currentList -> currentList + favourite }
    }

    override suspend fun deleteFav(favourite: Favourites) {
        favs.update { currentList -> currentList.filterNot { it.id == favourite.id } }
    }
}
package com.example.climo.data.local

import com.example.climo.model.Favourites
import kotlinx.coroutines.flow.Flow

interface FavouritesLocalDataSource {
    suspend fun getFavouriteList(): Flow<List<Favourites>>
    suspend fun addFav(favourite: Favourites)
    suspend fun deleteFav(favourite: Favourites)
}
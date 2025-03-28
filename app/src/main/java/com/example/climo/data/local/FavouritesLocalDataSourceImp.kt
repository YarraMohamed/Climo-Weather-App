package com.example.climo.data.local

import com.example.climo.data.db.FavouritesDAO
import com.example.climo.model.Favourites
import kotlinx.coroutines.flow.Flow

class FavouritesLocalDataSourceImp(private val favouritesDAO: FavouritesDAO) :
    FavouritesLocalDataSource {

    override suspend fun getFavouriteList() : Flow<List<Favourites>>{
        return favouritesDAO.getFavouriteList()
    }

    override suspend fun addFav(favourite: Favourites){
        return favouritesDAO.addFav(favourite)
    }

    override suspend fun deleteFav(favourite: Favourites){
        return favouritesDAO.deleteFavourite(favourite)
    }

}
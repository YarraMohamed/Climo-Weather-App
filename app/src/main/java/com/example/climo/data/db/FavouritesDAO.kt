package com.example.climo.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.climo.model.Favourites
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDAO {

    @Query("SELECT * FROM fav_table")
    fun getFavouriteList() : Flow<List<Favourites>>

    @Delete
    suspend fun deleteFavourite(favourite: Favourites)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFav(favourite: Favourites)

}

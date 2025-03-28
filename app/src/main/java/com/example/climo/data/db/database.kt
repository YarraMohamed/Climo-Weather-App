package com.example.climo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.climo.model.Favourites

@Database(entities = arrayOf(Favourites::class), version = 1)
abstract class database :RoomDatabase() {
    abstract fun getFavouritesDAO() : FavouritesDAO

    companion object {
        @Volatile
        private var instance: database? = null

        fun getInstance(context: Context): database {
            return instance ?: synchronized(this) {
                val dbInstance = Room.databaseBuilder(
                    context.applicationContext, database::class.java, "weather_db"
                ).fallbackToDestructiveMigration()
                    .build()
                instance = dbInstance
                dbInstance
            }
        }
    }

}
package com.example.climo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.climo.model.Favourites

@Database(entities = arrayOf(Favourites::class), version = 1)
abstract class AppDatabase :RoomDatabase() {
    abstract fun getFavouritesDAO() : FavouritesDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val dbInstance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "weather_db"
                ).fallbackToDestructiveMigration()
                    .build()
                instance = dbInstance
                dbInstance
            }
        }
    }

}
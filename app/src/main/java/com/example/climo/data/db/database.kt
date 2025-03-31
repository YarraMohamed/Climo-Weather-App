package com.example.climo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.climo.model.DailyDetails
import com.example.climo.model.Favourites
import com.example.climo.model.HorulyDetails
import com.example.climo.model.WeatherStatus
import com.example.climo.model.WeatherTypeConverter

@Database(entities = arrayOf(Favourites::class,
    WeatherStatus::class,
    HorulyDetails::class,DailyDetails::class), version = 1, exportSchema = false)
@TypeConverters(WeatherTypeConverter::class)
abstract class AppDatabase :RoomDatabase() {
    abstract fun getFavouritesDAO() : FavouritesDAO
    abstract fun getWeatherDAO() : WeatherDAO

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
package com.example.climo.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.climo.model.DailyDetails
import com.example.climo.model.Deatils
import com.example.climo.model.HorulyDetails
import com.example.climo.model.WeatherStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherStatus(weatherStatus: WeatherStatus)
    @Query("DELETE FROM weather_details WHERE lat = :lat AND lon = :lon")
    suspend fun deleteWeatherStatus(lat:Double,lon:Double)
    @Query("Select * From weather_details Where lat= :lat AND lon = :lon")
    fun getWeatherStatus(lat: Double,lon: Double) : Flow<WeatherStatus?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyDetails(horulyDetails: HorulyDetails)
    @Query("DELETE FROM horuly_deatils WHERE lat = :lat AND lon = :lon")
    suspend fun deleteHourlyDetails(lat:Double,lon:Double)
    @Query("Select * From horuly_deatils Where lat= :lat AND lon = :lon")
    fun getHourlyDetails(lat: Double,lon: Double) : Flow<HorulyDetails?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyDetails(dailyDetails: DailyDetails)
    @Query("DELETE FROM daily_deatils WHERE lat = :lat AND lon = :lon")
    suspend fun deleteDailyDetails(lat:Double,lon:Double)
    @Query("Select * From daily_deatils Where lat= :lat AND lon = :lon")
    fun getDailyDetails(lat: Double,lon: Double) : Flow<DailyDetails?>

}
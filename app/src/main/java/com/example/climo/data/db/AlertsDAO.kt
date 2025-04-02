package com.example.climo.data.db

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.climo.model.Alerts
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDAO {

    @Query("SELECT * FROM alerts_table")
    fun getAlerts() : Flow<List<Alerts>>

    @Delete
    suspend fun deleteAlert(alert:Alerts)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlert(alert: Alerts)

    @Query("Delete From alerts_table Where startTime=:startTime AND date=:date AND endTime=:endTime")
    suspend fun deleteSomeAlert(startTime:String,endTime:String,date:String)

}
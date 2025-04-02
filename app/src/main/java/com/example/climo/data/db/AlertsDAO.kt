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
    suspend fun deleteAlert(alert:Alerts){
        Log.i("Worker", "deleteAlert: in DAO ")
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAlert(alert: Alerts)

}
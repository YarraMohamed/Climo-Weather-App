package com.example.climo.data.local

import com.example.climo.model.Alerts
import kotlinx.coroutines.flow.Flow

interface AlertsLocalDataSource {
    suspend fun getAlerts(): Flow<List<Alerts>>

    suspend fun addAlert(alert: Alerts)

    suspend fun deleteAlert(alert: Alerts)
}
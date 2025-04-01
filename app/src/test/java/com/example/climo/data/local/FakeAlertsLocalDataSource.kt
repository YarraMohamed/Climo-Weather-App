package com.example.climo.data.local

import com.example.climo.model.Alerts
import kotlinx.coroutines.flow.Flow

class FakeAlertsLocalDataSource:AlertsLocalDataSource {
    override suspend fun getAlerts(): Flow<List<Alerts>> {
        TODO("Not yet implemented")
    }

    override suspend fun addAlert(alert: Alerts) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: Alerts) {
        TODO("Not yet implemented")
    }
}
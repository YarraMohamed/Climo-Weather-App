package com.example.climo.data.local

import com.example.climo.data.db.AlertsDAO
import com.example.climo.model.Alerts
import kotlinx.coroutines.flow.Flow

class AlertsLocalDataSourceImp(private val alertsDAO: AlertsDAO) : AlertsLocalDataSource {

    override suspend fun getAlerts() : Flow<List<Alerts>> {
        return alertsDAO.getAlerts()
    }

    override suspend fun addAlert(alert:Alerts) {
        return alertsDAO.addAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alerts){
        return alertsDAO.deleteAlert(alert)
    }
}
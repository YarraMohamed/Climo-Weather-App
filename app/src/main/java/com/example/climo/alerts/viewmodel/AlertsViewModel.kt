package com.example.climo.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.climo.data.Repository
import com.example.climo.favourites.viewmodel.FavouritesViewModel
import com.example.climo.model.Alerts
import com.example.climo.model.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertsViewModel(private val repo: Repository) : ViewModel() {
    private var alertsFlow: MutableStateFlow<Response<List<Alerts>>> =
        MutableStateFlow(Response.Loading)
    var alerts = alertsFlow.asStateFlow()

    private var messageFlow = MutableStateFlow("")
    var message = messageFlow.asStateFlow()

    init {
        getAlerts()
    }

    fun getAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getAlerts()
                    .catch { alertsFlow.value = Response.Failure(Throwable(it.message)) }
                    .collect {
                        alertsFlow.value = Response.Success(it)
                    }
            } catch (ex: Exception) {
                alertsFlow.value = Response.Failure(Throwable(ex.message))
            }
        }
    }

    fun addAlert(alert:Alerts){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.addAlert(alert)
                repo.makeAlert(alert)
                messageFlow.value="alert for day ${alert.date} is added successfully"
            } catch (ex: Exception) {
                messageFlow.value = "alert for day ${alert.date} cannot be deleted"
            }
        }
    }

    fun deleteAlert(alert: Alerts){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteAlert(alert)
                repo.cancelAlert(alert)
                messageFlow.value="alert for day ${alert.date} is deleted successfully"
            } catch (ex: Exception) {
                messageFlow.value = "alert for day ${alert.date} cannot be deleted"
            }
        }
    }

    class AlertsFactory(private val repo: Repository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AlertsViewModel(repo) as T
        }
    }
}
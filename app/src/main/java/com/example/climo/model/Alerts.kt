package com.example.climo.model

import kotlinx.serialization.Serializable

@Serializable
data class Alerts(val address:String,val startTime:String,val endTime:String)

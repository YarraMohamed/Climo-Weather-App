package com.example.climo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts_table")
data class Alerts(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val address:String,
    val startTime:String,
    val endTime:String,
    val date:String= "")

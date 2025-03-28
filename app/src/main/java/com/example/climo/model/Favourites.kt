package com.example.climo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_table")
data class Favourites(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val latitude:Double,
    val longitude:Double,
    val address:String
){
    constructor(latitude: Double, longitude: Double, address: String) : this(0, latitude, longitude, address)
}

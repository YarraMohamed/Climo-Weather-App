package com.example.climo.utilities

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getFromattedTime() : String{
    return SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
}

fun getFormattedDate() : String{
    return SimpleDateFormat("MMMM, d",Locale.getDefault()).format(Date())
}

fun formatTime(time: String): String {
    val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    return outputFormat.format(inputFormat.parse(time)!!)
}

fun formatDate(date:String) : String{
    val inputFormat = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
    val outptFormat = SimpleDateFormat("MMMM, d",Locale.getDefault())
    return outptFormat.format(inputFormat.parse(date)!!)
}
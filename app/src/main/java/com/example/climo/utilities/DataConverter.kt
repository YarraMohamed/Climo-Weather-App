package com.example.climo.utilities

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.climo.model.Alerts
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

val gson = Gson()
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

//Alerts
fun fromAlerts(alert: Alerts) : String = gson.toJson(alert)
fun toAlerts(data:String) : Alerts {
    val type = object : TypeToken<Alerts>() {}.type
    return gson.fromJson(data,type)
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseDateTime(date: String, time: String): LocalDateTime {
    val dateFormatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH)
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    val currentYear = Year.now().value
    val cleanedDate = date.replace(",", "").trim()
    val formattedDate = "$cleanedDate $currentYear"

    val localDate = LocalDate.parse(formattedDate, dateFormatter)
    val localTime = LocalTime.parse(time, timeFormatter)

    return LocalDateTime.of(localDate, localTime)
}



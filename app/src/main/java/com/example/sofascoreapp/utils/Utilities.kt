package com.example.sofascoreapp.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Utilities {

    fun getMatchHour(dateTime: String): String {
        val zonedDateTime = ZonedDateTime.parse(dateTime)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val hourMinute = formatter.format(zonedDateTime)
        return hourMinute
    }

}
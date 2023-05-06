package com.example.sofascoreapp.utils

import com.example.sofascoreapp.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class Utilities {

    fun getMatchHour(dateTime: String): String {
        val zonedDateTime = ZonedDateTime.parse(dateTime)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val hourMinute = formatter.format(zonedDateTime)
        return hourMinute
    }

    fun getTodaysDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }

    fun getDayInWeek(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObject = inputFormat.parse(date)
        val dayOfWeekFormat = SimpleDateFormat("EEE", Locale.getDefault())
        return dayOfWeekFormat.format(dateObject).toUpperCase()
    }

    fun getAvailableDateShort(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObject = inputFormat.parse(date)
        val dayOfWeekFormat = SimpleDateFormat("dd.MM.", Locale.getDefault())
        return dayOfWeekFormat.format(dateObject).toUpperCase()
    }

    fun getLongDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObject = inputFormat.parse(date)
        val dayOfWeekFormat = SimpleDateFormat("EEE, dd.MM.yyyy.", Locale.getDefault())
        return dayOfWeekFormat.format(dateObject)
    }

}
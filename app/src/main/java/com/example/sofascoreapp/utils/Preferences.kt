package com.example.sofascoreapp.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.sofascoreapp.R

class Preferences(val activity: Activity) {

    private val preferences = activity.applicationContext.getSharedPreferences(
        activity.applicationContext.resources.getString(R.string.package_name),
        Context.MODE_PRIVATE
    )
    private val extrasThemes = activity.applicationContext.resources.getStringArray(R.array.themes)
    private val extrasDates =
        activity.applicationContext.resources.getStringArray(R.array.dateFormats)
    private val resources = activity.applicationContext.resources

    fun getCurrentDate() = preferences.getString(extrasDates[0], extrasDates[1])
    fun getCurrentTheme() = preferences.getString(extrasThemes[0], extrasThemes[1])

    fun swapDateFormats() {
        when (getCurrentDate()) {
            extrasDates[1] -> preferences.edit().putString(extrasDates[0], extrasDates[2]).apply()
            else -> preferences.edit().putString(extrasDates[0], extrasDates[1]).apply()
        }
        Utilities().restartApp(activity)
    }

    fun swapTheme() {
        when (getCurrentTheme()) {
            extrasThemes[1] -> preferences.edit().putString(extrasThemes[0], extrasThemes[2])
                .apply()

            else -> preferences.edit().putString(extrasThemes[0], extrasThemes[1]).apply()
        }
        Utilities().restartApp(activity)
    }

    fun checkForChangeDate(newDateFormat: Int) {
        if (getCurrentDate() != preferences.getString(extrasDates[0], extrasDates[newDateFormat])) {
            swapDateFormats()
        }
    }

    fun checkForChangeTheme(newTheme: Int) {
        if (getCurrentTheme() == preferences.getString(extrasThemes[0], extrasThemes[newTheme])) {
            swapTheme()
        }
    }

    fun loadPreferences() {
        when (getCurrentTheme()) {
            extrasThemes[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

}
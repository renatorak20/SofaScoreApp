package com.example.sofascoreapp.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.sofascoreapp.R
import java.util.Locale

@SuppressLint("StaticFieldLeak")
object Preferences {
    private lateinit var context: Context

    private val preferences by lazy {
        context.getSharedPreferences(
            context.resources.getString(R.string.package_name),
            Context.MODE_PRIVATE
        )
    }

    private val extrasThemes by lazy {
        context.resources.getStringArray(R.array.themes)
    }

    private val extrasDates by lazy {
        context.resources.getStringArray(R.array.dateFormats)
    }

    private val extrasLanguages by lazy {
        context.resources.getStringArray(R.array.languages)
    }

    private val resources by lazy {
        context.resources
    }

    fun initialize(context: Context) {
        this.context = context.applicationContext
    }

    fun getCurrentDate() = preferences.getString(extrasDates[0], extrasDates[1])

    fun getCurrentTheme() = preferences.getString(extrasThemes[0], extrasThemes[1])

    fun getCurrentLanguage() =
        preferences.getString(resources.getString(R.string.lang), extrasLanguages[0])

    fun getSavedDateFormat(): Boolean {
        return getCurrentDate() == extrasDates[1]
    }

    fun swapDateFormats() {
        when (getCurrentDate()) {
            extrasDates[1] -> preferences.edit().putString(extrasDates[0], extrasDates[2]).apply()
            else -> preferences.edit().putString(extrasDates[0], extrasDates[1]).apply()
        }
    }

    fun swapTheme() {
        when (getCurrentTheme()) {
            extrasThemes[1] -> preferences.edit().putString(extrasThemes[0], extrasThemes[2])
                .apply()

            else -> preferences.edit().putString(extrasThemes[0], extrasThemes[1]).apply()
        }
    }

    fun setLanguage(langCode: String) {
        preferences.edit().putString(resources.getString(R.string.lang), langCode).apply()
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    private fun setAppLocale(language: String) {
        preferences.edit().putString(extrasLanguages[0], language).apply()
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun loadPreferences() {
        when (getCurrentTheme()) {
            extrasThemes[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        getCurrentLanguage()?.let { setAppLocale(it) }
    }

    fun getSortedListOfAvailableLanguages(): List<Pair<String, String>> {
        val prefLanguageCode = getCurrentLanguage()
        val languagePairs: ArrayList<Pair<String, String>> = arrayListOf()

        for (i in extrasLanguages.indices) {
            val languageCode = extrasLanguages[i]
            val languageLocale = Locale(languageCode)
            val displayLanguage = languageLocale.getDisplayLanguage(Locale(prefLanguageCode))
            languagePairs.add(Pair(languageCode, displayLanguage))
        }

        return languagePairs.sortedBy { it.second }
    }
}

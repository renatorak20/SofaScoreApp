package com.example.sofascoreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.sofascoreapp.databinding.ActivitySettingsBinding
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.toolbar.title.text = getString(R.string.action_settings)

        loadPreferences()
        loadSpinner()

        binding.themeDate.themeRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            Preferences.swapTheme()
            Utilities().restartApp(this)
        }

        binding.themeDate.dateRadioGroup.setOnCheckedChangeListener { radioGroup, id ->
            Preferences.swapDateFormats()
            Utilities().restartApp(this)
        }

        binding.languageSelector.setOnItemClickListener { parent, view, position, id ->
            handleLanguageChange(position)
        }

        binding.toolbar.back.setOnClickListener {
            finish()
        }

    }

    fun loadSpinner() {
        val supportedLanguages = Preferences.getSortedListOfAvailableLanguages()
        val currentLanguage = Preferences.getCurrentLanguage()

        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.tournament_spinner_item,
            supportedLanguages.map { it.second }
        )
        val languageSelector = binding.languageSelector as? MaterialAutoCompleteTextView
        languageSelector?.setAdapter(arrayAdapter)

        val currentLanguageIndex = supportedLanguages.indexOfFirst { it.first == currentLanguage }

        languageSelector?.setText(arrayAdapter.getItem(currentLanguageIndex), false)
    }


    fun handleLanguageChange(position: Int) {
        val supportedLanguages = Preferences.getSortedListOfAvailableLanguages()
        if (Preferences.getCurrentLanguage() != supportedLanguages[position].first) {
            Preferences.setLanguage(supportedLanguages[position].first)
            Utilities().restartApp(this)
        }
    }

    fun loadPreferences() {
        when (Preferences.getCurrentTheme()) {
            "Light" -> binding.themeDate.themeRadioGroup.check(R.id.lightButton)
            else -> binding.themeDate.themeRadioGroup.check(R.id.darkButton)
        }

        when (Preferences.getCurrentDate()) {
            getString(R.string.first_format) -> binding.themeDate.dateRadioGroup.check((R.id.firstFormat))
            else -> binding.themeDate.dateRadioGroup.check((R.id.secondFormat))
        }
    }


}
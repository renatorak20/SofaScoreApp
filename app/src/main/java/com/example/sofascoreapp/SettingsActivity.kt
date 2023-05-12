package com.example.sofascoreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.sofascoreapp.R
import com.example.sofascoreapp.databinding.ActivitySettingsBinding
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import org.intellij.lang.annotations.Language

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
            Preferences(this).swapTheme()
        }

        binding.themeDate.dateRadioGroup.setOnCheckedChangeListener { radioGroup, id ->
            Preferences(this).swapDateFormats()
        }

        binding.languageSelector.setOnItemClickListener { parent, view, position, id ->
            handleLanguageChange(position)
        }

        binding.toolbar.back.setOnClickListener {
            finish()
        }

    }

    fun loadSpinner() {
        val preferences = Preferences(this)
        val supportedLanguages = preferences.getListOfAvailableLanguages()
        val currentLanguage = preferences.getCurrentLanguage()

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
        val supportedLanguages = Preferences(this).getListOfAvailableLanguages()
        if (Preferences(this).getCurrentLanguage() != supportedLanguages[position].first) {
            Preferences(this).setLanguage(supportedLanguages[position].first)
        }
    }

    fun loadPreferences() {
        when (Preferences(this).getCurrentTheme()) {
            getString(R.string.light) -> binding.themeDate.themeRadioGroup.check(R.id.lightButton)
            else -> binding.themeDate.themeRadioGroup.check(R.id.darkButton)
        }

        when (Preferences(this).getCurrentDate()) {
            getString(R.string.first_format) -> binding.themeDate.dateRadioGroup.check((R.id.firstFormat))
            else -> binding.themeDate.dateRadioGroup.check((R.id.secondFormat))
        }
    }


}
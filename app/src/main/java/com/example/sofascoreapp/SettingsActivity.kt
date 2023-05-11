package com.example.sofascoreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.sofascoreapp.R
import com.example.sofascoreapp.databinding.ActivitySettingsBinding
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import org.intellij.lang.annotations.Language

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.toolbar.title.text = getString(R.string.action_settings)

        when (Preferences(this).getCurrentTheme()) {
            getString(R.string.light) -> binding.themeDate.themeRadioGroup.check(R.id.lightButton)
            else -> binding.themeDate.themeRadioGroup.check(R.id.darkButton)
        }

        when (Preferences(this).getCurrentDate()) {
            getString(R.string.first_format) -> binding.themeDate.dateRadioGroup.check((R.id.firstFormat))
            else -> binding.themeDate.dateRadioGroup.check((R.id.secondFormat))
        }

        binding.themeDate.themeRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            Preferences(this).swapTheme()
        }

        binding.themeDate.dateRadioGroup.setOnCheckedChangeListener { radioGroup, id ->
            Preferences(this).swapDateFormats()
        }

        binding.toolbar.back.setOnClickListener {
            finish()
        }

    }
}
package com.example.sofascoreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sofascoreapp.R
import com.example.sofascoreapp.databinding.ActivitySettingsBinding
import org.intellij.lang.annotations.Language

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.toolbar.title.text = getString(R.string.action_settings)
    }
}
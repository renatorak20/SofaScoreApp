package com.example.sofascoreapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.databinding.SharedActivityLayoutBinding
import com.example.sofascoreapp.utils.Utilities

class TournamentsActivity : AppCompatActivity() {

    private lateinit var binding: SharedActivityLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SharedActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navController = findNavController(R.id.nav_host_fragment_activity_tournaments)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_tournaments_football,
                R.id.navigation_tournaments_basketball,
                R.id.navigation_tournaments_american_football
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navViewTournaments.setupWithNavController(navController)

        binding.navViewTournaments.isItemActiveIndicatorEnabled = false


        binding.toolbarTournaments.back.setOnClickListener {
            finish()
        }

        enableViews()

    }

    fun enableViews() {
        binding.navViewMain.visibility = View.GONE
        binding.toolbarMain.layout.visibility = View.GONE
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
            ?.let { fragment ->
                fragment.view?.visibility = View.GONE
            }
        binding.navViewTournaments.visibility = View.VISIBLE
        binding.toolbarTournaments.layout.visibility = View.VISIBLE
    }
}
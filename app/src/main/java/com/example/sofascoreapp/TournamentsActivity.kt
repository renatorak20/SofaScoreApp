package com.example.sofascoreapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.sofascoreapp.databinding.SharedActivityLayoutBinding
import com.example.sofascoreapp.ui.SectionsPagerAdapter
import com.example.sofascoreapp.ui.adapters.MatchIncidentsAdapter
import com.example.sofascoreapp.ui.adapters.TournamentsAdapter
import com.example.sofascoreapp.viewmodel.TournamentsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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
package com.example.sofascoreapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.sofascoreapp.databinding.ActivityTournamentsBinding
import com.example.sofascoreapp.ui.SectionsPagerAdapter
import com.example.sofascoreapp.ui.adapters.MatchIncidentsAdapter
import com.example.sofascoreapp.ui.adapters.TournamentsAdapter
import com.example.sofascoreapp.viewmodel.TournamentsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TournamentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTournamentsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTournamentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_tournaments_football,
                R.id.navigation_tournaments_basketball,
                R.id.navigation_tournaments_american_football
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        binding.navView.isItemActiveIndicatorEnabled = false


        binding.toolbar.back.setOnClickListener {
            finish()
        }

    }

}
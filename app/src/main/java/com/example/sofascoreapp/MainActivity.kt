package com.example.sofascoreapp

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sofascoreapp.databinding.SharedActivityLayoutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: SharedActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SharedActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)



        supportActionBar?.hide()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_matches_football,
                R.id.navigation_matches_basketball,
                R.id.navigation_matches_american_football
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navViewMain.setupWithNavController(navController)


        binding.toolbarMain.tournamentsIcon.setOnClickListener {
            val intent = Intent(this, TournamentsActivity::class.java)
            startActivity(intent)
        }

        binding.navViewMain.isItemActiveIndicatorEnabled = false

        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_tournaments)
            ?.let { fragment ->
                fragment.view?.visibility = View.GONE
            }
    }


}

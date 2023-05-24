package com.example.sofascoreapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.databinding.ActivityTeamDetailsBinding
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.utils.Utilities.Companion.loadImage
import com.example.sofascoreapp.utils.Utilities.Companion.showNoInternetDialog
import com.example.sofascoreapp.viewmodel.TeamDetailsViewModel

class TeamDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamDetailsBinding
    private lateinit var teamDetailsViewModel: TeamDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTeamDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        teamDetailsViewModel = ViewModelProvider(this)[TeamDetailsViewModel::class.java]

        supportActionBar?.hide()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_team_details,
                R.id.navigation_team_matches,
                R.id.navigation_team_standings,
                R.id.navigation_team_squad
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)


        binding.toolbar.back.setOnClickListener {
            finish()
        }

        binding.navView.isItemActiveIndicatorEnabled = false


        teamDetailsViewModel.setTeamID(intent.getIntExtra("teamID", 0))

        teamDetailsViewModel.getTeamID().observe(this) {
            getInfo()
        }

        teamDetailsViewModel.getTeamDetails().observe(this) { response ->
            if (response.isSuccessful) {
                binding.teamToolbar.name.text = response.body()!!.name
                binding.teamToolbar.country.text = response.body()!!.country.name
                binding.teamToolbar.logo.loadImage(this, DataType.TEAM, response.body()!!.id)
            }
        }
    }

    fun getInfo() {
        if (Utilities().isNetworkAvailable(this)) {
            teamDetailsViewModel.getLatestTeamDetails()
        } else {
            showNoInternetDialog(this) { getInfo() }
        }
    }

    companion object {
        fun start(context: Context, teamID: Int) {
            context.startActivity(
                Intent(context, TeamDetailsActivity::class.java).putExtra(
                    "teamID",
                    teamID
                )
            )
        }
    }

}
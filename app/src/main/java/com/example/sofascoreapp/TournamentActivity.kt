package com.example.sofascoreapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.sofascoreapp.databinding.ActivityTournamentBinding
import com.example.sofascoreapp.viewmodel.SpecificTournamentViewModel

class TournamentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTournamentBinding
    private lateinit var viewModel: SpecificTournamentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTournamentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SpecificTournamentViewModel::class.java]
        viewModel.setTournamentID(intent.getIntExtra("tournamentID", -1))

        viewModel.getTournamentID().observe(this) {
            viewModel.getLatestTournamentStandings()
            viewModel.getLatestTournamentInfo()
        }

        viewModel.getTournamentInfo().observe(this) {
            if (it.isSuccessful) {
                binding.tournamentToolbar.logo.load(
                    getString(
                        R.string.tournament_icon_url,
                        it.body()!!.id
                    )
                )
                binding.tournamentToolbar.name.text = it.body()!!.name
                binding.tournamentToolbar.country.text = it.body()!!.country.name
            }
        }

        supportActionBar?.hide()


        val navController = findNavController(R.id.nav_host_fragment_activity_specific_tournament)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_tournament_matches,
                R.id.navigation_tournament_standings,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        binding.toolbar.back.setOnClickListener {
            finish()
        }

        binding.navView.isItemActiveIndicatorEnabled = false

    }
}
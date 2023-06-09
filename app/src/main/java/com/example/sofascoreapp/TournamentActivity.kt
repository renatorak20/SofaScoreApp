package com.example.sofascoreapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.model.UiModel
import com.example.sofascoreapp.databinding.ActivityTournamentBinding
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.utils.Utilities.Companion.loadImage
import com.example.sofascoreapp.utils.Utilities.Companion.showNoInternetDialog
import com.example.sofascoreapp.viewmodel.SpecificTournamentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
            getInfo()
        }

        viewModel.getTournamentInfo().observe(this) {
            if (it.isSuccessful) {
                binding.tournamentToolbar.logo.loadImage(this, DataType.TOURNAMENT, it.body()!!.id)
                binding.tournamentToolbar.name.text = it.body()!!.name
                binding.tournamentToolbar.country.text = it.body()!!.country.name
                Utilities.loadCountryImage(
                    it.body()?.country?.name!!,
                    binding.tournamentToolbar.countryImage
                )
            }
        }

        supportActionBar?.hide()
        binding.tournamentToolbar.favouriteIcon.visibility = View.INVISIBLE

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

    private fun getInfo() {
        if (Utilities().isNetworkAvailable(this)) {
            viewModel.getLatestTournamentStandings()
            viewModel.getLatestTournamentInfo()
        } else {
            showNoInternetDialog(this) { getInfo() }
        }
    }

    companion object {
        fun start(context: Context, tournament: Tournament) {
            context.startActivity(
                Intent(context, TournamentActivity::class.java).putExtra(
                    "tournamentID",
                    tournament.id
                )
            )
        }

        fun start(context: Context, tournament: UiModel.SeparatorTournament) {
            context.startActivity(
                Intent(context, TournamentActivity::class.java).putExtra(
                    "tournamentID",
                    tournament.event.tournament.id
                )
            )
        }

    }

}
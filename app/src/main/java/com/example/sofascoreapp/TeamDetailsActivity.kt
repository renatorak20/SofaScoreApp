package com.example.sofascoreapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
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
    var isFavourite = false

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
                binding.teamToolbar.logo.load(
                    getString(
                        R.string.team_icon_url,
                        response.body()!!.id
                    )
                )

                Utilities.loadCountryImage(
                    response.body()!!.country.name,
                    binding.teamToolbar.countryImage
                )
            }
        }

        teamDetailsViewModel.favourites.observe(this) {
            if (it.map { favourite -> favourite.id }
                    .contains(teamDetailsViewModel.getTeamID().value)) {
                isFavourite = true
                setStarFull()
            } else {
                isFavourite = false
                setStarEmpty()
            }
        }

        binding.teamToolbar.favouriteIcon.setOnClickListener {
            if (isFavourite) {
                teamDetailsViewModel.removeFromFavourites(this)
                setStarEmpty()
            } else {
                teamDetailsViewModel.addToFavourite(this)
                setStarFull()
            }

            isFavourite = !isFavourite
        }
    }

    private fun getInfo() {
        if (Utilities().isNetworkAvailable(this)) {
            teamDetailsViewModel.getLatestTeamDetails()
        } else {
            showNoInternetDialog(this) { getInfo() }
        }
    }

    private fun setStarEmpty() {
        binding.teamToolbar.favouriteIcon.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.ic_star_outline
            )
        )
    }

    private fun setStarFull() {
        binding.teamToolbar.favouriteIcon.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.ic_star_fill
            )
        )
    }

    override fun onStart() {
        super.onStart()
        teamDetailsViewModel.getFavourites(this)
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
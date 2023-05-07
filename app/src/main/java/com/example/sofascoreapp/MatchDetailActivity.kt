package com.example.sofascoreapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bumptech.glide.util.Util
import com.example.sofascoreapp.data.model.EventStatusEnum
import com.example.sofascoreapp.data.model.Incident
import com.example.sofascoreapp.data.model.WinnerCode
import com.example.sofascoreapp.databinding.ActivityMainBinding
import com.example.sofascoreapp.databinding.ActivityMatchDetailBinding
import com.example.sofascoreapp.ui.adapters.MatchIncidentsAdapter
import com.example.sofascoreapp.ui.adapters.TournamentsAdapter
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.MatchDetailViewModel
import java.util.ArrayList
import java.util.Collections

class MatchDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchDetailBinding
    private lateinit var matchViewModel: MatchDetailViewModel
    private lateinit var recyclerAdapter: MatchIncidentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        matchViewModel = ViewModelProvider(this)[MatchDetailViewModel::class.java]

        supportActionBar?.hide()

        matchViewModel.matchID = intent.getIntExtra("matchID", 0)

        matchViewModel.getEventInfo()
        matchViewModel.getEventIncidents()

        matchViewModel.getIncidents().observe(this) {
            if (it.isSuccessful && it.body()!!.isNotEmpty()) {
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                val reversedList = it.body()!!.reversed() as ArrayList<Incident>
                recyclerAdapter = MatchIncidentsAdapter(this, reversedList)
                binding.recyclerView.adapter = recyclerAdapter
            }
        }

        matchViewModel.getEvent().observe(this) {
            if (it.isSuccessful && it.body() != null) {

                if (it.body()!!.status == EventStatusEnum.NOTSTARTED) {
                    binding.noResultsLayout.layout.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE

                    binding.matchHeader.scoreLayout.layout.visibility = View.INVISIBLE
                    binding.matchHeader.notStartedLayout.layout.visibility = View.VISIBLE

                    binding.matchHeader.notStartedLayout.date.text =
                        Utilities().getDate(it.body()!!.startDate!!)
                    binding.matchHeader.notStartedLayout.hour.text =
                        Utilities().getMatchHour(it.body()!!.startDate!!)

                } else {

                    binding.matchHeader.scoreLayout.homeTeamScore.text =
                        it.body()!!.homeScore.total.toString()
                    binding.matchHeader.scoreLayout.awayTeamScore.text =
                        it.body()!!.awayScore.total.toString()
                    binding.matchHeader.scoreLayout.currentMinute.text =
                        getString(R.string.full_time)

                    val typedValue = TypedValue()
                    theme.resolveAttribute(
                        R.attr.on_surface_on_surface_lv_1,
                        typedValue,
                        true
                    );
                    val teamColor = ContextCompat.getColor(this, typedValue.resourceId)

                    when (it.body()!!.winnerCode) {
                        WinnerCode.HOME -> binding.matchHeader.scoreLayout.homeTeamScore.setTextColor(
                            teamColor
                        )

                        WinnerCode.DRAW -> {
                            binding.matchHeader.scoreLayout.homeTeamScore.setTextColor(teamColor)
                            binding.matchHeader.scoreLayout.awayTeamScore.setTextColor(teamColor)
                            binding.matchHeader.scoreLayout.minus.setTextColor(teamColor)
                        }

                        else -> binding.matchHeader.scoreLayout.awayTeamScore.setTextColor(teamColor)
                    }
                }

                binding.toolbar.toolbarLeagueIcon.load(
                    getString(
                        R.string.tournament_icon_url,
                        it.body()!!.tournament.id
                    )
                )

                binding.matchHeader.homeTeamLayout.teamIcon.load(
                    getString(
                        R.string.team_icon_url,
                        it.body()!!.homeTeam.id
                    )
                )
                binding.matchHeader.awayTeamLayout.teamIcon.load(
                    getString(
                        R.string.team_icon_url,
                        it.body()!!.awayTeam.id
                    )
                )

                binding.matchHeader.homeTeamLayout.teamTitle.text = it.body()!!.homeTeam.name
                binding.matchHeader.awayTeamLayout.teamTitle.text = it.body()!!.awayTeam.name

                binding.toolbar.toolbarLeagueTitle.text = getString(
                    R.string.match_toolbar_text,
                    it.body()!!.tournament.sport.name,
                    it.body()!!.tournament.country.name,
                    it.body()!!.tournament.name,
                    it.body()!!.round
                )


            }
        }

        binding.matchHeader.homeTeamLayout.root.setOnClickListener {
            val intent = Intent(this, TeamDetailsActivity::class.java)
            intent.putExtra("teamID", matchViewModel.getEvent().value?.body()!!.homeTeam.id)
            startActivity(intent)
        }

        binding.matchHeader.awayTeamLayout.root.setOnClickListener {
            val intent = Intent(this, TeamDetailsActivity::class.java)
            intent.putExtra("teamID", matchViewModel.getEvent().value?.body()!!.awayTeam.id)
            startActivity(intent)
        }

        binding.toolbar.back.setOnClickListener {
            finish()
        }

    }
}
package com.example.sofascoreapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.data.model.EventStatusEnum
import com.example.sofascoreapp.data.model.Incident
import com.example.sofascoreapp.data.model.SportType
import com.example.sofascoreapp.data.model.WinnerCode
import com.example.sofascoreapp.databinding.ActivityMatchDetailBinding
import com.example.sofascoreapp.ui.adapters.MatchIncidentsAdapter
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.utils.Utilities.Companion.loadImage
import com.example.sofascoreapp.utils.Utilities.Companion.showNoInternetDialog
import com.example.sofascoreapp.viewmodel.MatchDetailViewModel


class MatchDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchDetailBinding
    private lateinit var matchViewModel: MatchDetailViewModel
    private lateinit var recyclerAdapter: MatchIncidentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMatchDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Preferences.initialize(this)

        matchViewModel = ViewModelProvider(this)[MatchDetailViewModel::class.java]

        supportActionBar?.hide()

        matchViewModel.matchID = intent.getIntExtra("matchID", 0)

        getInfo()

        matchViewModel.getIncidents().observe(this) {
            if (it.isSuccessful && it.body()?.isNotEmpty()!!) {
                binding.recyclerView.layoutManager = LinearLayoutManager(this)

                var reversedList: ArrayList<Incident>?
                var pair: Pair<Int?, ArrayList<Incident>>? = null

                if (matchViewModel.getEvent().value?.body()!!.status == EventStatusEnum.INPROGRESS) {
                    pair = Utilities().decideLastPeriod(
                        it.body()!!,
                        matchViewModel.getEvent().value?.body()?.tournament?.sport?.name!!,
                        this
                    )
                    reversedList = pair.second.reversed() as ArrayList<Incident>
                } else {
                    reversedList = it.body()!!.reversed() as ArrayList<Incident>
                }

                val sportType =
                    when (matchViewModel.getEvent().value?.body()!!.tournament.sport.name) {
                        "Football" -> SportType.FOOTBALL
                        "Basketball" -> SportType.BASKETBALL
                        else -> SportType.AMERICAN_FOOTBALL
                    }
                recyclerAdapter = MatchIncidentsAdapter(
                    this,
                    reversedList,
                    sportType,
                    matchViewModel.getEvent().value?.body()!!.status == EventStatusEnum.INPROGRESS,
                    pair?.first
                )
                binding.recyclerView.adapter = recyclerAdapter
            }
        }

        matchViewModel.getEvent().observe(this) {
            if (it.isSuccessful && it.body() != null) {

                when (it.body()?.status) {
                    EventStatusEnum.NOTSTARTED -> {
                        binding.noResultsLayout.layout.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE

                        binding.matchHeader.scoreLayout.layout.visibility = View.INVISIBLE
                        binding.matchHeader.notStartedLayout.layout.visibility = View.VISIBLE

                        val isToday = Utilities().isToday(it.body()?.startDate!!)

                        if (isToday) {
                            binding.matchHeader.notStartedLayout.date.text =
                                getString(R.string.today)
                        } else {
                            if (Preferences.getSavedDateFormat()) {
                                binding.matchHeader.notStartedLayout.date.text =
                                    Utilities().getDate(it.body()?.startDate!!)
                            } else {
                                binding.matchHeader.notStartedLayout.date.text =
                                    Utilities().getInvertedDate(it.body()?.startDate!!)
                            }
                        }

                        binding.matchHeader.notStartedLayout.hour.text =
                            Utilities().getMatchHour(it.body()?.startDate!!)
                    }

                    EventStatusEnum.FINISHED -> {
                        binding.matchHeader.scoreLayout.homeTeamScore.text =
                            it.body()?.homeScore?.total.toString()
                        binding.matchHeader.scoreLayout.awayTeamScore.text =
                            it.body()?.awayScore?.total.toString()
                        binding.matchHeader.scoreLayout.currentMinute.text =
                            getString(R.string.full_time)

                        when (it.body()?.winnerCode) {
                            WinnerCode.HOME -> {
                                Utilities().setMatchTint(
                                    this,
                                    1,
                                    binding.matchHeader.scoreLayout.homeTeamScore
                                )
                            }

                            WinnerCode.DRAW -> {
                                Utilities().setMatchTint(
                                    this,
                                    1,
                                    binding.matchHeader.scoreLayout.homeTeamScore,
                                    binding.matchHeader.scoreLayout.awayTeamScore,
                                    binding.matchHeader.scoreLayout.minus
                                )
                            }

                            else -> {
                                Utilities().setMatchTint(
                                    this,
                                    1,
                                    binding.matchHeader.scoreLayout.awayTeamScore
                                )
                            }
                        }
                    }

                    else -> {
                        binding.matchHeader.scoreLayout.homeTeamScore.text =
                            it.body()?.homeScore?.total.toString()
                        binding.matchHeader.scoreLayout.awayTeamScore.text =
                            it.body()?.awayScore?.total.toString()
                        binding.matchHeader.scoreLayout.currentMinute.text =
                            getString(R.string.in_progress)
                        
                        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
                        anim.duration = 1000

                        anim.repeatMode = Animation.REVERSE
                        anim.repeatCount = Animation.INFINITE
                        binding.matchHeader.scoreLayout.currentMinute.startAnimation(anim)

                        Utilities().setMatchTint(
                            this,
                            2,
                            binding.matchHeader.scoreLayout.homeTeamScore,
                            binding.matchHeader.scoreLayout.awayTeamScore,
                            binding.matchHeader.scoreLayout.minus,
                            binding.matchHeader.scoreLayout.currentMinute
                        )
                    }
                }

                binding.toolbar.toolbarLeagueIcon.loadImage(
                    this,
                    DataType.TOURNAMENT,
                    it.body()?.tournament?.id!!
                )

                binding.matchHeader.homeTeamLayout.teamIcon.load(
                    getString(
                        R.string.team_icon_url,
                        it.body()?.homeTeam?.id!!
                    )
                )
                binding.matchHeader.awayTeamLayout.teamIcon.load(
                    getString(
                        R.string.team_icon_url,
                        it.body()?.awayTeam?.id!!
                    )
                )

                binding.matchHeader.homeTeamLayout.teamTitle.text = it.body()?.homeTeam?.name
                binding.matchHeader.awayTeamLayout.teamTitle.text = it.body()?.awayTeam?.name

                binding.toolbar.toolbarLeagueTitle.text = getString(
                    R.string.match_toolbar_text,
                    it.body()!!.tournament.sport.name,
                    it.body()!!.tournament.country.name,
                    it.body()!!.tournament.name,
                    it.body()!!.round
                )

                Utilities().setRotatingText(binding.toolbar.toolbarLeagueTitle)

                binding.toolbar.toolbarLeagueTitle.setOnClickListener {
                    TournamentActivity.start(
                        this,
                        matchViewModel.getEvent().value?.body()?.tournament!!
                    )
                }

                binding.noResultsLayout.viewTournamentDetails.setOnClickListener { view ->
                    TournamentActivity.start(this, it.body()?.tournament!!)
                }

            }
        }

        binding.matchHeader.homeTeamLayout.root.setOnClickListener {
            TeamDetailsActivity.start(this, matchViewModel.getEvent().value?.body()?.homeTeam?.id!!)
        }

        binding.matchHeader.awayTeamLayout.root.setOnClickListener {
            TeamDetailsActivity.start(this, matchViewModel.getEvent().value?.body()?.awayTeam?.id!!)
        }

        binding.toolbar.back.setOnClickListener {
            finish()
        }

    }

    private fun getInfo() {
        if (Utilities().isNetworkAvailable(this)) {
            matchViewModel.getAllEventInfo()
        } else {
            showNoInternetDialog(this) { getInfo() }
        }
    }

    companion object {
        fun start(context: Context, teamID: Int) {
            context.startActivity(
                Intent(context, MatchDetailActivity::class.java).putExtra(
                    "matchID",
                    teamID
                )
            )
        }
    }

}
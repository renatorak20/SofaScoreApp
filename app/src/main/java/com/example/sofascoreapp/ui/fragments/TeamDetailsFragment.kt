package com.example.sofascoreapp.ui.fragments

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.bumptech.glide.util.Util
import com.example.sofascoreapp.MatchDetailActivity
import com.example.sofascoreapp.R
import com.example.sofascoreapp.TournamentActivity
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.data.model.EventStatusEnum
import com.example.sofascoreapp.databinding.FragmentTeamDetailsBinding
import com.example.sofascoreapp.databinding.TeamTournementItemBinding
import com.example.sofascoreapp.databinding.TournamentListItemBinding
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.utils.Utilities.Companion.loadImage
import com.example.sofascoreapp.utils.Utilities.Companion.showNoInternetDialog
import com.example.sofascoreapp.viewmodel.TeamDetailsViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlin.random.Random

class TeamDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTeamDetailsBinding
    private lateinit var teamDetailsViewModel: TeamDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTeamDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamDetailsViewModel =
            ViewModelProvider(requireActivity())[TeamDetailsViewModel::class.java]

        teamDetailsViewModel.getTeamID().observe(viewLifecycleOwner) {
            getInfo()
        }


        teamDetailsViewModel.getTeamDetails().observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                binding.coach.name.text =
                    getString(R.string.coachName, response.body()?.managerName)
                binding.stadiumText.text = response.body()?.venue
                Utilities.loadCountryImage(
                    response.body()?.country?.name!!,
                    binding.coach.countryImage
                )
            }
        }

        teamDetailsViewModel.getTeamPlayers().observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                binding.stats.totalPlayersNumber.text = response.body()?.size.toString()

                var foreignPlayers = Utilities().calculateForignPlayers(
                    response.body()?.map { player -> player.country?.name!! }!!,
                    teamDetailsViewModel.getTeamDetails().value?.body()!!.country.name
                ).toString()

                binding.stats.foreignPlayerNumber.text = foreignPlayers
                animateIndicator(
                    binding.stats.statIndicator,
                    ((foreignPlayers.toFloat() / response.body()?.size?.toFloat()!!) * 100).toInt()
                )
            }
        }

        teamDetailsViewModel.getTeamTournaments().observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                if (binding.tournamentGrid.isEmpty()) {
                    for (tournament in response.body()!!) {

                        val itemLayout = TeamTournementItemBinding.inflate(layoutInflater)
                        itemLayout.tournamentName.text = tournament.name
                        itemLayout.tournamentIcon.loadImage(
                            requireContext(),
                            DataType.TOURNAMENT,
                            tournament.id
                        )

                        binding.tournamentGrid.addView(itemLayout.root.rootView)

                        itemLayout.root.setOnClickListener {
                            TournamentActivity.start(requireContext(), tournament)
                        }

                    }
                }
            }
        }


        teamDetailsViewModel.getNextMatch().observe(requireActivity()) { response ->
            if (response.isSuccessful) {

                with(binding) {

                    if (response.body()!![0].status == EventStatusEnum.INPROGRESS) {
                        nextMatch.timeLayout.currentMinute.text =
                            requireContext().getString(R.string.minute, Random(1000).nextInt(1, 30))
                        Utilities().setMatchTint(
                            requireContext(),
                            2,
                            nextMatch.timeLayout.currentMinute,
                            nextMatch.homeScore,
                            nextMatch.awayScore
                        )

                        nextMatch.homeScore.text = response.body()!![0].homeScore.total.toString()
                        nextMatch.awayScore.text = response.body()!![0].awayScore.total.toString()
                    } else {
                        binding.nextMatch.timeLayout.currentMinute.text = "-"
                    }
                }

                binding.nextMatch.homeTeamLayout.teamName.text = response.body()!![0].homeTeam.name
                binding.nextMatch.awayTeamLayout.teamName.text = response.body()!![0].awayTeam.name

                if (Utilities().isTomorrow(response.body()!![0].startDate!!)) {
                    binding.nextMatch.timeLayout.timeOfMatch.text = getString(R.string.tomorrow)
                    binding.nextMatch.timeLayout.currentMinute.text =
                        Utilities().getMatchHour(response.body()!![0].startDate!!)

                } else if (Utilities().isToday(response.body()!![0].startDate!!)) {
                    binding.nextMatch.timeLayout.timeOfMatch.text =
                        Utilities().getMatchHour(response.body()!![0].startDate!!)
                } else {
                    if (Preferences.getSavedDateFormat()) {
                        binding.nextMatch.timeLayout.timeOfMatch.text =
                            Utilities().getDate(response.body()!![0].startDate!!)
                        binding.nextMatch.timeLayout.currentMinute.text =
                            Utilities().getMatchHour(response.body()!![0].startDate!!)
                    } else {
                        binding.nextMatch.timeLayout.timeOfMatch.text =
                            Utilities().getInvertedDate(response.body()!![0].startDate!!)
                        binding.nextMatch.timeLayout.currentMinute.text =
                            Utilities().getMatchHour(response.body()!![0].startDate!!)
                    }
                }

                binding.nextMatch.homeTeamLayout.clubIcon.loadImage(
                    requireContext(),
                    DataType.TEAM,
                    response.body()!![0].homeTeam.id
                )
                binding.nextMatch.awayTeamLayout.clubIcon.loadImage(
                    requireContext(),
                    DataType.TEAM,
                    response.body()!![0].awayTeam.id
                )

                binding.nextMatchHeader.league.text = response.body()!![0].tournament.name
                binding.nextMatchHeader.country.text = response.body()!![0].tournament.country.name
                binding.nextMatchHeader.leagueIcon.loadImage(
                    requireContext(),
                    DataType.TOURNAMENT,
                    response.body()!![0].tournament.id
                )
                binding.nextMatchHeader.layout.setOnClickListener {
                    TournamentActivity.start(requireContext(), response.body()!![0].tournament)
                }

            }
        }

        binding.nextMatch.layout.setOnClickListener {
            MatchDetailActivity.start(
                requireContext(),
                teamDetailsViewModel.getNextMatch().value?.body()!![0].id
            )
        }


    }

    private fun getInfo() {
        if (Utilities().isNetworkAvailable(requireContext())) {
            teamDetailsViewModel.getLatestTeamDetails()
        } else {
            showNoInternetDialog(requireContext()) { getInfo() }
        }
    }

    private fun animateIndicator(indicator: CircularProgressIndicator, value: Int) {
        val duration = 500
        val animator = ObjectAnimator.ofInt(indicator, "progress", value)
        animator.duration = duration.toLong()
        animator.start()
    }

}
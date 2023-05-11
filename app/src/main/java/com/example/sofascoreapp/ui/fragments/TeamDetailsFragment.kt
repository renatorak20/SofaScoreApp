package com.example.sofascoreapp.ui.fragments

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
import com.example.sofascoreapp.databinding.FragmentTeamDetailsBinding
import com.example.sofascoreapp.databinding.TeamTournementItemBinding
import com.example.sofascoreapp.databinding.TournamentListItemBinding
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.TeamDetailsViewModel

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
            teamDetailsViewModel.getLatestTeamDetails()
        }


        teamDetailsViewModel.getTeamDetails().observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                binding.coach.name.text =
                    getString(R.string.coachName, response.body()?.managerName)
                binding.stadiumText.text = response.body()?.venue
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
                binding.stats.statIndicator.progress =
                    ((foreignPlayers.toFloat() / response.body()?.size?.toFloat()!!) * 100).toInt()
            }
        }

        teamDetailsViewModel.getTeamTournaments().observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                if (binding.tournamentGrid.isEmpty()) {
                    for (tournament in response.body()!!) {

                        val itemLayout = TeamTournementItemBinding.inflate(layoutInflater)
                        itemLayout.tournamentName.text = tournament.name
                        itemLayout.tournamentIcon.load(
                            getString(
                                R.string.tournament_icon_url,
                                tournament.id
                            )
                        )

                        binding.tournamentGrid.addView(itemLayout.root.rootView)

                        binding.tournamentGrid.setOnClickListener {
                            context?.startActivity(
                                Intent(
                                    context,
                                    TournamentActivity::class.java
                                ).putExtra("tournamentID", tournament.id)
                            )
                        }

                    }
                }
            }
        }

        teamDetailsViewModel.getTeamEvents().observe(requireActivity()) { response ->
            if (response.isSuccessful) {

                binding.nextMatch.homeTeamLayout.teamName.text = response.body()!![0].homeTeam.name
                binding.nextMatch.awayTeamLayout.teamName.text = response.body()!![0].awayTeam.name

                if (Utilities().isTomorrow(response.body()!![0].startDate!!)) {
                    binding.nextMatch.timeLayout.timeOfMatch.text = getString(R.string.tomorrow)
                    binding.nextMatch.timeLayout.currentMinute.text =
                        Utilities().getMatchHour(response.body()!![0].startDate!!)

                } else if (Utilities().isToday(response.body()!![0].startDate!!)) {
                    binding.nextMatch.timeLayout.timeOfMatch.text =
                        Utilities().getMatchHour(response.body()!![0].startDate!!)
                    binding.nextMatch.timeLayout.currentMinute.text = "-"
                } else {
                    binding.nextMatch.timeLayout.timeOfMatch.text =
                        Utilities().getAvailableDateShort(response.body()!![0].startDate!!)
                    binding.nextMatch.timeLayout.currentMinute.text =
                        Utilities().getMatchHour(response.body()!![0].startDate!!)
                }

                binding.nextMatch.homeTeamLayout.clubIcon.load(
                    getString(
                        R.string.team_icon_url,
                        response.body()!![0].homeTeam.id
                    )
                )
                binding.nextMatch.awayTeamLayout.clubIcon.load(
                    getString(
                        R.string.team_icon_url,
                        response.body()!![0].awayTeam.id
                    )
                )

                binding.nextMatchHeader.league.text = response.body()!![0].tournament.name
                binding.nextMatchHeader.country.text = response.body()!![0].tournament.country.name
                binding.nextMatchHeader.leagueIcon.load(
                    getString(
                        R.string.tournament_icon_url,
                        response.body()!![0].tournament.id
                    )
                )


            }
        }

        binding.nextMatch.layout.setOnClickListener {
            val intent = Intent(requireContext(), MatchDetailActivity::class.java)
            intent.putExtra("matchID", teamDetailsViewModel.getTeamEvents().value?.body()!![0].id)
            startActivity(intent)
        }


    }
}
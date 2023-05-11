package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.sofascoreapp.R
import com.example.sofascoreapp.databinding.FragmentTeamStandingsBinding
import com.example.sofascoreapp.ui.adapters.StandingsAdapter
import com.example.sofascoreapp.viewmodel.TeamDetailsViewModel


class TeamStandingsFragment : Fragment() {

    private lateinit var binding: FragmentTeamStandingsBinding
    private lateinit var viewModel: TeamDetailsViewModel
    private lateinit var recyclerAdapter: StandingsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTeamStandingsBinding.inflate(layoutInflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TeamDetailsViewModel::class.java]

        viewModel.getTeamTournamentStandings()

        viewModel.getTeamTournaments().observe(viewLifecycleOwner) {
            val arrayAdapter = ArrayAdapter(
                requireContext(),
                R.layout.tournament_spinner_item,
                it.body()!!.map { tournament -> tournament.name })
            binding.teamTournamentHeader.spinner.adapter = arrayAdapter

        }


        binding.teamTournamentHeader.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    if (!viewModel.getSport().value.isNullOrEmpty() && viewModel.getTeamTournaments().value != null && !viewModel.getStandings().value.isNullOrEmpty()) {
                        populateRecyclerView(position)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        viewModel.getStandings().observe(viewLifecycleOwner) {
            populateRecyclerView(0)
        }
    }

    fun populateRecyclerView(tournamentIndex: Int) {

        binding.teamTournamentHeader.tournamentIcon.load(
            getString(
                R.string.tournament_icon_url,
                viewModel.getTeamTournaments().value?.body()!![tournamentIndex].id
            )
        )

        recyclerAdapter = when (viewModel.getSport().value) {
            getString(R.string.football) -> StandingsAdapter(
                requireContext(),
                viewModel.getStandings().value!![tournamentIndex].sortedStandingsRows,
                0,
                viewModel.getTeamID().value!!
            )

            getString(R.string.basketball) -> StandingsAdapter(
                requireContext(),
                viewModel.getStandings().value!![tournamentIndex].sortedStandingsRows,
                1,
                viewModel.getTeamID().value!!
            )

            else -> StandingsAdapter(
                requireContext(),
                viewModel.getStandings().value!![tournamentIndex].sortedStandingsRows,
                2,
                viewModel.getTeamID().value!!
            )
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = recyclerAdapter
    }

}
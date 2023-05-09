package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.databinding.FragmentTeamStandingsBinding
import com.example.sofascoreapp.ui.adapters.StandingsAdapter
import com.example.sofascoreapp.ui.adapters.TeamMembersRecyclerAdapter
import com.example.sofascoreapp.utils.Utilities
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

        viewModel.getStandings().observe(viewLifecycleOwner) {
            val sportType = it[0].tournament.sport.name
            recyclerAdapter = when (sportType) {
                "Football" -> StandingsAdapter(
                    requireContext(),
                    viewModel.getStandings().value!![0].sortedStandingsRows,
                    0
                )

                "Basketball" -> StandingsAdapter(
                    requireContext(),
                    viewModel.getStandings().value!![0].sortedStandingsRows,
                    1
                )

                else -> StandingsAdapter(
                    requireContext(),
                    viewModel.getStandings().value!![0].sortedStandingsRows,
                    2
                )
            }
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.adapter = recyclerAdapter
        }
    }

}
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
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.databinding.FragmentTeamStandingsBinding
import com.example.sofascoreapp.ui.adapters.StandingsAdapter
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.utils.Utilities.Companion.loadImage
import com.example.sofascoreapp.utils.Utilities.Companion.showNoInternetDialog
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

        getInfo()

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

        binding.teamTournamentHeader.tournamentIcon.loadImage(
            requireContext(),
            DataType.TOURNAMENT,
            viewModel.getTeamTournaments().value?.body()!![tournamentIndex].id
        )

        when (viewModel.getSport().value) {
            "Football" -> {
                recyclerAdapter = StandingsAdapter(
                    requireContext(),
                    viewModel.getStandings().value!![tournamentIndex].sortedStandingsRows,
                    "Football",
                    viewModel.getTeamID().value!!
                )
                binding.standings.layout.visibility = View.VISIBLE
            }

            "Basketball" -> {
                recyclerAdapter = StandingsAdapter(
                    requireContext(),
                    viewModel.getStandings().value!![tournamentIndex].sortedStandingsRows,
                    "Basketball",
                    viewModel.getTeamID().value!!
                )
                binding.standingsBasketball.layout.visibility = View.VISIBLE
            }

            else -> {
                recyclerAdapter = StandingsAdapter(
                    requireContext(),
                    viewModel.getStandings().value!![tournamentIndex].sortedStandingsRows,
                    "Amer. Football",
                    viewModel.getTeamID().value!!
                )
                binding.standingsAmericanFootball.layout.visibility = View.VISIBLE
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = recyclerAdapter
    }

    fun getInfo() {
        if (Utilities().isNetworkAvailable(requireContext())) {
            viewModel.getTeamTournamentStandings()
        } else {
            showNoInternetDialog(requireContext()) { getInfo() }
        }
    }

}
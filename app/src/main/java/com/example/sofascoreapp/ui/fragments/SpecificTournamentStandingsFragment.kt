package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.sofascoreapp.R
import com.example.sofascoreapp.databinding.FragmentTournamentStandingsBinding
import com.example.sofascoreapp.ui.adapters.StandingsAdapter
import com.example.sofascoreapp.viewmodel.SpecificTournamentViewModel

class SpecificTournamentStandingsFragment : Fragment() {

    private lateinit var binding: FragmentTournamentStandingsBinding
    private lateinit var recyclerAdapter: StandingsAdapter
    private lateinit var viewModel: SpecificTournamentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTournamentStandingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SpecificTournamentViewModel::class.java]

        viewModel.getTournamentStandings().observe(requireActivity()) {
            populateRecyclerView()
        }
    }

    private fun populateRecyclerView() {

        when (viewModel.getSport().value) {
            "Football" -> {
                binding.standings.layout.visibility = View.VISIBLE
            }

            "Basketball" -> {
                binding.standingsBasketball.layout.visibility = View.VISIBLE
            }

            else -> {
                binding.standingsAmericanFootball.layout.visibility = View.VISIBLE
            }
        }

        recyclerAdapter = StandingsAdapter(
            requireContext(),
            viewModel.getTournamentStandings().value?.sortedStandingsRows!!,
            viewModel.getSport().value!!,
            -1
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = recyclerAdapter
    }

}
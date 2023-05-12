package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.databinding.FragmentTournamentMatchesBinding
import com.example.sofascoreapp.ui.adapters.EventsPagingAdapter
import com.example.sofascoreapp.ui.adapters.LoadStateHeaderFooterAdapter
import com.example.sofascoreapp.viewmodel.SpecificTournamentViewModel
import kotlinx.coroutines.launch

class SpecificTournamentMatchesFragment : Fragment() {

    private lateinit var binding: FragmentTournamentMatchesBinding
    private lateinit var recyclerAdapter: EventsPagingAdapter
    private lateinit var viewModel: SpecificTournamentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTournamentMatchesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SpecificTournamentViewModel::class.java]

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerAdapter = EventsPagingAdapter(requireActivity(), requireContext(), 1)
        binding.recyclerView.adapter = recyclerAdapter.withLoadStateHeaderAndFooter(
            LoadStateHeaderFooterAdapter(), LoadStateHeaderFooterAdapter()
        )
        binding.recyclerView.setHasFixedSize(true)

        viewModel.tournamentEvents.observe(requireActivity()) { pagingData ->
            lifecycleScope.launch {
                recyclerAdapter.submitData(pagingData)
            }
        }

    }

}
package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.databinding.FragmentTeamMatchesBinding
import com.example.sofascoreapp.ui.adapters.EventsPagingAdapter
import com.example.sofascoreapp.ui.adapters.LoadStateHeaderFooterAdapter
import com.example.sofascoreapp.viewmodel.TeamDetailsViewModel
import kotlinx.coroutines.launch

class TeamMatchesFragment : Fragment() {

    private lateinit var binding: FragmentTeamMatchesBinding
    private lateinit var teamDetailsViewModel: TeamDetailsViewModel
    private lateinit var recyclerAdapter: EventsPagingAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamMatchesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamDetailsViewModel =
            ViewModelProvider(requireActivity())[TeamDetailsViewModel::class.java]

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerAdapter = EventsPagingAdapter(requireContext(), 0)
        binding.recyclerView.adapter = recyclerAdapter.withLoadStateHeaderAndFooter(
            LoadStateHeaderFooterAdapter(),
            LoadStateHeaderFooterAdapter()
        )
        binding.recyclerView.setHasFixedSize(true)

        teamDetailsViewModel.teamEvents.observe(requireActivity()) { pagingData ->
            lifecycleScope.launch {
                recyclerAdapter.submitData(pagingData)
            }
        }

    }
}
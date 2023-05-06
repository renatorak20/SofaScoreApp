package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.R
import com.example.sofascoreapp.databinding.FragmentAmericanFootballTournamentsBinding
import com.example.sofascoreapp.databinding.FragmentFootballTournamentsBinding
import com.example.sofascoreapp.ui.adapters.TournamentsAdapter
import com.example.sofascoreapp.viewmodel.TournamentsViewModel

class AmericanFootballTournamentsFragment : Fragment() {

    private lateinit var binding: FragmentAmericanFootballTournamentsBinding
    private lateinit var tournamentsViewModel: TournamentsViewModel
    private lateinit var recyclerAdapter: TournamentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAmericanFootballTournamentsBinding.inflate(layoutInflater)
        tournamentsViewModel = ViewModelProvider(this)[TournamentsViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tournamentsViewModel.getAFootballTournaments()

        tournamentsViewModel.aFootballTournaments().observe(viewLifecycleOwner) {
            if (it.body()?.isNotEmpty() == true) {
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerAdapter = TournamentsAdapter(requireContext(), it.body()!!)
                binding.recyclerView.adapter = recyclerAdapter
            }
        }

    }

}
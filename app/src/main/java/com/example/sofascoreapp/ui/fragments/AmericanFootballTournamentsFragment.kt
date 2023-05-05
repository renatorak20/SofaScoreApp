package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.sofascoreapp.R
import com.example.sofascoreapp.databinding.FragmentAmericanFootballTournamentsBinding
import com.example.sofascoreapp.databinding.FragmentFootballTournamentsBinding
import com.example.sofascoreapp.viewmodel.TournamentsViewModel

class AmericanFootballTournamentsFragment : Fragment() {

    private lateinit var binding: FragmentAmericanFootballTournamentsBinding
    private lateinit var tournamentsViewModel: TournamentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAmericanFootballTournamentsBinding.inflate(layoutInflater)
        tournamentsViewModel = ViewModelProvider(this)[TournamentsViewModel::class.java]

        return binding.root
    }

}
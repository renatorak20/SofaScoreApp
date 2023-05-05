package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.sofascoreapp.R
import com.example.sofascoreapp.databinding.FragmentBasketballTournamentsBinding
import com.example.sofascoreapp.viewmodel.TournamentsViewModel

class BasketballTournamentsFragment : Fragment() {

    private lateinit var binding: FragmentBasketballTournamentsBinding
    private lateinit var tournamentsViewModel: TournamentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBasketballTournamentsBinding.inflate(layoutInflater)
        tournamentsViewModel = ViewModelProvider(this)[TournamentsViewModel::class.java]

        return binding.root
    }

}
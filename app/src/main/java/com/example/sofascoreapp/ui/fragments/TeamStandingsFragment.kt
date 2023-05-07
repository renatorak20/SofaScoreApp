package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sofascoreapp.R
import com.example.sofascoreapp.databinding.FragmentTeamStandingsBinding


class TeamStandingsFragment : Fragment() {

    private lateinit var binding: FragmentTeamStandingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamStandingsBinding.inflate(layoutInflater)

        return binding.root
    }

}
package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sofascoreapp.databinding.FragmentTeamMatchesBinding

class TeamMatchesFragment : Fragment() {

    private lateinit var binding: FragmentTeamMatchesBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamMatchesBinding.inflate(inflater, container, false)

        return binding.root
    }

}
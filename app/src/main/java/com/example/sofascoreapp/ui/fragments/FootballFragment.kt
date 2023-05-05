package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.R
import com.example.sofascoreapp.databinding.FragmentFootballBinding
import com.example.sofascoreapp.ui.adapters.FootballMainAdapter
import com.example.sofascoreapp.viewmodel.SharedViewModel

class FootballFragment : Fragment() {

    private lateinit var binding: FragmentFootballBinding
    private lateinit var recyclerAdapter: FootballMainAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFootballBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        return binding.root
   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.getFootballEvents("football", "2023-05-06")

        sharedViewModel.footballEvents().observe(viewLifecycleOwner) {
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerAdapter = FootballMainAdapter(requireContext(), it.body()!!)
            binding.recyclerView.adapter = recyclerAdapter
        }

    }
}
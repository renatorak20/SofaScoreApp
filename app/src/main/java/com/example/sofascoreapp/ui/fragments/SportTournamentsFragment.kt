package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.data.model.SportType
import com.example.sofascoreapp.databinding.FragmentSportTournamentsBinding
import com.example.sofascoreapp.ui.adapters.TournamentsAdapter
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.utils.Utilities.Companion.showNoInternetDialog
import com.example.sofascoreapp.viewmodel.TournamentsViewModel

abstract class SportTournamentsFragment : Fragment() {

    private lateinit var binding: FragmentSportTournamentsBinding
    private lateinit var recyclerAdapter: TournamentsAdapter
    private lateinit var tournamentsViewModel: TournamentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSportTournamentsBinding.inflate(layoutInflater)
        tournamentsViewModel = ViewModelProvider(this)[TournamentsViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getInfo()
        binding.indicator.show()


        tournamentsViewModel.getTournaments().observe(viewLifecycleOwner) {
            if (it.isSuccessful && !it.body().isNullOrEmpty()) {
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerAdapter = TournamentsAdapter(requireContext(), it.body()!!)
                binding.recyclerView.adapter = recyclerAdapter
                binding.indicator.hide()
                binding.recyclerView.visibility = View.VISIBLE
            }
        }

    }


    protected abstract fun getSportType(): SportType

    private fun getInfo() {
        if (Utilities().isNetworkAvailable(requireContext())) {
            tournamentsViewModel.getTournaments(getSportType())
        } else {
            showNoInternetDialog(requireContext()) { getInfo() }
        }
    }

}
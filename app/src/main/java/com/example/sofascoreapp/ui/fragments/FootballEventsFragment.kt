package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.SportType
import com.example.sofascoreapp.databinding.FragmentFootballEventsBinding
import com.example.sofascoreapp.ui.adapters.DatesAdapter
import com.example.sofascoreapp.ui.adapters.EventsRecyclerAdapter
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.SharedViewModel
import java.time.LocalDate

class FootballEventsFragment : Fragment() {

    private lateinit var binding: FragmentFootballEventsBinding
    private lateinit var recyclerAdapter: EventsRecyclerAdapter
    private lateinit var datesAdapter: DatesAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFootballEventsBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.initializeAvailableDays(30)

        sharedViewModel.getFootballAvailableDays().observe(viewLifecycleOwner) {
            binding.daysRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            datesAdapter = DatesAdapter(requireContext(), it, sharedViewModel, SportType.FOOTBALL)
            binding.daysRecyclerView.adapter = datesAdapter
            binding.daysRecyclerView.scrollToPosition(it.indexOf(it.find { it.isSelected }) - 2)
        }


        sharedViewModel.setFootballDate(Utilities().getTodaysDate())

        sharedViewModel.footballDate.observe(viewLifecycleOwner) {
            sharedViewModel.getNewestEvents("football", it)
        }


        sharedViewModel.footballEvents().observe(viewLifecycleOwner) {
            if (it.body()?.isNotEmpty() == true) {
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                val groupedMatches =
                    it.body()!!.groupBy { it.tournament }.flatMap { listOf(it.key) + it.value }
                recyclerAdapter =
                    EventsRecyclerAdapter(requireContext(), groupedMatches as ArrayList<Any>)
                binding.recyclerView.adapter = recyclerAdapter

            } else {
                binding.recyclerView.adapter = null
            }

            if (sharedViewModel.footballDate.value == LocalDate.now().toString()) {
                binding.info.date.text = getString(R.string.today)
            } else {
                binding.info.date.text =
                    Utilities().getLongDate(sharedViewModel.footballDate.value!!)
            }

            if (it.body()?.isNotEmpty() == true) {
                binding.info.noOfEvents.text = getString(R.string.numberOfEvents, it.body()?.size)
            } else {
                binding.info.noOfEvents.text = getString(R.string.noEventsToday)
            }

        }
    }

}
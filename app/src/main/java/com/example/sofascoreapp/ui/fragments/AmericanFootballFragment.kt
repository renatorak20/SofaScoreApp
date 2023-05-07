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
import com.example.sofascoreapp.databinding.FragmentAmericanFootballEventsBinding
import com.example.sofascoreapp.ui.adapters.DatesAdapter
import com.example.sofascoreapp.ui.adapters.EventsRecyclerAdapter
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.MainViewModel
import java.time.LocalDate

class AmericanFootballFragment : Fragment() {

    private lateinit var binding: FragmentAmericanFootballEventsBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var datesAdapter: DatesAdapter
    private lateinit var recyclerAdapter: EventsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAmericanFootballEventsBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.initializeAvailableDays(30)

        mainViewModel.getAFootballAvailableDays().observe(viewLifecycleOwner) {
            binding.daysRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            datesAdapter =
                DatesAdapter(requireContext(), it, mainViewModel, SportType.AMERICAN_FOOTBALL)
            binding.daysRecyclerView.adapter = datesAdapter
            binding.daysRecyclerView.scrollToPosition(it.indexOf(it.find { it.isSelected }) - 2)
        }


        mainViewModel.setAFootballDate(Utilities().getTodaysDate())


        mainViewModel.amerFootballDate.observe(viewLifecycleOwner) {
            mainViewModel.getNewestEvents("american-football", it)
        }

        mainViewModel.basketballEvents().observe(viewLifecycleOwner) {
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

            if (mainViewModel.amerFootballDate.value == LocalDate.now().toString()) {
                binding.info.date.text = getString(R.string.today)
            } else {
                binding.info.date.text =
                    Utilities().getLongDate(mainViewModel.amerFootballDate.value!!)
            }

            if (it.body()?.isNotEmpty() == true) {
                binding.info.noOfEvents.text = getString(R.string.numberOfEvents, it.body()?.size)
            } else {
                binding.info.noOfEvents.text = getString(R.string.noEventsToday)
            }
        }

    }


}
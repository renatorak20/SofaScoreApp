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
import com.example.sofascoreapp.databinding.FragmentBasketballEventsBinding
import com.example.sofascoreapp.ui.adapters.DatesAdapter
import com.example.sofascoreapp.ui.adapters.EventsRecyclerAdapter
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.SharedViewModel
import java.time.LocalDate

class BasketballEventsFragment : Fragment() {

    private lateinit var binding: FragmentBasketballEventsBinding
    private lateinit var recyclerAdapter: EventsRecyclerAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var datesAdapter: DatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBasketballEventsBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.initializeAvailableDays()

        sharedViewModel.getBasketballAvailableDays().observe(viewLifecycleOwner) {
            binding.daysRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            datesAdapter = DatesAdapter(requireContext(), it, sharedViewModel, SportType.BASKETBALL)
            binding.daysRecyclerView.adapter = datesAdapter
            binding.daysRecyclerView.scrollToPosition(it.indexOf(it.find { it.isSelected }) - 2)
        }

        sharedViewModel.setBasketballDate(Utilities().getTodaysDate())

        sharedViewModel.basketballDate.observe(viewLifecycleOwner) {
            sharedViewModel.getNewestEvents("basketball", it)
        }

        sharedViewModel.basketballEvents().observe(viewLifecycleOwner) {
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

            if (sharedViewModel.basketballDate.value == LocalDate.now().toString()) {
                binding.info.date.text = getString(R.string.today)
            } else {
                binding.info.date.text =
                    Utilities().getLongDate(sharedViewModel.basketballDate.value!!)
            }

            if (it.body()?.isNotEmpty() == true) {
                binding.info.noOfEvents.text = getString(R.string.numberOfEvents, it.body()?.size)
            } else {
                binding.info.noOfEvents.text = getString(R.string.noEventsToday)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.basketballEvents().removeObservers(viewLifecycleOwner)
    }


}
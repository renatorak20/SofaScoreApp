package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.SportType
import com.example.sofascoreapp.databinding.FragmentSportEventsBinding
import com.example.sofascoreapp.ui.adapters.DatesAdapter
import com.example.sofascoreapp.ui.adapters.EventsRecyclerAdapter
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.MainViewModel
import java.time.LocalDate

abstract class SportEventsFragment : Fragment() {

    private lateinit var binding: FragmentSportEventsBinding
    private lateinit var recyclerAdapter: EventsRecyclerAdapter
    protected lateinit var mainViewModel: MainViewModel
    private lateinit var datesAdapter: DatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSportEventsBinding.inflate(layoutInflater)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.indicator.show()

        mainViewModel.initializeAvailableDays(30)

        mainViewModel.getAvailableDays().observe(viewLifecycleOwner) {
            binding.daysRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            datesAdapter =
                DatesAdapter(requireActivity(), requireContext(), it, mainViewModel, getSportType())
            binding.daysRecyclerView.adapter = datesAdapter
            binding.daysRecyclerView.scrollToPosition(it.indexOf(it.find { it.isSelected }) - 2)
        }

        setInitialDate()

        mainViewModel.getDate().observe(viewLifecycleOwner) {

            binding.recyclerView.visibility = View.GONE
            binding.indicator.visibility = View.VISIBLE

            mainViewModel.getNewestEvents(getSportType(), it)
        }

        mainViewModel.getEvents().observe(viewLifecycleOwner) {

            binding.recyclerView.visibility = View.VISIBLE
            binding.indicator.visibility = View.GONE

            if (it.body()?.isNotEmpty() == true) {
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                val groupedMatches =
                    it.body()!!.groupBy { it.tournament }.flatMap { listOf(it.key) + it.value }
                recyclerAdapter =
                    EventsRecyclerAdapter(
                        requireActivity(),
                        requireContext(),
                        groupedMatches as ArrayList<Any>
                    )
                binding.recyclerView.adapter = recyclerAdapter
            } else {
                binding.recyclerView.adapter = null
            }

            if (mainViewModel.getDate().value == LocalDate.now().toString()) {
                binding.info.date.text = getString(R.string.today)
            } else {
                if (Preferences(requireActivity()).getSavedDateFormat()) {
                    binding.info.date.text =
                        Utilities().getLongDate(mainViewModel.getDate().value!!)
                } else {
                    binding.info.date.text =
                        Utilities().getInvertedLongDate(mainViewModel.getDate().value!!)
                }
            }

            if (it.body()?.isNotEmpty() == true) {
                binding.info.noOfEvents.text = getString(R.string.numberOfEvents, it.body()?.size)
            } else {
                binding.info.noOfEvents.text = getString(R.string.noEventsToday)
                Utilities().setRotatingText(binding.info.noOfEvents)
            }
        }
    }

    protected abstract fun getSportType(): SportType

    fun setInitialDate() {
        mainViewModel.setDate(Utilities().getTodaysDate())
    }
}

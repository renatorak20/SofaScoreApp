package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.databinding.FragmentAmericanFootballEventsBinding
import com.example.sofascoreapp.ui.adapters.EventsRecyclerAdapter
import com.example.sofascoreapp.viewmodel.SharedViewModel

class AmericanFootballFragment : Fragment() {

    private lateinit var binding: FragmentAmericanFootballEventsBinding
    private lateinit var recyclerAdapter: EventsRecyclerAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAmericanFootballEventsBinding.inflate(layoutInflater)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.getNewestEvents("american-football", "2023-05-06")

        /*        sharedViewModel.basketballEvents().observe(viewLifecycleOwner) {
                    if (it.body()?.isNotEmpty() == true) {
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        recyclerAdapter =
                            it.body()?.let { it1 ->
                                EventsRecyclerAdapter(
                                    requireContext(),
                                    it1 as ArrayList<Event>
                                )
                            }!!
                        binding.recyclerView.adapter = recyclerAdapter
                    }
                }*/

    }


}
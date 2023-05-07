package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.Country
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.data.model.Sport
import com.example.sofascoreapp.data.model.Team2
import com.example.sofascoreapp.databinding.FragmentTeamSquadBinding
import com.example.sofascoreapp.databinding.TeamTournementItemBinding
import com.example.sofascoreapp.ui.adapters.EventsRecyclerAdapter
import com.example.sofascoreapp.ui.adapters.TeamMembersRecyclerAdapter
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.TeamDetailsViewModel

class TeamSquadFragment : Fragment() {

    private lateinit var binding: FragmentTeamSquadBinding
    private lateinit var teamDetailsViewModel: TeamDetailsViewModel
    private lateinit var recyclerAdapter: TeamMembersRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTeamSquadBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamDetailsViewModel =
            ViewModelProvider(requireActivity())[TeamDetailsViewModel::class.java]


        teamDetailsViewModel.getTeamPlayers().observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerAdapter =
                    TeamMembersRecyclerAdapter(
                        requireContext(),
                        (Utilities().makeTeamMembersList(
                            Player(
                                -1,
                                teamDetailsViewModel.getTeamDetails().value?.body()!!.managerName,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                            ), response.body()!!
                        ))
                    )
                binding.recyclerView.adapter = recyclerAdapter
            }
        }


    }

}
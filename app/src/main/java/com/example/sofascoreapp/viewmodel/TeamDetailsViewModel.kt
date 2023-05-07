package com.example.sofascoreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.data.model.TeamDetails
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.networking.Network
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class TeamDetailsViewModel : ViewModel() {

    private val _teamID = MutableLiveData<Int>()

    fun setTeamID(id: Int) {
        _teamID.value = id
    }

    fun getTeamID(): MutableLiveData<Int> {
        return _teamID
    }

    private val _teamDetails = MutableLiveData<Response<TeamDetails>>()

    fun setTeamDetails(details: Response<TeamDetails>) {
        _teamDetails.value = details
    }

    fun getTeamDetails(): MutableLiveData<Response<TeamDetails>> {
        return _teamDetails
    }

    private val _teamPlayers = MutableLiveData<Response<ArrayList<Player>>>()

    fun setTeamPlayers(players: Response<ArrayList<Player>>) {
        _teamPlayers.value = players
    }

    fun getTeamPlayers(): MutableLiveData<Response<ArrayList<Player>>> {
        return _teamPlayers
    }

    private val _teamTournaments = MutableLiveData<Response<ArrayList<Tournament>>>()

    fun setTeamTournaments(tournaments: Response<ArrayList<Tournament>>) {
        _teamTournaments.value = tournaments
    }

    fun getTeamTournaments(): MutableLiveData<Response<ArrayList<Tournament>>> {
        return _teamTournaments
    }

    private val _teamEvents = MutableLiveData<Response<ArrayList<Event>>>()

    fun setTeamEvents(events: Response<ArrayList<Event>>) {
        _teamEvents.value = events
    }

    fun getTeamEvents(): MutableLiveData<Response<ArrayList<Event>>> {
        return _teamEvents
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getLatestTeamDetails() {
        viewModelScope.launch {
            val teamDetails = async {
                Network().getService().getTeamDetails(_teamID.value!!)
            }
            val teamPlayers = async {
                Network().getService().getTeamPlayers(_teamID.value!!)
            }
            val teamTournaments = async {
                Network().getService().getTeamTournaments(_teamID.value!!)
            }
            val teamEvents = async {
                Network().getService().getTeamEventsPage(_teamID.value!!, "next", 0)
            }

            teamDetails.await()
            teamPlayers.await()
            teamTournaments.await()
            teamEvents.await()

            setTeamDetails(teamDetails.getCompleted())
            setTeamPlayers(teamPlayers.getCompleted())
            setTeamTournaments(teamTournaments.getCompleted())
            setTeamEvents(teamEvents.getCompleted())

        }
    }


}
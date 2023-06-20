package com.example.sofascoreapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.liveData
import androidx.paging.map
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.Favourite
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.data.model.Standing
import com.example.sofascoreapp.data.model.TeamDetails
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.model.UiModel
import com.example.sofascoreapp.data.networking.Network
import com.example.sofascoreapp.database.SofascoreApiDatabase
import com.example.sofascoreapp.ui.paging.TeamEventsPagingSource
import com.example.sofascoreapp.utils.Utilities
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _sport = MutableLiveData<String>()

    fun setSport(sport: String) {
        _sport.value = sport
    }

    fun getSport(): MutableLiveData<String> {
        return _sport
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

    val teamEvents = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        0,
        pagingSourceFactory = { TeamEventsPagingSource(_teamID.value!!) }
    ).liveData.cachedIn(viewModelScope).map { value: PagingData<Event> ->
        value.map { event ->
            UiModel.Event(event)
        }
            .insertSeparators { before, after ->
                val event = after ?: before
                when {
                    shouldSeparate(before?.tournament, after?.tournament) -> event?.let {
                        UiModel.SeparatorTournament(
                            it
                        )
                    }

                    else -> null
                }
            }
    }

    fun shouldSeparate(before: Tournament?, after: Tournament?): Boolean {
        if (after == null) {
            return false
        }
        return before?.id != after.id
    }


    private val _standings = MutableLiveData<ArrayList<Standing>>()

    fun setStandings(rows: ArrayList<Standing>) {
        _standings.value = rows
    }

    fun getStandings(): MutableLiveData<ArrayList<Standing>> {
        return _standings
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
            setSport(teamEvents.getCompleted().body()?.get(0)?.tournament?.sport?.name!!)
            setNextMatch(teamEvents.getCompleted())
        }
    }

    fun getTeamTournamentStandings() {
        viewModelScope.launch {
            var tournaments: ArrayList<Standing> = arrayListOf()

            if (_teamTournaments.value?.isSuccessful == true) {

                for (tournament in _teamTournaments.value?.body()!!) {

                    val response = Network().getService().getTournamentStandings(tournament.id)
                    if (response.isSuccessful) {
                        if (response.body()!!.size > 1) {
                            tournaments.addAll(response.body()!!.filterIndexed { index, _ ->
                                index % 3 == 2
                            })
                        } else {
                            tournaments.addAll(response.body()!!)
                        }
                    }

                }
                setStandings(tournaments)
            }
        }
    }

    private val _nextMatches = MutableLiveData<Response<ArrayList<Event>>>()

    fun setNextMatch(events: Response<ArrayList<Event>>) {
        _nextMatches.value = events
    }

    fun getNextMatch(): MutableLiveData<Response<ArrayList<Event>>> {
        return _nextMatches
    }

    private val _favourites = MutableLiveData<List<Favourite>>()
    var favourites: LiveData<List<Favourite>> = _favourites

    fun setFavourites(list: List<Favourite>) {
        _favourites.value = list
    }

    fun getFavourites(context: Context) {
        viewModelScope.launch {
            val databaseDao = SofascoreApiDatabase.getDatabase(context)?.sofascoreDao()
            databaseDao?.getAllFavourites()?.let { setFavourites(it) }
        }
    }

    fun addToFavourite(context: Context) {
        viewModelScope.launch {
            val databaseDao = SofascoreApiDatabase.getDatabase(context)?.sofascoreDao()
            val team = getTeamDetails().value?.body()!!
            databaseDao?.insertFavourite(Utilities().teamToFavourite(team))
        }
    }

    fun removeFromFavourites(context: Context) {
        viewModelScope.launch {
            val databaseDao = SofascoreApiDatabase.getDatabase(context)?.sofascoreDao()
            databaseDao?.deleteFavourite(_teamDetails.value?.body()!!.id)
        }
    }

}
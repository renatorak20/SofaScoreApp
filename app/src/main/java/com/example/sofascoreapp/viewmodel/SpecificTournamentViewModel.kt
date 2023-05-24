package com.example.sofascoreapp.viewmodel

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
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.Standing
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.model.UiModel
import com.example.sofascoreapp.data.networking.Network
import com.example.sofascoreapp.ui.paging.TeamEventsPagingSource
import com.example.sofascoreapp.ui.paging.TournamentEventsPagingSource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Response

class SpecificTournamentViewModel : ViewModel() {

    private val _tournamentID = MutableLiveData<Int>()

    fun setTournamentID(id: Int) {
        _tournamentID.value = id
    }

    fun getTournamentID(): MutableLiveData<Int> {
        return _tournamentID
    }

    private val _tournamentStandings = MutableLiveData<Standing>()

    fun setTournamentStandings(standings: Standing) {
        _tournamentStandings.value = standings
    }

    fun getTournamentStandings(): LiveData<Standing> {
        return _tournamentStandings
    }

    private val _tournamentInfo = MutableLiveData<Response<Tournament>>()

    fun setTournamentInfo(tournament: Response<Tournament>) {
        _tournamentInfo.value = tournament
    }

    fun getTournamentInfo(): MutableLiveData<Response<Tournament>> {
        return _tournamentInfo
    }

    fun getLatestTournamentInfo() {
        viewModelScope.launch {
            val response = async {
                Network().getService().getTournamentDetails(_tournamentID.value!!)
            }
            response.await()
            setTournamentInfo(response.getCompleted())
            setSport(response.getCompleted().body()?.sport?.name!!)
        }
    }

    fun getLatestTournamentStandings() {
        viewModelScope.launch {
            val response = Network().getService().getTournamentStandings(_tournamentID.value!!)
            if (response.isSuccessful) {
                when (_sport.value) {
                    "Football" -> setTournamentStandings(response.body()!![2])
                    else -> setTournamentStandings(response.body()!![0])
                }

            }
        }
    }

    private val _sport = MutableLiveData<String>()

    fun setSport(sport: String) {
        _sport.value = sport
    }

    fun getSport(): MutableLiveData<String> {
        return _sport
    }

    val tournamentEvents = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        0,
        pagingSourceFactory = { TournamentEventsPagingSource(_tournamentID.value!!) }
    ).liveData.cachedIn(viewModelScope).map { value: PagingData<Event> ->
        value.map { event ->
            UiModel.Event(event)
        }
            .insertSeparators { before, after ->
                val round = before?.round ?: after?.round
                when {
                    shouldSeparate(before, after) -> UiModel.SeparatorRound(
                        "Round $round"
                    )

                    else -> null
                }
            }
    }

    fun shouldSeparate(before: UiModel.Event?, after: UiModel.Event?): Boolean {
        if (before?.round == 1) return false
        if (after == null) return false
        return before?.round != after.round
    }

}
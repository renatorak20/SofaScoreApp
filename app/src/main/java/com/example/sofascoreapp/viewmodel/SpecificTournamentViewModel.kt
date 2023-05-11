package com.example.sofascoreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.sofascoreapp.data.model.Standing
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.networking.Network
import com.example.sofascoreapp.ui.paging.TeamEventsPagingSource
import com.example.sofascoreapp.ui.paging.TournamentEventsPagingSource
import kotlinx.coroutines.async
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

    private val _tournamentStandings = MutableLiveData<Response<ArrayList<Standing>>>()

    fun setTournamentStandings(standings: Response<ArrayList<Standing>>) {
        _tournamentStandings.value = standings
    }

    fun getTournamentStandings(): MutableLiveData<Response<ArrayList<Standing>>> {
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
            setTournamentStandings(
                Network().getService().getTournamentStandings(_tournamentID.value!!)
            )
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
    ).liveData.cachedIn(viewModelScope)

}
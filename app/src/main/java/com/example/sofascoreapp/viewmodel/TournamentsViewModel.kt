package com.example.sofascoreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.SportType
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.networking.Network
import kotlinx.coroutines.launch
import retrofit2.Response

class TournamentsViewModel : ViewModel() {

    private val _tournaments = MutableLiveData<Response<ArrayList<Tournament>>>()

    fun setTournaments(results: Response<ArrayList<Tournament>>) {
        _tournaments.value = results
    }

    fun getTournaments(): MutableLiveData<Response<ArrayList<Tournament>>> {
        return _tournaments
    }

    fun getTournaments(sport: SportType) {
        viewModelScope.launch {
            when (sport) {
                SportType.FOOTBALL -> setTournaments(
                    Network().getService().getTournamentsForSport("football")
                )

                SportType.BASKETBALL -> setTournaments(
                    Network().getService().getTournamentsForSport("basketball")
                )

                else -> setTournaments(
                    Network().getService().getTournamentsForSport("american-football")
                )
            }
        }
    }
}
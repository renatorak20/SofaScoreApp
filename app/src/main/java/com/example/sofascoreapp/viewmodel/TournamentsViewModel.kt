package com.example.sofascoreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.networking.Network
import kotlinx.coroutines.launch
import retrofit2.Response

class TournamentsViewModel : ViewModel() {

    private val _footballTournaments = MutableLiveData<Response<ArrayList<Tournament>>>()

    fun setFootballTournaments(results: Response<ArrayList<Tournament>>) {
        _footballTournaments.value = results
    }

    fun footballTournaments(): MutableLiveData<Response<ArrayList<Tournament>>> {
        return _footballTournaments
    }

    fun getFootballTournaments() {
        viewModelScope.launch {
            setFootballTournaments(Network().getService().getTournamentsForSport("football"))
        }
    }


    private val _basketballTournaments = MutableLiveData<Response<ArrayList<Tournament>>>()

    fun setBasketballTournaments(results: Response<ArrayList<Tournament>>) {
        _basketballTournaments.value = results
    }

    fun basketballTournaments(): MutableLiveData<Response<ArrayList<Tournament>>> {
        return _basketballTournaments
    }

    fun getBasketballTournaments() {
        viewModelScope.launch {
            setBasketballTournaments(Network().getService().getTournamentsForSport("basketball"))
        }
    }


    private val _AFootballTournaments = MutableLiveData<Response<ArrayList<Tournament>>>()

    fun setAFootballTournaments(results: Response<ArrayList<Tournament>>) {
        _AFootballTournaments.value = results
    }

    fun aFootballTournaments(): MutableLiveData<Response<ArrayList<Tournament>>> {
        return _AFootballTournaments
    }

    fun getAFootballTournaments() {
        viewModelScope.launch {
            setAFootballTournaments(
                Network().getService().getTournamentsForSport("american-football")
            )
        }
    }


}
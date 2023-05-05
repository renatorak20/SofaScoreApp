package com.example.sofascoreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.networking.Network
import kotlinx.coroutines.launch
import retrofit2.Response

class SharedViewModel : ViewModel() {

    private val _footballEvents = MutableLiveData<Response<ArrayList<Event>>>()

    fun setFootballEvents(results: Response<ArrayList<Event>>) {
        _footballEvents.value = results
    }

    fun footballEvents(): MutableLiveData<Response<ArrayList<Event>>> {
        return _footballEvents
    }

    fun getFootballEvents(sport: String, date: String) {
        viewModelScope.launch {
            setFootballEvents(Network().getService().getEventsForSportDate(sport, date))
        }
    }

}
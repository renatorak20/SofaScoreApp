package com.example.sofascoreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.MatchDate
import com.example.sofascoreapp.data.networking.Network
import kotlinx.coroutines.launch
import retrofit2.Response

class MatchDetailViewModel : ViewModel() {

    var matchID: Int = 0

    private val _event = MutableLiveData<Response<Event>>()

    fun setEvent(results: Response<Event>) {
        _event.value = results
    }

    fun getEvent(): MutableLiveData<Response<Event>> {
        return _event
    }

    fun getEventInfo() {
        viewModelScope.launch {
            setEvent(Network().getService().getEvent(matchID))
        }
    }


    private val _incidents = MutableLiveData<Response<ArrayList<Any>>>()

    fun setIncidents(results: Response<ArrayList<Any>>) {
        _incidents.value = results
    }

    fun getIncidents(): MutableLiveData<Response<ArrayList<Any>>> {
        return _incidents
    }

    fun getEventIncidents() {
        viewModelScope.launch {
            setIncidents(Network().getService().getEventIncidents(matchID))
        }
    }

}
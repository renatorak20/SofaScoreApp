package com.example.sofascoreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.Incident
import com.example.sofascoreapp.data.networking.Network
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class MatchDetailViewModel : ViewModel() {

    var matchID: Int = 0

    private val _event = MutableLiveData<Response<Event>>()

    private fun setEvent(results: Response<Event>) {
        _event.value = results
    }

    fun getEvent(): MutableLiveData<Response<Event>> {
        return _event
    }


    private val _incidents = MutableLiveData<Response<ArrayList<Incident>>>()

    private fun setIncidents(results: Response<ArrayList<Incident>>) {
        _incidents.value = results
    }

    fun getIncidents(): MutableLiveData<Response<ArrayList<Incident>>> {
        return _incidents
    }

    fun getAllEventInfo() {
        viewModelScope.launch {
            val infoResponse = async {
                Network().getService().getEvent(matchID)
            }
            val incidentsResponse = async {
                Network().getService().getEventIncidents(matchID)
            }

            infoResponse.await()
            incidentsResponse.await()

            setEvent(infoResponse.getCompleted())
            setIncidents(incidentsResponse.getCompleted())

        }
    }

}
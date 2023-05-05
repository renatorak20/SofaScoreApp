package com.example.sofascoreapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.MatchDate
import com.example.sofascoreapp.data.networking.Network
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SharedViewModel : ViewModel() {

    private val _availableDays = MutableLiveData<ArrayList<MatchDate>>()

    fun getAvailableDays(): MutableLiveData<ArrayList<MatchDate>> {
        return _availableDays
    }

    fun setAvailableDays(results: ArrayList<MatchDate>) {
        _availableDays.value = results
    }

    fun initializeAvailableDays() {

        val list: MutableList<MatchDate> = mutableListOf()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val currentDate = LocalDate.now()
        val thirtyDaysAgo = currentDate.minusDays(30)
        val thirtyDaysLater = currentDate.plusDays(30)

        var date = thirtyDaysAgo
        while (date.isBefore(currentDate)) {
            list.add(MatchDate(date.format(formatter)))
            date = date.plusDays(1)
        }

        list.add(MatchDate(currentDate.format(formatter), true))

        date = currentDate.plusDays(1)
        while (date.isBefore(thirtyDaysLater.plusDays(1))) {
            list.add(MatchDate(date.format(formatter)))
            date = date.plusDays(1)
        }

        setAvailableDays(list as ArrayList<MatchDate>)
    }

    private val _footballEvents = MutableLiveData<Response<ArrayList<Event>>>()

    fun setFootballEvents(results: Response<ArrayList<Event>>) {
        _footballEvents.value = results
    }

    fun footballEvents(): MutableLiveData<Response<ArrayList<Event>>> {
        return _footballEvents
    }

    fun getNewestEvents(sport: String, date: String) {
        viewModelScope.launch {
            when (sport) {
                "football" -> {
                    setFootballEvents(Network().getService().getEventsForSport(sport, date))
                }

                "basketball" -> {
                    setBasketballEvents(Network().getService().getEventsForSport(sport, date))
                }

                else -> {
                    setFootballEvents(Network().getService().getEventsForSport(sport, date))
                }
            }
        }
    }

    private val _basketballEvents = MutableLiveData<Response<ArrayList<Event>>>()

    fun setBasketballEvents(results: Response<ArrayList<Event>>) {
        _basketballEvents.value = results
    }

    fun basketballEvents(): MutableLiveData<Response<ArrayList<Event>>> {
        return _basketballEvents
    }

    private val _amerFootballEvents = MutableLiveData<Response<ArrayList<Event>>>()

    fun setAmerFootballEvents(results: Response<ArrayList<Event>>) {
        _amerFootballEvents.value = results
    }

    fun amerFootballEvents(): MutableLiveData<Response<ArrayList<Event>>> {
        return _amerFootballEvents
    }

    private val _footballDate = MutableLiveData<String>()
    var footballDate: LiveData<String> = _footballDate
        get() = _footballDate

    fun setFootballDate(date: String) {
        _footballDate.value = date
    }


    private val _basketballDate = MutableLiveData<String>()
    var basketballDate: LiveData<String> = _basketballDate
        get() = _basketballDate

    fun setBasketballDate(date: String) {
        _basketballDate.value = date
    }


    private val _amerFootballDate = MutableLiveData<String>()
    var amerFootballDate: LiveData<String> = _amerFootballDate
        get() = _amerFootballDate

    fun setAmerFootballDate(date: String) {
        _amerFootballDate.value = date
    }


}
package com.example.sofascoreapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.MatchDate
import com.example.sofascoreapp.data.model.SportType
import com.example.sofascoreapp.data.networking.Network
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel : ViewModel() {

    private val _availableDays = MutableLiveData<ArrayList<MatchDate>>()
    private val _date = MutableLiveData<String>()

    fun setAvailableDays(days: ArrayList<MatchDate>) {
        _availableDays.value = days
    }

    fun getAvailableDays(): MutableLiveData<ArrayList<MatchDate>> {
        return _availableDays
    }

    fun getDate(): MutableLiveData<String> {
        return _date
    }

    fun setDate(date: String) {
        _date.value = date
        clearDays()
        _availableDays.value!![_availableDays.value!!.indexOf(
            MatchDate(
                date,
                false
            )
        )].isSelected = true
    }

    fun clearDays() {
        if (!_availableDays.value.isNullOrEmpty()) {
            for (item in _availableDays.value!!) {
                item.isSelected = false
            }
        }
    }

    fun initializeAvailableDays(days: Int) {

        val list: MutableList<MatchDate> = mutableListOf()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val currentDate = LocalDate.now()
        val thirtyDaysAgo = currentDate.minusDays(days.toLong())
        val thirtyDaysLater = currentDate.plusDays(days.toLong())

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

        setAvailableDays((list as ArrayList<MatchDate>))
    }

    private val _events = MutableLiveData<Response<ArrayList<Event>>>()

    fun setEvents(results: Response<ArrayList<Event>>) {
        _events.value = results
    }

    fun getEvents(): MutableLiveData<Response<ArrayList<Event>>> {
        return _events
    }

    fun getNewestEvents(sport: SportType, date: String) {
        viewModelScope.launch {
            when (sport) {
                SportType.FOOTBALL -> {
                    setEvents(Network().getService().getEventsForSport("football", date))
                }

                SportType.BASKETBALL -> {
                    setEvents(Network().getService().getEventsForSport("basketball", date))
                }

                else -> {
                    setEvents(Network().getService().getEventsForSport("american-football", date))
                }
            }
        }
    }



}
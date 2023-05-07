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

class MainViewModel : ViewModel() {

    private val _footballAvailableDays = MutableLiveData<ArrayList<MatchDate>>()

    fun getFootballAvailableDays(): MutableLiveData<ArrayList<MatchDate>> {
        return _footballAvailableDays
    }

    fun setFootballAvailableDays(results: ArrayList<MatchDate>) {
        _footballAvailableDays.value = results
    }

    private val _basketballAvailableDays = MutableLiveData<ArrayList<MatchDate>>()

    fun getBasketballAvailableDays(): MutableLiveData<ArrayList<MatchDate>> {
        return _basketballAvailableDays
    }

    fun setBasketballAvailableDays(results: ArrayList<MatchDate>) {
        _basketballAvailableDays.value = results
    }

    private val _aFootballAvailableDays = MutableLiveData<ArrayList<MatchDate>>()

    fun getAFootballAvailableDays(): MutableLiveData<ArrayList<MatchDate>> {
        return _aFootballAvailableDays
    }

    fun setAFootballAvailableDays(results: ArrayList<MatchDate>) {
        _aFootballAvailableDays.value = results
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

        setFootballAvailableDays(list as ArrayList<MatchDate>)
        setBasketballAvailableDays(list)
        setAFootballAvailableDays(list)
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
        clearFootballDays()
        _footballAvailableDays.value!![_footballAvailableDays.value!!.indexOf(
            MatchDate(
                date,
                false
            )
        )].isSelected = true
    }

    fun clearFootballDays() {
        if (!_footballAvailableDays.value.isNullOrEmpty()) {
            for (item in _footballAvailableDays.value!!) {
                item.isSelected = false
            }
        }
    }


    private val _basketballDate = MutableLiveData<String>()
    var basketballDate: LiveData<String> = _basketballDate
        get() = _basketballDate

    fun setBasketballDate(date: String) {
        _basketballDate.value = date
        clearBasketballDays()
        _basketballAvailableDays.value!![_basketballAvailableDays.value!!.indexOf(
            MatchDate(
                date,
                false
            )
        )].isSelected =
            true
    }

    fun clearBasketballDays() {
        if (!_basketballAvailableDays.value.isNullOrEmpty()) {
            for (item in _basketballAvailableDays.value!!) {
                item.isSelected = false
            }
        }
    }


    private val _amerFootballDate = MutableLiveData<String>()
    var amerFootballDate: LiveData<String> = _amerFootballDate
        get() = _amerFootballDate

    fun setAmerFootballDate(date: String) {
        _amerFootballDate.value = date
    }

    fun setAFootballDate(date: String) {
        _amerFootballDate.value = date
        clearAFootballDays()
        _aFootballAvailableDays.value!![_aFootballAvailableDays.value!!.indexOf(
            MatchDate(
                date,
                false
            )
        )].isSelected =
            true
    }

    fun clearAFootballDays() {
        if (!_aFootballAvailableDays.value.isNullOrEmpty()) {
            for (item in _aFootballAvailableDays.value!!) {
                item.isSelected = false
            }
        }
    }


}
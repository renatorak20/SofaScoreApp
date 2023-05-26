package com.example.sofascoreapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.PlayerAutocomplete
import com.example.sofascoreapp.data.model.TeamAutocomplete
import com.example.sofascoreapp.data.networking.Network
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchActivityViewModel : ViewModel() {

    private val _autocompleteList = MutableLiveData<ArrayList<Any>>()
    val autocompleteList: LiveData<ArrayList<Any>> = _autocompleteList

    fun setAutocompleteList(list: ArrayList<Any>) {
        _autocompleteList.value = list
    }

    fun getAllAutocompletes(query: String) {
        viewModelScope.launch {
            val teams = async {
                Network().getService().getTeamAutocomplete(query)
            }
            val players = async {
                Network().getService().getPlayerAutocomplete(query)
            }

            teams.await()
            players.await()

            val list = arrayListOf<Any>()

            if (teams.getCompleted().isSuccessful) {
                list.addAll(teams.getCompleted().body()!!)
            }
            if (players.getCompleted().isSuccessful) {
                list.addAll(players.getCompleted().body()!!)
            }

            setAutocompleteList(list)
        }
    }

}
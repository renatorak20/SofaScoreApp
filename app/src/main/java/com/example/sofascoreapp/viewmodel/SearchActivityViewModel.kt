package com.example.sofascoreapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.RecentSearch
import com.example.sofascoreapp.data.networking.Network
import com.example.sofascoreapp.database.SofascoreApiDatabase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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

    private val _recentsList = MutableLiveData<List<RecentSearch>>()
    val recentsList: LiveData<List<RecentSearch>> = _recentsList

    fun setRecentsList(list: List<RecentSearch>) {
        _recentsList.value = list
    }


    fun getRecentSearches(context: Context) {
        viewModelScope.launch {
            val databaseDao = SofascoreApiDatabase.getDatabase(context)?.sofascoreDao()
            val recents = databaseDao?.getAllRecents()
            recents?.let { setRecentsList(it) }
        }
    }

    fun addNewRecentSearch(context: Context, recentItem: RecentSearch) {
        viewModelScope.launch {
            val databaseDao = SofascoreApiDatabase.getDatabase(context)?.sofascoreDao()
            databaseDao?.insertRecent(recentItem)
        }
    }

    fun removeFromRecentSearch(context: Context, id: Int) {
        viewModelScope.launch {
            val databaseDao = SofascoreApiDatabase.getDatabase(context)?.sofascoreDao()
            databaseDao?.deleteRecent(id)
        }
    }

}
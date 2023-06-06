package com.example.sofascoreapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.liveData
import androidx.paging.map
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.Favourite
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.model.UiModel
import com.example.sofascoreapp.data.networking.Network
import com.example.sofascoreapp.database.SofascoreApiDatabase
import com.example.sofascoreapp.ui.paging.PlayerEventsPagingSource
import com.example.sofascoreapp.utils.Utilities
import kotlinx.coroutines.launch
import retrofit2.Response

class PlayerDetailsViewModel : ViewModel() {

    private val _playerID = MutableLiveData<Int>()

    fun setPlayerID(id: Int) {
        _playerID.value = id
    }

    fun getPlayerID(): MutableLiveData<Int> {
        return _playerID
    }

    private val _player = MutableLiveData<Response<Player>>()

    fun setPlayer(player: Response<Player>) {
        _player.value = player
    }

    fun getPlayer(): MutableLiveData<Response<Player>> {
        return _player
    }


    fun getPlayerInformation() {
        viewModelScope.launch {
            setPlayer(Network().getService().getPlayerInfo(_playerID.value!!))
        }
    }


    val playerEvents = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        0,
        pagingSourceFactory = { PlayerEventsPagingSource(_playerID.value!!) }
    ).liveData.cachedIn(viewModelScope).map { value: PagingData<Event> ->
        value.map { event ->
            UiModel.Event(event)
        }
            .insertSeparators { before, after ->
                val event = after ?: before
                when {
                    shouldSeparate(before?.tournament, after?.tournament) -> event?.let {
                        UiModel.SeparatorTournament(
                            it
                        )
                    }

                    else -> null
                }
            }
    }

    private fun shouldSeparate(before: Tournament?, after: Tournament?): Boolean {
        if (before?.id == 1) return false
        if (after == null) return false
        return before?.id != after.id
    }

    private val _favourites = MutableLiveData<List<Favourite>>()
    var favourites: LiveData<List<Favourite>> = _favourites

    private fun setFavourites(list: List<Favourite>) {
        _favourites.value = list
    }

    fun getFavourites(context: Context) {
        viewModelScope.launch {
            val databaseDao = SofascoreApiDatabase.getDatabase(context)?.sofascoreDao()
            databaseDao?.getAllFavourites()?.let { setFavourites(it) }
        }
    }

    fun addToFavourite(context: Context) {
        viewModelScope.launch {
            val databaseDao = SofascoreApiDatabase.getDatabase(context)?.sofascoreDao()
            val player = _player.value?.body()!!
            databaseDao?.insertFavourite(Utilities().playerToFavourite(player))
        }
    }

    fun removeFromFavourites(context: Context) {
        viewModelScope.launch {
            val databaseDao = SofascoreApiDatabase.getDatabase(context)?.sofascoreDao()
            databaseDao?.deleteFavourite(_player.value?.body()!!.id)
        }
    }

}
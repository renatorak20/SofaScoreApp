package com.example.sofascoreapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.data.networking.Network
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

}
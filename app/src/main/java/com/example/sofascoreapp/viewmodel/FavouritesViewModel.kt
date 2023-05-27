package com.example.sofascoreapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sofascoreapp.data.model.Favourite
import com.example.sofascoreapp.database.SofascoreApiDatabase
import kotlinx.coroutines.launch

class FavouritesViewModel : ViewModel() {

    private val _favourites = MutableLiveData<List<Favourite>>()
    var favourites: LiveData<List<Favourite>> = _favourites

    fun setFavourites(list: List<Favourite>) {
        _favourites.value = list
    }

    fun getFavourites(context: Context) {
        viewModelScope.launch {
            val databaseDao = SofascoreApiDatabase.getDatabase(context)?.sofascoreDao()
            databaseDao?.getAllFavourites()?.let { setFavourites(it) }
        }
    }

    fun removeFromFavourites(context: Context, id: Int) {
        viewModelScope.launch {
            val databaseDao = SofascoreApiDatabase.getDatabase(context)?.sofascoreDao()
            databaseDao?.deleteFavourite(id)
        }
    }

}
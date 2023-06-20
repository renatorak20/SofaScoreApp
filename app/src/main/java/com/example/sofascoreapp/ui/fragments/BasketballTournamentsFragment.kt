package com.example.sofascoreapp.ui.fragments

import com.example.sofascoreapp.data.model.SportType

class BasketballTournamentsFragment : SportTournamentsFragment() {

    override fun getSportType(): SportType {
        return SportType.BASKETBALL
    }
}
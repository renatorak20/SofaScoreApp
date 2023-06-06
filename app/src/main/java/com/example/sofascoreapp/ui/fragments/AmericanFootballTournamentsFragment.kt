package com.example.sofascoreapp.ui.fragments

import com.example.sofascoreapp.data.model.SportType

class AmericanFootballTournamentsFragment : SportTournamentsFragment() {

    override fun getSportType(): SportType {
        return SportType.AMERICAN_FOOTBALL
    }
}
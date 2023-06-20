package com.example.sofascoreapp.ui.fragments

import com.example.sofascoreapp.data.model.SportType

class AmericanFootballFragment : SportEventsFragment() {

    override fun getSportType(): SportType {
        return SportType.AMERICAN_FOOTBALL
    }
}
package com.example.sofascoreapp.ui.fragments

import com.example.sofascoreapp.data.model.SportType
import com.example.sofascoreapp.utils.Utilities

class AmericanFootballFragment : SportEventsFragment() {

    override fun getSportType(): SportType {
        return SportType.AMERICAN_FOOTBALL
    }
}
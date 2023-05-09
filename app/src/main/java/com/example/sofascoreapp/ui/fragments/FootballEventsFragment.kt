package com.example.sofascoreapp.ui.fragments

import com.example.sofascoreapp.data.model.SportType
class FootballEventsFragment : SportEventsFragment() {

    override fun getSportType(): SportType {
        return SportType.FOOTBALL
    }
}
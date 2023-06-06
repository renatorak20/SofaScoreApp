package com.example.sofascoreapp.ui.fragments

import com.example.sofascoreapp.data.model.SportType

class BasketballEventsFragment : SportEventsFragment() {

    override fun getSportType(): SportType {
        return SportType.BASKETBALL
    }
}
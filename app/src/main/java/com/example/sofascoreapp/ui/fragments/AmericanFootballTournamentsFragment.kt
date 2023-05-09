package com.example.sofascoreapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.SportType
import com.example.sofascoreapp.databinding.FragmentSportTournamentsBinding
import com.example.sofascoreapp.ui.adapters.TournamentsAdapter
import com.example.sofascoreapp.viewmodel.TournamentsViewModel

class AmericanFootballTournamentsFragment : SportTournamentsFragment() {

    override fun getSportType(): SportType {
        return SportType.AMERICAN_FOOTBALL
    }
}
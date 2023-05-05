package com.example.sofascoreapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sofascoreapp.ui.fragments.AmericanFootballFragment
import com.example.sofascoreapp.ui.fragments.AmericanFootballTournamentsFragment
import com.example.sofascoreapp.ui.fragments.BasketballEventsFragment
import com.example.sofascoreapp.ui.fragments.BasketballTournamentsFragment
import com.example.sofascoreapp.ui.fragments.FootballEventsFragment
import com.example.sofascoreapp.ui.fragments.FootballTournamentsFragment

private const val NUM_TABS = 3

class SectionsPagerAdapter(fa: FragmentActivity, val type: Int) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (type) {
            1 -> {
                return when (position) {
                    0 -> FootballEventsFragment()
                    1 -> BasketballEventsFragment()
                    else -> AmericanFootballFragment()
                }
            }

            else -> {
                return when (position) {
                    0 -> FootballTournamentsFragment()
                    1 -> BasketballTournamentsFragment()
                    else -> AmericanFootballTournamentsFragment()
                }
            }
        }
    }
}
package com.example.sofascoreapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sofascoreapp.ui.fragments.AmericanFootballFragment
import com.example.sofascoreapp.ui.fragments.BasketballFragment
import com.example.sofascoreapp.ui.fragments.FootballFragment

private const val NUM_TABS = 3

class SectionsPagerAdapter(fa: FragmentActivity) :
    FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FootballFragment()
            1 -> BasketballFragment()
            else -> AmericanFootballFragment()
        }
    }
}
package com.example.sofascoreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.sofascoreapp.databinding.ActivityTournamentsBinding
import com.example.sofascoreapp.databinding.ViewPagerTabBinding
import com.example.sofascoreapp.ui.SectionsPagerAdapter
import com.example.sofascoreapp.viewmodel.TournamentsViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TournamentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTournamentsBinding
    private lateinit var tournamentViewModel: TournamentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tournaments)

        binding = ActivityTournamentsBinding.inflate(layoutInflater)
        tournamentViewModel = ViewModelProvider(this)[TournamentsViewModel::class.java]

        val sectionsPagerAdapter = SectionsPagerAdapter(this, 2)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            val customTabView = ViewPagerTabBinding.inflate(layoutInflater)
            when (position) {
                0 -> {
                    customTabView.tabTitle.text = getString(R.string.football)
                    customTabView.tabIcon.setImageDrawable(getDrawable(R.drawable.icon_football))
                }

                1 -> {
                    customTabView.tabTitle.text = getString(R.string.basketball)
                    customTabView.tabIcon.setImageDrawable(getDrawable(R.drawable.icon_basketball))
                }

                else -> {
                    customTabView.tabTitle.text = getString(R.string.amer_football_short)
                    customTabView.tabIcon.setImageDrawable(getDrawable(R.drawable.icon_american_football))
                }
            }
            tab.customView = customTabView.root
        }.attach()

    }

}
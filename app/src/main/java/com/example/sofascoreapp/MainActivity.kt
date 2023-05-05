package com.example.sofascoreapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.sofascoreapp.databinding.ActivityMainBinding
import com.example.sofascoreapp.databinding.ViewPagerTabBinding
import com.example.sofascoreapp.ui.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


val tabArray = arrayOf(
    R.string.football,
    R.string.basketball,
    R.string.amer_football_short
)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) {
                tab, position ->
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
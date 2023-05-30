package com.example.sofascoreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.ui.adapters.RecentFavouriteAdapter
import com.example.sofascoreapp.databinding.ActivitySearchBinding
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.SearchActivityViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var sharedViewModel: SearchActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this)[SearchActivityViewModel::class.java]

        binding.autoComplete.threshold = 3
        binding.autoComplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(text: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length!! >= 3) {
                    getInfo(s.toString())
                    hideIndicator()
                } else if (s.isEmpty()) {
                    binding.autoComplete.setAdapter(null)
                    sharedViewModel.getRecentSearches(applicationContext)
                }
            }
        })

        sharedViewModel.getRecentSearches(this)
        sharedViewModel.recentsList.observe(this) {

            if (it.isEmpty()) {
                showIndicator()
            }

            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter =
                RecentFavouriteAdapter(this, it as ArrayList<Any>, sharedViewModel)
        }

        sharedViewModel.autocompleteList.observe(this) { list ->

            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = RecentFavouriteAdapter(this, list, sharedViewModel)

        }


        binding.clearIcon.setOnClickListener {
            binding.autoComplete.clear()
        }

        binding.back.setOnClickListener {
            finish()
        }
    }

    fun getInfo(query: String) {
        if (Utilities().isNetworkAvailable(this)) {
            sharedViewModel.getAllAutocompletes(query)
        } else {
            Utilities.showNoInternetDialog(this) { getInfo(query) }
        }
    }

    fun AutoCompleteTextView.clear() {
        this.text.clear()
    }

    fun hideIndicator() {
        binding.recentTitle.visibility = View.VISIBLE
        binding.description.visibility = View.INVISIBLE
    }

    fun showIndicator() {
        binding.recentTitle.visibility = View.INVISIBLE
        binding.description.visibility = View.VISIBLE
    }

}
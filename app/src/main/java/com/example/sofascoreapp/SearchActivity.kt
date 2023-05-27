package com.example.sofascoreapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.adapters.RecentFavouriteAdapter
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
                if (s?.length!! >= 3 && Utilities().isNetworkAvailable(applicationContext)) {
                    sharedViewModel.getAllAutocompletes(s.toString())
                    binding.recentTitle.visibility = View.GONE
                } else if (s.isEmpty()) {
                    binding.autoComplete.setAdapter(null)
                    binding.recentTitle.visibility = View.VISIBLE
                    sharedViewModel.getRecentSearches(applicationContext)
                }
            }
        })

        sharedViewModel.getRecentSearches(this)
        sharedViewModel.recentsList.observe(this) {
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


    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }


    fun AutoCompleteTextView.clear() {
        this.text.clear()
    }

}
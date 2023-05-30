package com.example.sofascoreapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofascoreapp.ui.adapters.RecentFavouriteAdapter
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.databinding.ActivityFavouritesBinding
import com.example.sofascoreapp.viewmodel.FavouritesViewModel

const val ALL = 0
const val TEAMS = 1
const val PLAYERS = 2

class FavouritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouritesBinding
    private lateinit var viewModel: FavouritesViewModel
    var recyclerViewVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[FavouritesViewModel::class.java]

        viewModel.getFavourites(this)

        viewModel.favourites.observe(this) {

            toggleAnimation(it)

            if (it.isEmpty()) {
                binding.toggleButton.visibility = View.INVISIBLE
            }

            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter =
                RecentFavouriteAdapter(this, it as ArrayList<Any>, viewModel)
        }

        binding.toolbar.title.text = getString(R.string.favourites)

        binding.toggleButton.addOnButtonCheckedListener { group, checkedId, _ ->
            val selectedIndex = group.indexOfChild(group.findViewById(checkedId))

            if (selectedIndex != -1) {
                when (selectedIndex) {
                    ALL -> {
                        toggleAnimation(viewModel.favourites.value!!)
                        binding.recyclerView.adapter = RecentFavouriteAdapter(
                            this,
                            viewModel.favourites.value as ArrayList<Any>,
                            viewModel
                        )
                    }

                    TEAMS -> {
                        toggleAnimation(viewModel.favourites.value!!)
                        binding.recyclerView.adapter = RecentFavouriteAdapter(
                            this,
                            viewModel.favourites.value?.filter { it.type == DataType.TEAM } as ArrayList<Any>,
                            viewModel)
                    }

                    PLAYERS -> {
                        toggleAnimation(viewModel.favourites.value?.filter { it.type == DataType.PLAYER } as ArrayList<Any>)
                        binding.recyclerView.adapter = RecentFavouriteAdapter(
                            this,
                            viewModel.favourites.value?.filter { it.type == DataType.PLAYER } as ArrayList<Any>,
                            viewModel)
                    }
                }
            }
        }


        binding.toolbar.back.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavourites(this)
    }

    fun toggleAnimation(list: List<Any>) {
        with(binding) {
            if (list.isEmpty()) {
                animation.visibility = View.VISIBLE
                recyclerView.visibility = View.INVISIBLE
                description.visibility = View.VISIBLE
            } else {
                animation.visibility = View.INVISIBLE
                recyclerView.visibility = View.VISIBLE
                description.visibility = View.INVISIBLE
            }
        }
    }


}
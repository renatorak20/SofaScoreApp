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
    var tabSelected = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[FavouritesViewModel::class.java]

        viewModel.getFavourites(this)

        viewModel.favourites.observe(this) { favouritesList ->

            toggleAnimation(favouritesList)

            if (favouritesList.isEmpty()) {
                binding.toggleButton.visibility = View.INVISIBLE
            }

            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            val list = when (tabSelected) {
                0 -> favouritesList as ArrayList<Any>
                1 -> favouritesList.filter { it.type == DataType.TEAM } as ArrayList<Any>
                else -> favouritesList.filter { it.type == DataType.PLAYER } as ArrayList<Any>
            }
            binding.recyclerView.adapter =
                RecentFavouriteAdapter(this, list, viewModel)
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
                        tabSelected = 0
                    }

                    TEAMS -> {
                        toggleAnimation(viewModel.favourites.value?.filter { it.type == DataType.TEAM } as ArrayList<Any>)
                        binding.recyclerView.adapter = RecentFavouriteAdapter(
                            this,
                            viewModel.favourites.value?.filter { it.type == DataType.TEAM } as ArrayList<Any>,
                            viewModel)
                        tabSelected = 1
                    }

                    PLAYERS -> {
                        toggleAnimation(viewModel.favourites.value?.filter { it.type == DataType.PLAYER } as ArrayList<Any>)
                        binding.recyclerView.adapter = RecentFavouriteAdapter(
                            this,
                            viewModel.favourites.value?.filter { it.type == DataType.PLAYER } as ArrayList<Any>,
                            viewModel)
                        tabSelected = 2
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
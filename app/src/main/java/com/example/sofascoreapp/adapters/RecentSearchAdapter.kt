package com.example.sofascoreapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sofascoreapp.PlayerDetailsActivity
import com.example.sofascoreapp.R
import com.example.sofascoreapp.TeamDetailsActivity
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.data.model.PlayerAutocomplete
import com.example.sofascoreapp.data.model.RecentSearch
import com.example.sofascoreapp.data.model.TeamAutocomplete
import com.example.sofascoreapp.databinding.RecentSearchItemBinding
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.utils.Utilities.Companion.loadImage
import com.example.sofascoreapp.viewmodel.SearchActivityViewModel


class RecentSearchAdapter(
    val context: Context,
    val array: ArrayList<Any>,
    val viewModel: SearchActivityViewModel
) : RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder>() {

    class RecentSearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecentSearchItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        return RecentSearchViewHolder(
            LayoutInflater.from(context).inflate(R.layout.recent_search_item, parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {

        val item = array[position]

        with(holder.binding) {
            when (item) {
                is PlayerAutocomplete -> {
                    image.loadImage(context, DataType.PLAYER, item.id)
                    layout.setOnClickListener {
                        viewModel.addNewRecentSearch(context, Utilities().playerToRecent(item))
                        PlayerDetailsActivity.start(context, item.id)
                    }
                    name.text = item.name
                    deleteIcon.visibility = View.INVISIBLE
                }

                is TeamAutocomplete -> {
                    image.load(context.getString(R.string.team_icon_url, item.id))
                    layout.setOnClickListener {
                        viewModel.addNewRecentSearch(context, Utilities().teamToRecent(item))
                        TeamDetailsActivity.start(context, item.id)
                    }
                    name.text = item.name
                    deleteIcon.visibility = View.INVISIBLE
                }

                is RecentSearch -> {
                    name.text = item.name
                    deleteIcon.visibility = View.VISIBLE
                    when (item.type) {
                        DataType.PLAYER -> {
                            image.loadImage(context, DataType.PLAYER, item.id)
                            layout.setOnClickListener {
                                PlayerDetailsActivity.start(context, item.id)
                            }
                            deleteIcon.setOnClickListener {
                                viewModel.removeFromRecentSearch(context, item.id)
                                removeItem(position)
                            }
                        }

                        else -> {
                            image.load(context.getString(R.string.team_icon_url, item.id))
                            layout.setOnClickListener {
                                TeamDetailsActivity.start(context, item.id)
                            }
                            deleteIcon.setOnClickListener {
                                viewModel.removeFromRecentSearch(context, item.id)
                                removeItem(position)
                            }
                        }
                    }
                }
            }
        }
    }

    fun removeItem(position: Int) {
        array.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    override fun getItemCount() = array.size

}
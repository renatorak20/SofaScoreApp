package com.example.sofascoreapp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sofascoreapp.PlayerDetailsActivity
import com.example.sofascoreapp.R
import com.example.sofascoreapp.TeamDetailsActivity
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.data.model.Favourite
import com.example.sofascoreapp.data.model.PlayerAutocomplete
import com.example.sofascoreapp.data.model.RecentSearch
import com.example.sofascoreapp.data.model.TeamAutocomplete
import com.example.sofascoreapp.databinding.RecentSearchItemBinding
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.utils.Utilities.Companion.loadImage
import com.example.sofascoreapp.utils.Utilities.Companion.setSportIcon
import com.example.sofascoreapp.viewmodel.FavouritesViewModel
import com.example.sofascoreapp.viewmodel.SearchActivityViewModel


class RecentFavouriteAdapter(
    val context: Context,
    val array: ArrayList<Any>,
    val viewModel: ViewModel
) : RecyclerView.Adapter<RecentFavouriteAdapter.RecentSearchViewHolder>() {

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
                    sport.text = item.sport.name
                    sportImage.setSportIcon(context, item.sport.name)
                    layout.setOnClickListener {
                        (viewModel as SearchActivityViewModel).addNewRecentSearch(
                            context,
                            Utilities().playerToRecent(item)
                        )
                        PlayerDetailsActivity.start(context, item.id)
                    }
                    name.text = item.name
                    deleteIcon.visibility = View.INVISIBLE
                }

                is TeamAutocomplete -> {
                    image.load(context.getString(R.string.team_icon_url, item.id))
                    sport.text = item.sport.name
                    sportImage.setSportIcon(context, item.sport.name)
                    layout.setOnClickListener {
                        (viewModel as SearchActivityViewModel).addNewRecentSearch(
                            context,
                            Utilities().teamToRecent(item)
                        )
                        TeamDetailsActivity.start(context, item.id)
                    }
                    name.text = item.name
                    deleteIcon.visibility = View.INVISIBLE
                }

                is RecentSearch -> {
                    name.text = item.name
                    deleteIcon.visibility = View.VISIBLE
                    deleteIcon.setImageDrawable(context.getDrawable(R.drawable.ic_close))
                    sport.text = item.sport
                    sportImage.setSportIcon(context, item.sport)
                    when (item.type) {
                        DataType.PLAYER -> {
                            image.loadImage(context, DataType.PLAYER, item.id)
                            layout.setOnClickListener {
                                PlayerDetailsActivity.start(context, item.id)
                            }
                        }

                        else -> {
                            image.load(context.getString(R.string.team_icon_url, item.id)) {
                                error(context.getDrawable(R.drawable.ic_person))
                                placeholder(context.getDrawable(R.drawable.ic_person))
                            }
                            layout.setOnClickListener {
                                TeamDetailsActivity.start(context, item.id)
                            }
                        }
                    }
                    deleteIcon.setOnClickListener {
                        (viewModel as SearchActivityViewModel).removeFromRecentSearch(
                            context,
                            item.id
                        )
                        removeItem(position)
                    }
                }

                is Favourite -> {
                    name.text = item.name
                    deleteIcon.visibility = View.VISIBLE
                    deleteIcon.setImageDrawable(context.getDrawable(R.drawable.ic_star_fill))
                    deleteIcon.setColorFilter(context.getColor(R.color.color_primary_default))
                    when (item.type) {
                        DataType.PLAYER -> {
                            image.loadImage(context, DataType.PLAYER, item.id)
                            layout.setOnClickListener {
                                PlayerDetailsActivity.start(context, item.id)
                            }
                        }

                        else -> {
                            image.load(context.getString(R.string.team_icon_url, item.id)) {
                                error(context.getDrawable(R.drawable.ic_person))
                                placeholder(context.getDrawable(R.drawable.ic_person))
                            }
                            layout.setOnClickListener {
                                TeamDetailsActivity.start(context, item.id)
                            }
                        }
                    }
                    deleteIcon.setOnClickListener {
                        (viewModel as FavouritesViewModel).removeFromFavourites(context, item.id)
                        removeItem(position)
                    }
                }
            }
        }
    }

    fun removeItem(position: Int) {
        array.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
        when (viewModel) {
            is FavouritesViewModel -> viewModel.getFavourites(context)
            is SearchActivityViewModel -> viewModel.getRecentSearches(context)
        }
    }

    override fun getItemCount() = array.size

}
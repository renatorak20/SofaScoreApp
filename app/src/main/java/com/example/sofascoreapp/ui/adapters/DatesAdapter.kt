package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.MatchDate
import com.example.sofascoreapp.data.model.SportType
import com.example.sofascoreapp.databinding.DateListItemBinding
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.SharedViewModel


class DatesAdapter(
    val context: Context,
    val array: MutableList<MatchDate>,
    val viewModel: SharedViewModel,
    val sportType: SportType
) :
    RecyclerView.Adapter<DatesAdapter.MatchDateViewHolder>() {


    class MatchDateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DateListItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchDateViewHolder {
        return MatchDateViewHolder(
            LayoutInflater.from(context).inflate(R.layout.date_list_item, parent, false)
        )
    }

    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: MatchDateViewHolder, position: Int) {

        val item = array[position]

        if (item.isSelected) {
            holder.binding.indicator.visibility = View.VISIBLE
        } else {
            holder.binding.indicator.visibility = View.INVISIBLE
        }

        holder.binding.dayInWeek.text = Utilities().getDayInWeek(item.date)
        holder.binding.date.text = Utilities().getAvailableDateShort(item.date)

        holder.binding.layout.setOnClickListener {
            when (sportType) {
                SportType.FOOTBALL -> viewModel.setFootballDate(item.date)
                SportType.BASKETBALL -> viewModel.setBasketballDate(item.date)
                else -> viewModel.setAmerFootballDate(item.date)
            }

        }

    }
}
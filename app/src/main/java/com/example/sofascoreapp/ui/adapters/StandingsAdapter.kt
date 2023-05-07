package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sofascoreapp.data.model.StandingRow
import com.example.sofascoreapp.databinding.StandingsAmericanLayoutItemBinding
import com.example.sofascoreapp.databinding.StandingsBasketballItemBinding
import com.example.sofascoreapp.databinding.StandingsLayoutItemBinding
import java.text.DecimalFormat


private const val TYPE_FOOTBALL = 0
private const val TYPE_BASKETBALL = 1

class StandingsAdapter(
    val context: Context,
    val array: ArrayList<StandingRow>,
    val sport: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_FOOTBALL -> {
            FootballViewHolder(
                StandingsLayoutItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

        TYPE_BASKETBALL -> {
            BasketballViewHolder(
                StandingsBasketballItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

        else -> {
            AmericanFootballViewHolder(
                StandingsAmericanLayoutItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

    }

    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is FootballViewHolder -> holder.bind(array[position], position)
            is BasketballViewHolder -> holder.bind(array[position], position)
            else -> (holder as AmericanFootballViewHolder).bind(array[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (sport) {
            TYPE_FOOTBALL -> 0
            TYPE_BASKETBALL -> 1
            else -> 2
        }
    }

    class FootballViewHolder(
        private val binding: StandingsLayoutItemBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(row: StandingRow, position: Int) {

            binding.no.text = (position + 1).toString()
            binding.teamName.text = row.team.name

            binding.played.text = row.played.toString()
            binding.wins.text = row.wins.toString()
            binding.draws.text = row.draws.toString()
            binding.loses.text = row.losses.toString()
            binding.goals.text = row.scoresFor.toString()
            binding.points.text = row.points.toString()
        }
    }

    class BasketballViewHolder(
        private val binding: StandingsBasketballItemBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: StandingRow, position: Int) {

            binding.no.text = (position + 1).toString()
            binding.teamName.text = row.team.name

            binding.played.text = row.played.toString()
            binding.wins.text = row.wins.toString()
            binding.loses.text = row.losses.toString()

            val df = DecimalFormat("#.##")
            binding.points.text = df.format(row.percentage).toString()

            binding.diff.text = (row.scoresFor - row.scoresAgainst).toString()

        }
    }

    class AmericanFootballViewHolder(
        private val binding: StandingsAmericanLayoutItemBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: StandingRow, position: Int) {

            binding.no.text = (position + 1).toString()
            binding.teamName.text = row.team.name

            binding.played.text = row.played.toString()
            binding.wins.text = row.wins.toString()
            binding.draws.text = row.draws.toString()
            binding.loses.text = row.losses.toString()

            val df = DecimalFormat("#.##")
            binding.points.text = df.format(row.percentage).toString()

        }
    }

}
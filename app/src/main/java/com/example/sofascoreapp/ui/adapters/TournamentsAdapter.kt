package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sofascoreapp.R
import com.example.sofascoreapp.TournamentActivity
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.databinding.TournamentListItemBinding
import com.example.sofascoreapp.utils.Utilities.Companion.loadImage


class TournamentsAdapter(
    val context: Context,
    val array: ArrayList<Tournament>
) :
    RecyclerView.Adapter<TournamentsAdapter.TournamentViewHolder>() {


    class TournamentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = TournamentListItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        return TournamentViewHolder(
            LayoutInflater.from(context).inflate(R.layout.tournament_list_item, parent, false)
        )
    }

    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {

        val item = array[position]

        holder.binding.tournamentIcon.loadImage(context, DataType.TOURNAMENT, item.id)
        holder.binding.tournamentName.text = item.name

        holder.binding.layout.setOnClickListener {
            TournamentActivity.start(context, item)
        }

    }
}
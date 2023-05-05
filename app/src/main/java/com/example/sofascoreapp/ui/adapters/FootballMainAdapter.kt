package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.networking.Network
import com.example.sofascoreapp.databinding.FragmentFootballBinding
import com.example.sofascoreapp.databinding.MatchListItemBinding
import com.example.sofascoreapp.utils.Utilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait

class FootballMainAdapter(
    val context: Context,
    val array: ArrayList<Event>
) :
    RecyclerView.Adapter<FootballMainAdapter.FootballViewHolder>() {


    class FootballViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = MatchListItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FootballViewHolder {
        return FootballViewHolder(
            LayoutInflater.from(context).inflate(R.layout.match_list_item, parent, false)
        )
    }

    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: FootballViewHolder, position: Int) {

        val item = array[position]

        holder.binding.timeLayout.timeOfMatch.text = item.startDate?.let {
            Utilities().getMatchHour(
                it
            )
        }

        holder.binding.homeTeamLayout.teamName.text = item.homeTeam.name
        holder.binding.awayTeamLayout.teamName.text = item.awayTeam.name

        holder.binding.homeScore.text = item.homeScore.toString()
        holder.binding.awayScore.text = item.awayScore.toString()


    }
}
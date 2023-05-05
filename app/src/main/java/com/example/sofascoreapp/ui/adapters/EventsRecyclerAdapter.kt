package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.BasketballEvent
import com.example.sofascoreapp.data.model.FootballEvent
import com.example.sofascoreapp.data.model.EventStatusEnum
import com.example.sofascoreapp.databinding.MatchListItemBinding
import com.example.sofascoreapp.utils.Utilities

class EventsRecyclerAdapter(
    val context: Context,
    val array: ArrayList<Any>
) :
    RecyclerView.Adapter<EventsRecyclerAdapter.FootballViewHolder>() {


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

        when (item) {
            is FootballEvent -> {
                holder.binding.timeLayout.timeOfMatch.text = item.startDate?.let {
                    Utilities().getMatchHour(
                        it
                    )
                }

                holder.binding.homeTeamLayout.teamName.text = item.homeTeam.name
                holder.binding.awayTeamLayout.teamName.text = item.awayTeam.name


                when (item.status) {
                    EventStatusEnum.INPROGRESS -> {
                        holder.binding.homeScore.text = item.homeScore.total.toString()
                        holder.binding.awayScore.text = item.awayScore.total.toString()
                    }

                    EventStatusEnum.FINISHED -> {
                        holder.binding.homeScore.text = item.homeScore.total.toString()
                        holder.binding.awayScore.text = item.awayScore.total.toString()
                    }

                    else -> {
                        holder.binding.timeLayout.currentMinute.text = "-"
                    }
                }

                holder.binding.homeTeamLayout.clubIcon.load(
                    context.getString(
                        R.string.team_icon_url,
                        item.homeTeam.id
                    )
                )
                holder.binding.awayTeamLayout.clubIcon.load(
                    context.getString(
                        R.string.team_icon_url,
                        item.awayTeam.id
                    )
                )
            }

            is BasketballEvent -> {
                holder.binding.timeLayout.timeOfMatch.text = item.startDate?.let {
                    Utilities().getMatchHour(
                        it
                    )
                }

                holder.binding.homeTeamLayout.teamName.text = item.homeTeam.name
                holder.binding.awayTeamLayout.teamName.text = item.awayTeam.name


                when (item.status) {
                    EventStatusEnum.INPROGRESS -> {
                        holder.binding.homeScore.text = item.homeScore.toString()
                        holder.binding.awayScore.text = item.awayScore.toString()
                    }

                    EventStatusEnum.FINISHED -> {
                        holder.binding.homeScore.text = item.homeScore.toString()
                        holder.binding.awayScore.text = item.awayScore.toString()
                    }

                    else -> {
                        holder.binding.timeLayout.currentMinute.text = "-"
                    }
                }

                holder.binding.homeTeamLayout.clubIcon.load(
                    context.getString(
                        R.string.team_icon_url,
                        item.homeTeam.id
                    )
                )
                holder.binding.awayTeamLayout.clubIcon.load(
                    context.getString(
                        R.string.team_icon_url,
                        item.awayTeam.id
                    )
                )
            }
        }


    }
}
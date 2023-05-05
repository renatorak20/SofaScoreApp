package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.Country
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.EventStatusEnum
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.model.WinnerCode
import com.example.sofascoreapp.databinding.MatchListItemBinding
import com.example.sofascoreapp.databinding.MatchListLeagueSectionBinding
import com.example.sofascoreapp.utils.Utilities
import java.lang.IllegalArgumentException

private const val TYPE_SECTION = 0
private const val TYPE_MATCH = 1

class EventsRecyclerAdapter(
    val context: Context,
    val array: ArrayList<Any>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int) = when (array[position]) {
        is Event -> TYPE_MATCH
        is Tournament -> TYPE_SECTION
        else -> throw IllegalArgumentException()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_MATCH -> {
            MatchViewHolder(
                MatchListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

        else -> {
            SectionViewHolder(
                MatchListLeagueSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

    }

    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MatchViewHolder) {
            holder.bind(array[position] as Event)
        } else if (holder is SectionViewHolder) {
            holder.bind(array[position] as Tournament)
        }

    }

    class MatchViewHolder(private val binding: MatchListItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {

            binding.timeLayout.timeOfMatch.text =
                event.startDate?.let { Utilities().getMatchHour(it) }

            binding.homeTeamLayout.teamName.text = event.homeTeam.name
            binding.awayTeamLayout.teamName.text = event.awayTeam.name


            when (event.status) {
                EventStatusEnum.INPROGRESS -> {
                    binding.homeScore.text = event.homeScore.total.toString()
                    binding.awayScore.text = event.awayScore.total.toString()
                }

                EventStatusEnum.FINISHED -> {
                    binding.homeScore.text = event.homeScore.total.toString()
                    binding.awayScore.text = event.awayScore.total.toString()

                    when (event.winnerCode) {
                        WinnerCode.HOME -> {
                            binding.homeTeamLayout.teamName.setTextColor(context.getColor(R.color.on_surface_on_surface_lv_1))
                            binding.homeScore.setTextColor(context.getColor(R.color.on_surface_on_surface_lv_1))
                        }

                        WinnerCode.AWAY -> {
                            binding.awayTeamLayout.teamName.setTextColor(context.getColor(R.color.on_surface_on_surface_lv_1))
                            binding.awayScore.setTextColor(context.getColor(R.color.on_surface_on_surface_lv_1))
                        }

                        else -> {}
                    }

                }

                else -> {
                    binding.timeLayout.currentMinute.text = "-"
                }
            }

            binding.homeTeamLayout.clubIcon.load(
                context.getString(
                    R.string.team_icon_url,
                    event.homeTeam.id
                )
            )
            binding.awayTeamLayout.clubIcon.load(
                context.getString(
                    R.string.team_icon_url,
                    event.awayTeam.id
                )
            )
        }

    }

    class SectionViewHolder(
        private val binding: MatchListLeagueSectionBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tournament: Tournament) {
            binding.country.text = tournament.country.name
            binding.league.text = tournament.name
            binding.leagueIcon.load(context.getString(R.string.tournament_icon_url, tournament.id))
        }
    }
}
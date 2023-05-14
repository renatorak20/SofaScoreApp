package com.example.sofascoreapp.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.util.Util
import com.example.sofascoreapp.MatchDetailActivity
import com.example.sofascoreapp.R
import com.example.sofascoreapp.TournamentActivity
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.EventStatusEnum
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.model.WinnerCode
import com.example.sofascoreapp.databinding.MatchListItemBinding
import com.example.sofascoreapp.databinding.MatchListLeagueSectionBinding
import com.example.sofascoreapp.databinding.TeamMemberLayoutBinding
import com.example.sofascoreapp.databinding.TeamMemberSectionBinding
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import java.lang.IllegalArgumentException

private const val VIEW_TYPE_SECTION = 0
private const val VIEW_TYPE_MATCH = 1

class EventsRecyclerAdapter(
    val activity: Activity,
    val context: Context,
    val array: ArrayList<Any>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (array[position]) {
            is Event -> VIEW_TYPE_MATCH
            is Tournament -> VIEW_TYPE_SECTION
            else -> throw IllegalArgumentException()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_MATCH -> {
                MatchViewHolder(
                    MatchListItemBinding.inflate(
                        inflater,
                        parent,
                        false
                    ), context
                )
            }

            VIEW_TYPE_SECTION -> {
                SectionViewHolder(
                    MatchListLeagueSectionBinding.inflate(
                        inflater,
                        parent,
                        false
                    ), context
                )
            }

            else -> throw IllegalArgumentException()
        }
    }


    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is MatchViewHolder -> holder.bind(array[position] as Event)
            is SectionViewHolder -> holder.bind(array[position] as Tournament)
        }

    }

    inner class MatchViewHolder(private val binding: MatchListItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            with(binding) {

                homeTeamLayout.teamName.text = event.homeTeam.name
                awayTeamLayout.teamName.text = event.awayTeam.name

                when (event.status) {
                    EventStatusEnum.INPROGRESS -> {
                        homeScore.text = event.homeScore.total.toString()
                        awayScore.text = event.awayScore.total.toString()
                    }

                    EventStatusEnum.FINISHED -> {
                        homeScore.text = event.homeScore.total.toString()
                        awayScore.text = event.awayScore.total.toString()

                        val typedValue = TypedValue()
                        context.theme.resolveAttribute(
                            R.attr.on_surface_on_surface_lv_1,
                            typedValue,
                            true
                        )
                        val teamColor = ContextCompat.getColor(context, typedValue.resourceId)

                        context.theme.resolveAttribute(
                            R.attr.on_surface_on_surface_lv_1,
                            typedValue,
                            true
                        )
                        val scoreColor = ContextCompat.getColor(context, typedValue.resourceId)

                        when (event.winnerCode) {
                            WinnerCode.HOME -> {
                                homeTeamLayout.teamName.setTextColor(teamColor)
                                homeScore.setTextColor(scoreColor)
                            }

                            WinnerCode.AWAY -> {

                                awayTeamLayout.teamName.setTextColor(teamColor)
                                awayScore.setTextColor(scoreColor)
                            }

                            else -> {}
                        }

                        timeLayout.currentMinute.text = context.getString(R.string.ft)
                        if (Preferences(activity).getSavedDateFormat()) {
                            timeLayout.timeOfMatch.text =
                                Utilities().getAvailableDateShort(event.startDate!!)
                        } else {
                            timeLayout.timeOfMatch.text =
                                Utilities().getInvertedAvailableDateShort(event.startDate!!)
                        }
                    }

                    else -> {
                        if (Utilities().isToday(event.startDate!!)) {
                            timeLayout.currentMinute.text = "-"
                            timeLayout.timeOfMatch.text =
                                event.startDate.let { Utilities().getMatchHour(it) }
                        } else if (Utilities().isTomorrow(event.startDate)) {
                            timeLayout.currentMinute.text =
                                event.startDate.let { Utilities().getMatchHour(it) }
                            timeLayout.timeOfMatch.text = context.getString(R.string.tomorrow)
                        } else {

                            timeLayout.currentMinute.text =
                                event.startDate.let { Utilities().getMatchHour(it) }

                            if (Preferences(activity).getSavedDateFormat()) {
                                timeLayout.timeOfMatch.text =
                                    Utilities().getAvailableDateShort(event.startDate)
                            } else {
                                timeLayout.timeOfMatch.text =
                                    Utilities().getInvertedAvailableDateShort(event.startDate)
                            }
                        }
                    }
                }

                homeTeamLayout.clubIcon.load(
                    context.getString(
                        R.string.team_icon_url,
                        event.homeTeam.id
                    )
                )
                awayTeamLayout.clubIcon.load(
                    context.getString(
                        R.string.team_icon_url,
                        event.awayTeam.id
                    )
                )

                layout.setOnClickListener {
                    val intent = Intent(context, MatchDetailActivity::class.java)
                    intent.putExtra("matchID", event.id)
                    context.startActivity(intent)
                }
            }

        }

    }

    class SectionViewHolder(
        private val binding: MatchListLeagueSectionBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tournament: Tournament) {
            with(binding) {
                country.text = tournament.country.name
                league.text = tournament.name
                leagueIcon.load(context.getString(R.string.tournament_icon_url, tournament.id))

                layout.setOnClickListener {
                    context.startActivity(
                        Intent(
                            context,
                            TournamentActivity::class.java
                        ).putExtra("tournamentID", tournament.id)
                    )
                }

            }
        }
    }
}
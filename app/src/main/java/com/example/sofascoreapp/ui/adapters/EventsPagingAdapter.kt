package com.example.sofascoreapp.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sofascoreapp.MatchDetailActivity
import com.example.sofascoreapp.R
import com.example.sofascoreapp.TournamentActivity
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.EventStatusEnum
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.model.WinnerCode
import com.example.sofascoreapp.databinding.MatchListItemBinding
import com.example.sofascoreapp.databinding.MatchListLeagueSectionBinding
import com.example.sofascoreapp.databinding.RoundSectionBinding
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import java.lang.IllegalArgumentException

private const val VIEW_TYPE_SECTION = 0
private const val VIEW_TYPE_MATCH = 1

class EventsPagingAdapter(
    val activity: Activity,
    val context: Context,
    private val sectionType: Int
) :
    PagingDataAdapter<Any, RecyclerView.ViewHolder>(EventsDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Event -> VIEW_TYPE_MATCH
            is Tournament -> VIEW_TYPE_SECTION
            is Int -> VIEW_TYPE_SECTION
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
                when (sectionType) {
                    0 -> SectionViewHolder(
                        MatchListLeagueSectionBinding.inflate(
                            inflater,
                            parent,
                            false
                        ), context
                    )

                    else -> RoundViewHolder(
                        RoundSectionBinding.inflate(
                            inflater,
                            parent,
                            false
                        ),
                        context
                    )
                }
            }

            else -> throw IllegalArgumentException()
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        when (holder) {
            is MatchViewHolder -> holder.bind(getItem(position) as Event)
            is SectionViewHolder -> holder.bind(getItem(position) as Tournament)
            is RoundViewHolder -> holder.bind(getItem(position) as Int)
        }
    }

    inner class MatchViewHolder(private val binding: MatchListItemBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            with(binding) {
                timeLayout.timeOfMatch.text =
                    event.startDate?.let { Utilities().getMatchHour(it) }

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
                        );
                        val teamColor = ContextCompat.getColor(context, typedValue.resourceId)

                        context.theme.resolveAttribute(
                            R.attr.on_surface_on_surface_lv_1,
                            typedValue,
                            true
                        );
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

                    }

                    else -> {
                        if (Utilities().isTomorrow(event.startDate!!)) {

                        } else if (Utilities().isToday(event.startDate)) {
                            if (Preferences(activity).getSavedDateFormat()) {
                            } else {
                                timeLayout.timeOfMatch.text =
                                    Utilities().getInvertedAvailableDateShort(event.startDate)
                                timeLayout.currentMinute.text =
                                    Utilities().getMatchHour(event.startDate)
                            }
                        }
                        when (decideDate(event.startDate)) {
                            0 -> {
                                timeLayout.timeOfMatch.text = context.getString(R.string.today)
                                timeLayout.currentMinute.text =
                                    Utilities().getMatchHour(event.startDate)
                            }

                            1 -> {
                                timeLayout.timeOfMatch.text = context.getString(R.string.tomorrow)
                                timeLayout.currentMinute.text =
                                    Utilities().getMatchHour(event.startDate)
                            }

                            else -> {
                                if (Preferences(activity).getSavedDateFormat()) {
                                    timeLayout.timeOfMatch.text =
                                        Utilities().getAvailableDateShort(event.startDate)
                                    timeLayout.currentMinute.text =
                                        Utilities().getMatchHour(event.startDate)
                                } else {
                                    timeLayout.timeOfMatch.text =
                                        Utilities().getInvertedAvailableDateShort(event.startDate)
                                    timeLayout.currentMinute.text =
                                        Utilities().getMatchHour(event.startDate)
                                }
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

    class RoundViewHolder(
        private val binding: RoundSectionBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(roundNumber: Int) {
            binding.round.text = context.getString(R.string.round_no, roundNumber)
        }
    }


    class EventsDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when (oldItem) {
                is Event -> oldItem.id == (newItem as Event).id
                is String -> (oldItem as String) == (newItem as String)
                else -> (oldItem as Int) == (newItem as Int)
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when (oldItem) {
                is Event -> (oldItem as Event) == (newItem as Event)
                is String -> (oldItem as String) == (newItem as String)
                else -> (oldItem as Int) == (newItem as Int)
            }
        }
    }

    fun decideDate(date: String): Int {
        return if (Utilities().isToday(date)) {
            0
        } else if (Utilities().isTomorrow(date)) {
            1
        } else {
            2
        }
    }
}
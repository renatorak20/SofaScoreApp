package com.example.sofascoreapp.ui.custom

import android.content.Context
import android.content.Intent
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sofascoreapp.MatchDetailActivity
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.EventStatusEnum
import com.example.sofascoreapp.data.model.UiModel
import com.example.sofascoreapp.data.model.WinnerCode
import com.example.sofascoreapp.databinding.MatchListItemBinding
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.utils.Utilities.Companion.loadImage
import kotlin.random.Random

class MatchViewHolder(private val binding: MatchListItemBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(event: UiModel.Event) {
        Preferences.initialize(context)
        resetFields(binding)

        with(binding) {

            homeTeamLayout.teamName.text = event.homeTeam.name
            awayTeamLayout.teamName.text = event.awayTeam.name

            when (event.status) {
                EventStatusEnum.INPROGRESS -> {
                    homeScore.text = event.homeScore.total.toString()
                    awayScore.text = event.awayScore.total.toString()

                    timeLayout.currentMinute.text =
                        context.getString(R.string.minute, Random(1000).nextInt(1, 30))
                    timeLayout.timeOfMatch.text =
                        event.startDate.let { Utilities().getMatchHour(it!!) }
                    Utilities().setMatchTint(
                        context,
                        2,
                        homeScore,
                        awayScore,
                        timeLayout.currentMinute
                    )
                }

                EventStatusEnum.FINISHED -> {
                    homeScore.text = event.homeScore.total.toString()
                    awayScore.text = event.awayScore.total.toString()

                    when (event.winnerCode) {
                        WinnerCode.HOME -> {
                            Utilities().setMatchTint(
                                context,
                                1,
                                homeTeamLayout.teamName,
                                homeScore
                            )
                        }

                        WinnerCode.AWAY -> {
                            Utilities().setMatchTint(
                                context,
                                1,
                                awayTeamLayout.teamName,
                                awayScore
                            )
                        }

                        else -> {}
                    }

                    timeLayout.currentMinute.text = context.getString(R.string.ft)
                    if (Preferences.getSavedDateFormat()) {
                        timeLayout.timeOfMatch.text =
                            Utilities().getAvailableDateShort(event.startDate!!)
                    } else {
                        timeLayout.timeOfMatch.text =
                            Utilities().getInvertedAvailableDateShort(event.startDate!!)
                    }
                }

                EventStatusEnum.NOTSTARTED -> {
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

                        if (Preferences.getSavedDateFormat()) {
                            timeLayout.timeOfMatch.text =
                                Utilities().getAvailableDateShort(event.startDate)
                        } else {
                            timeLayout.timeOfMatch.text =
                                Utilities().getInvertedAvailableDateShort(event.startDate)
                        }
                    }
                }
            }

            homeTeamLayout.clubIcon.loadImage(context, DataType.TEAM, event.homeTeam.id)
            awayTeamLayout.clubIcon.loadImage(context, DataType.TEAM, event.awayTeam.id)

            layout.setOnClickListener {
                MatchDetailActivity.start(context, event.id)
            }
        }
    }

    fun bind(event: Event) {

        Preferences.initialize(context)
        resetFields(binding)

        with(binding) {

            homeTeamLayout.teamName.text = event.homeTeam.name
            awayTeamLayout.teamName.text = event.awayTeam.name

            when (event.status) {
                EventStatusEnum.INPROGRESS -> {
                    homeScore.text = event.homeScore.total.toString()
                    awayScore.text = event.awayScore.total.toString()

                    timeLayout.currentMinute.text =
                        context.getString(R.string.minute, Random(1000).nextInt(1, 30))
                    timeLayout.timeOfMatch.text =
                        event.startDate.let { Utilities().getMatchHour(it!!) }
                    Utilities().setMatchTint(
                        context,
                        2,
                        homeScore,
                        awayScore,
                        timeLayout.currentMinute
                    )
                }

                EventStatusEnum.FINISHED -> {
                    homeScore.text = event.homeScore.total.toString()
                    awayScore.text = event.awayScore.total.toString()

                    when (event.winnerCode) {
                        WinnerCode.HOME -> {
                            Utilities().setMatchTint(
                                context,
                                1,
                                homeTeamLayout.teamName,
                                homeScore
                            )
                        }

                        WinnerCode.AWAY -> {
                            Utilities().setMatchTint(
                                context,
                                1,
                                awayTeamLayout.teamName,
                                awayScore
                            )
                        }

                        else -> {
                            if (event.homeScore.total!! > event.awayScore.total!!) {
                                Utilities().setMatchTint(
                                    context,
                                    1,
                                    homeTeamLayout.teamName,
                                    homeScore
                                )
                            } else if (event.awayScore.total > event.homeScore.total) {
                                Utilities().setMatchTint(
                                    context,
                                    1,
                                    awayTeamLayout.teamName,
                                    awayScore
                                )
                            }
                        }
                    }

                    timeLayout.currentMinute.text = context.getString(R.string.ft)
                    if (Preferences.getSavedDateFormat()) {
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

                        if (Preferences.getSavedDateFormat()) {
                            timeLayout.timeOfMatch.text =
                                Utilities().getAvailableDateShort(event.startDate)
                        } else {
                            timeLayout.timeOfMatch.text =
                                Utilities().getInvertedAvailableDateShort(event.startDate)
                        }
                    }
                }
            }

            homeTeamLayout.clubIcon.loadImage(context, DataType.TEAM, event.homeTeam.id)
            awayTeamLayout.clubIcon.loadImage(context, DataType.TEAM, event.awayTeam.id)

            layout.setOnClickListener {
                MatchDetailActivity.start(context, event.id)
            }
        }
    }


    fun resetFields(binding: MatchListItemBinding) {
        with(binding) {
            homeScore.text = ""
            awayScore.text = ""
            timeLayout.currentMinute.text = ""
            timeLayout.timeOfMatch.text = ""

            Utilities().setMatchTint(
                context,
                0,
                homeTeamLayout.teamName,
                homeScore,
                awayTeamLayout.teamName,
                awayScore
            )

        }
    }

}
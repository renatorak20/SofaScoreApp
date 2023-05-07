package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.CardTeamSideEnum
import com.example.sofascoreapp.data.model.Incident
import com.example.sofascoreapp.data.model.IncidentEnum
import com.example.sofascoreapp.databinding.MatchCardIncidentBinding
import com.example.sofascoreapp.databinding.MatchGoalIncidentHomeBinding
import com.example.sofascoreapp.databinding.PeriodLayoutBinding
import com.example.sofascoreapp.utils.Utilities

private const val TYPE_PERIOD = 0
private const val TYPE_GOAL = 1
private const val TYPE_CARD = 2

class MatchIncidentsAdapter(
    val context: Context,
    val array: ArrayList<Incident>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_PERIOD -> {
            PeriodViewHolder(
                PeriodLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

        TYPE_GOAL -> {
            GoalViewHolder(
                MatchGoalIncidentHomeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

        else -> {
            CardViewHolder(
                MatchCardIncidentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }


    }

    override fun getItemViewType(position: Int): Int {
        val incident = array[position]
        return when (incident.type) {
            IncidentEnum.CARD -> TYPE_CARD
            IncidentEnum.PERIOD -> TYPE_PERIOD
            else -> TYPE_GOAL
        }
    }

    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is PeriodViewHolder -> holder.bind(array[position])
            is GoalViewHolder -> holder.bind(array[position])
            is CardViewHolder -> holder.bind(array[position])
        }

    }

    class PeriodViewHolder(private val binding: PeriodLayoutBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(incident: Incident) {
            binding.periodText.text = incident.text
        }
    }

    class GoalViewHolder(private val binding: MatchGoalIncidentHomeBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(incident: Incident) {
            if (incident.teamSide == CardTeamSideEnum.HOME) {
                binding.minute.text = context.getString(R.string.minute, incident.time)
                binding.newResult.text =
                    context.getString(R.string.result, incident.homeScore, incident.awayScore)
                binding.playerName.text = incident.player?.name
                Utilities().setImageGoal(binding.goalIcon, incident)
            } else {
                Utilities().toggleHomeAwayGoalLayout(binding, incident, context)
            }
        }
    }


    class CardViewHolder(private val binding: MatchCardIncidentBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(incident: Incident) {
            if (incident.teamSide == CardTeamSideEnum.HOME) {
                binding.minute.text = context.getString(R.string.minute, incident.time)
                binding.playerName.text = incident.player?.name
                Utilities().setImageGoal(binding.cardIcon, incident)
            } else {
                Utilities().toggleHomeAwayCardLayout(binding, incident, context)
            }
        }

    }

}
package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.sofascoreapp.PlayerDetailsActivity
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.CardColorEnum
import com.example.sofascoreapp.data.model.CardTeamSideEnum
import com.example.sofascoreapp.data.model.GoalScoringTeamEnum
import com.example.sofascoreapp.data.model.GoalTypeEnum
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
            is PeriodViewHolder -> holder.bind(array[position], holder)
            is GoalViewHolder -> holder.bind(array[position], holder)
            is CardViewHolder -> holder.bind(array[position], holder)
        }
    }

    inner class PeriodViewHolder(private val binding: PeriodLayoutBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(incident: Incident, holder: PeriodViewHolder) {
            holder.binding.periodText.text = incident.text
        }
    }

    inner class GoalViewHolder(
        private val binding: MatchGoalIncidentHomeBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(incident: Incident, holder: GoalViewHolder) {
            if (incident.scoringTeam == GoalScoringTeamEnum.HOME) {
                holder.binding.minute.text = context.getString(R.string.minute, incident.time)
                holder.binding.newResult.text =
                    context.getString(R.string.result, incident.homeScore, incident.awayScore)
                holder.binding.playerName.text = incident.player?.name
                setImageGoal(holder.binding.goalIcon, incident)
            } else {
                toggleHomeAwayGoalLayout(binding)
                holder.binding.minuteAway.text = context.getString(R.string.minute, incident.time)
                holder.binding.playerNameAway.text = incident.player?.name
                holder.binding.newResultAway.text =
                    context.getString(R.string.result, incident.homeScore, incident.awayScore)
                setImageGoal(holder.binding.goalIconAway, incident)
            }

            holder.binding.layout.setOnClickListener {
                val intent = Intent(context, PlayerDetailsActivity::class.java)
                intent.putExtra("playerID", incident.player?.id)
                context.startActivity(intent)
            }
        }
    }

    inner class CardViewHolder(
        private val binding: MatchCardIncidentBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(incident: Incident, holder: CardViewHolder) {
            if (incident.teamSide == CardTeamSideEnum.HOME) {
                holder.binding.minute.text = context.getString(R.string.minute, incident.time)
                holder.binding.playerName.text = incident.player?.name
                setImageCard(holder.binding.cardIcon, incident)
            } else {
                toggleHomeAwayCardLayout(binding)
                holder.binding.minuteAway.text = context.getString(R.string.minute, incident.time)
                holder.binding.playerNameAway.text = incident.player?.name
                setImageCard(holder.binding.cardIconAway, incident)
            }

            holder.binding.layout.setOnClickListener {
                val intent = Intent(context, PlayerDetailsActivity::class.java)
                intent.putExtra("playerID", incident.player?.id)
                context.startActivity(intent)
            }
        }
    }

    private fun setImageGoal(imageView: ImageView, incident: Incident) {
        when (incident.goalType) {
            GoalTypeEnum.REGULAR -> imageView.setImageResource(R.drawable.icon_goal)
            GoalTypeEnum.PENALTY -> imageView.setImageResource(R.drawable.ic_penalty_score)
            else -> imageView.setImageResource(R.drawable.ic_autogoal)
        }
    }

    private fun setImageCard(imageView: ImageView, incident: Incident) {
        when (incident.color) {
            CardColorEnum.YELLOW -> imageView.setImageResource(R.drawable.ic_yellow_card)
            else -> imageView.setImageResource(R.drawable.ic_red_card)
        }
    }

    private fun toggleHomeAwayGoalLayout(
        binding: MatchGoalIncidentHomeBinding
    ) {
        binding.playerName.visibility = View.GONE
        binding.newResult.visibility = View.GONE
        binding.minute.visibility = View.GONE
        binding.goalIcon.visibility = View.GONE

        binding.playerNameAway.visibility = View.VISIBLE
        binding.newResultAway.visibility = View.VISIBLE
        binding.minuteAway.visibility = View.VISIBLE
        binding.goalIconAway.visibility = View.VISIBLE
    }

    private fun toggleHomeAwayCardLayout(
        binding: MatchCardIncidentBinding
    ) {
        binding.playerName.visibility = View.GONE
        binding.cardIcon.visibility = View.GONE
        binding.minute.visibility = View.GONE
        binding.reason.visibility = View.GONE

        binding.playerNameAway.visibility = View.VISIBLE
        binding.cardIconAway.visibility = View.VISIBLE
        binding.minuteAway.visibility = View.VISIBLE
        binding.reasonAway.visibility = View.VISIBLE
    }
}


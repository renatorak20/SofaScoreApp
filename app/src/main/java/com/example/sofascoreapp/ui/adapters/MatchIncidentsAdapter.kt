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
import com.example.sofascoreapp.data.model.SportType
import com.example.sofascoreapp.databinding.MatchCardIncidentBinding
import com.example.sofascoreapp.databinding.MatchGoalIncidentHomeBinding
import com.example.sofascoreapp.databinding.MatchHoopItemBinding
import com.example.sofascoreapp.databinding.PeriodLayoutBinding

class MatchIncidentsAdapter(
    val context: Context,
    val array: ArrayList<Incident>,
    val sport: SportType
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_PERIOD = 0
        private const val TYPE_GOAL = 1
        private const val TYPE_CARD = 2
    }

    override fun getItemViewType(position: Int): Int {
        val incident = array[position]
        return when (incident.type) {
            IncidentEnum.CARD -> TYPE_CARD
            IncidentEnum.PERIOD -> TYPE_PERIOD
            IncidentEnum.GOAL -> TYPE_GOAL
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_PERIOD -> {
                PeriodViewHolder(
                    PeriodLayoutBinding.inflate(
                        inflater,
                        parent,
                        false
                    ), context
                )
            }

            TYPE_GOAL -> {
                when (sport) {

                    SportType.BASKETBALL -> {
                        GoalBasketballViewHolder(
                            MatchHoopItemBinding.inflate(
                                inflater,
                                parent,
                                false
                            ), context
                        )
                    }

                    else -> {
                        GoalViewHolder(
                            MatchGoalIncidentHomeBinding.inflate(
                                inflater,
                                parent,
                                false
                            ), context
                        )
                    }


                }
            }

            else -> {
                CardViewHolder(
                    MatchCardIncidentBinding.inflate(
                        inflater,
                        parent,
                        false
                    ), context
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        when (holder) {
            is PeriodViewHolder -> holder.bind(array[position])
            is GoalViewHolder -> holder.bind(array[position])
            is CardViewHolder -> holder.bind(array[position])
            else -> (holder as GoalBasketballViewHolder).bind(array[position])
        }
    }


    override fun getItemCount() = array.size


    inner class PeriodViewHolder(private val binding: PeriodLayoutBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(incident: Incident) {
            binding.periodText.text = incident.text
        }
    }

    inner class GoalViewHolder(
        private val binding: MatchGoalIncidentHomeBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(incident: Incident) {
            with(binding) {
                if (incident.scoringTeam == GoalScoringTeamEnum.HOME) {
                    minute.text = context.getString(R.string.minute, incident.time)
                    newResult.text =
                        context.getString(R.string.result, incident.homeScore, incident.awayScore)
                    playerName.text = incident.player?.name
                    setImageGoal(goalIcon, incident, sport)
                } else {
                    toggleHomeAwayGoalFootballLayout(binding)
                    minuteAway.text = context.getString(R.string.minute, incident.time)
                    playerNameAway.text = incident.player?.name
                    newResultAway.text =
                        context.getString(R.string.result, incident.homeScore, incident.awayScore)
                    setImageGoal(goalIconAway, incident, sport)
                }

                layout.setOnClickListener {
                    val intent = Intent(context, PlayerDetailsActivity::class.java)
                    intent.putExtra("playerID", incident.player?.id)
                    context.startActivity(intent)
                }
            }
        }
    }

    inner class GoalBasketballViewHolder(
        private val binding: MatchHoopItemBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(incident: Incident) {
            with(binding) {

                minute.text = context.getString(R.string.minute, incident.time)

                if (incident.scoringTeam == GoalScoringTeamEnum.AWAY) {
                    newResultAway.text =
                        context.getString(R.string.result, incident.homeScore, incident.awayScore)
                    toggleHomeAwayGoalBasketballLayoutLayout(binding)
                    setImageHoop(goalIconAway, incident)
                } else {
                    newResult.text =
                        context.getString(R.string.result, incident.homeScore, incident.awayScore)
                    setImageHoop(goalIcon, incident)
                }

                layout.setOnClickListener {
                    val intent = Intent(context, PlayerDetailsActivity::class.java)
                    intent.putExtra("playerID", incident.player?.id)
                    context.startActivity(intent)
                }
            }
        }
    }

    inner class CardViewHolder(
        private val binding: MatchCardIncidentBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(incident: Incident) {
            with(binding) {
                if (incident.teamSide == CardTeamSideEnum.HOME) {
                    minute.text = context.getString(R.string.minute, incident.time)
                    playerName.text = incident.player?.name
                    setImageCard(cardIcon, incident)
                } else {
                    toggleHomeAwayCardLayout(binding)
                    minuteAway.text = context.getString(R.string.minute, incident.time)
                    playerNameAway.text = incident.player?.name
                    setImageCard(cardIconAway, incident)
                }

                layout.setOnClickListener {
                    val intent = Intent(context, PlayerDetailsActivity::class.java)
                    intent.putExtra("playerID", incident.player?.id)
                    context.startActivity(intent)
                }
            }
        }
    }

    private fun setImageGoal(imageView: ImageView, incident: Incident, sport: SportType) {

        when (sport) {
            SportType.FOOTBALL -> {
                when (incident.goalType) {
                    GoalTypeEnum.REGULAR -> imageView.setImageResource(R.drawable.icon_goal)
                    GoalTypeEnum.PENALTY -> imageView.setImageResource(R.drawable.ic_penalty_score)
                    else -> imageView.setImageResource(R.drawable.ic_autogoal)
                }
            }

            else -> {
                when (incident.goalType) {
                    GoalTypeEnum.TOUCHDOWN -> imageView.setImageResource(R.drawable.ic_touchdown)
                    GoalTypeEnum.EXTRAPOINT -> imageView.setImageResource(R.drawable.ic_extra_point)
                    else -> imageView.setImageResource(R.drawable.ic_field_goal)
                }
            }
        }


    }

    private fun setImageHoop(imageView: ImageView, incident: Incident) {
        when (incident.goalType) {
            GoalTypeEnum.ONEPOINT -> imageView.setImageResource(R.drawable.ic_hoop_one)
            GoalTypeEnum.TWOPOINT -> imageView.setImageResource(R.drawable.ic_hoop_two)
            else -> imageView.setImageResource(R.drawable.ic_hoop_three)
        }
    }

    private fun setImageCard(imageView: ImageView, incident: Incident) {
        when (incident.color) {
            CardColorEnum.YELLOW -> imageView.setImageResource(R.drawable.ic_yellow_card)
            else -> imageView.setImageResource(R.drawable.ic_red_card)
        }
    }

    private fun toggleHomeAwayGoalFootballLayout(
        binding: MatchGoalIncidentHomeBinding
    ) {
        with(binding) {
            playerName.visibility = View.GONE
            newResult.visibility = View.GONE
            minute.visibility = View.GONE
            goalIcon.visibility = View.GONE

            playerNameAway.visibility = View.VISIBLE
            newResultAway.visibility = View.VISIBLE
            minuteAway.visibility = View.VISIBLE
            goalIconAway.visibility = View.VISIBLE
        }
    }

    private fun toggleHomeAwayCardLayout(
        binding: MatchCardIncidentBinding
    ) {
        with(binding) {
            playerName.visibility = View.GONE
            cardIcon.visibility = View.GONE
            minute.visibility = View.GONE
            reason.visibility = View.GONE

            playerNameAway.visibility = View.VISIBLE
            cardIconAway.visibility = View.VISIBLE
            minuteAway.visibility = View.VISIBLE
            reasonAway.visibility = View.VISIBLE
        }
    }

    private fun toggleHomeAwayGoalBasketballLayoutLayout(
        binding: MatchHoopItemBinding
    ) {
        with(binding) {
            goalIcon.visibility = View.GONE
            newResult.visibility = View.GONE

            goalIconAway.visibility = View.VISIBLE
            newResultAway.visibility = View.VISIBLE
        }
    }


}


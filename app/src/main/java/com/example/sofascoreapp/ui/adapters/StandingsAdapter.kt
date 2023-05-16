package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.sofascoreapp.R
import com.example.sofascoreapp.TeamDetailsActivity
import com.example.sofascoreapp.data.model.StandingRow
import com.example.sofascoreapp.data.model.Team2
import com.example.sofascoreapp.databinding.StandingsAmericanLayoutItemBinding
import com.example.sofascoreapp.databinding.StandingsBasketballItemBinding
import com.example.sofascoreapp.databinding.StandingsLayoutItemBinding
import com.example.sofascoreapp.utils.Utilities
import java.text.DecimalFormat


private const val TYPE_FOOTBALL = "Football"
private const val TYPE_BASKETBALL = "Basketball"

class StandingsAdapter(
    val context: Context,
    val array: ArrayList<StandingRow>,
    val sport: String,
    val selectedTeamID: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        0 -> {
            FootballViewHolder(
                StandingsLayoutItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

        1 -> {
            BasketballViewHolder(
                StandingsBasketballItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

        else -> {
            AmericanFootballViewHolder(
                StandingsAmericanLayoutItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

    }

    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is FootballViewHolder -> holder.bind(array[position], position)
            is BasketballViewHolder -> holder.bind(array[position], position)
            else -> (holder as AmericanFootballViewHolder).bind(array[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (sport) {
            TYPE_FOOTBALL -> 0
            TYPE_BASKETBALL -> 1
            else -> 2
        }
    }

    inner class FootballViewHolder(
        private val binding: StandingsLayoutItemBinding,
        val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(row: StandingRow, position: Int) {

            if (shouldSelectTeam(row.team.id)) {
                binding.layout.setBackgroundColor(
                    ColorUtils.setAlphaComponent(
                        context.getColor(R.color.status_success),
                        128
                    )
                );
            } else {
                binding.layout.setBackgroundColor(context.getColor(android.R.color.transparent));
            }

            Utilities().setRotatingText(binding.teamName)

            binding.no.text = (position + 1).toString()
            binding.teamName.text = row.team.name

            binding.played.text = row.played.toString()
            binding.wins.text = row.wins.toString()
            binding.draws.text = row.draws.toString()
            binding.loses.text = row.losses.toString()
            binding.goals.text = row.scoresFor.toString()
            binding.points.text = row.points.toString()

            binding.no.setOnClickListener {
                showToast(row.team)
            }

            binding.layout.setOnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        TeamDetailsActivity::class.java
                    ).putExtra("teamID", row.team.id)
                )
            }
        }
    }

    inner class BasketballViewHolder(
        private val binding: StandingsBasketballItemBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: StandingRow, position: Int) {

            if (shouldSelectTeam(row.team.id)) {
                binding.layout.setBackgroundColor(
                    ColorUtils.setAlphaComponent(
                        context.getColor(R.color.status_success),
                        128
                    )
                );
            } else {
                binding.layout.setBackgroundColor(context.getColor(android.R.color.transparent));
            }

            Utilities().setRotatingText(binding.teamName)

            binding.no.text = (position + 1).toString()
            binding.teamName.text = row.team.name

            binding.played.text = row.played.toString()
            binding.wins.text = row.wins.toString()
            binding.loses.text = row.losses.toString()

            val df = DecimalFormat("#.##")
            binding.points.text = df.format(row.percentage).toString()

            binding.diff.text = (row.scoresFor - row.scoresAgainst).toString()

            binding.no.setOnClickListener {
                showToast(row.team)
            }

            binding.layout.setOnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        TeamDetailsActivity::class.java
                    ).putExtra("teamID", row.team.id)
                )
            }

        }
    }

    inner class AmericanFootballViewHolder(
        private val binding: StandingsAmericanLayoutItemBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(row: StandingRow, position: Int) {

            if (shouldSelectTeam(row.team.id)) {
                binding.layout.setBackgroundColor(
                    ColorUtils.setAlphaComponent(
                        context.getColor(R.color.status_success),
                        128
                    )
                );
            } else {
                binding.layout.setBackgroundColor(context.getColor(android.R.color.transparent));
            }

            Utilities().setRotatingText(binding.teamName)

            binding.no.text = (position + 1).toString()
            binding.teamName.text = row.team.name

            binding.played.text = row.played.toString()
            binding.wins.text = row.wins.toString()
            binding.draws.text = row.draws.toString()
            binding.loses.text = row.losses.toString()

            if (row.percentage != null) {
                val df = DecimalFormat("#.##")
                binding.points.text = df.format(row.percentage).toString()
            }


            binding.no.setOnClickListener {
                showToast(row.team)
            }

            binding.layout.setOnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        TeamDetailsActivity::class.java
                    ).putExtra("teamID", row.team.id)
                )
            }

        }
    }

    fun shouldSelectTeam(teamID: Int) = teamID == selectedTeamID

    fun showToast(team: Team2) {
        val toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        val request = ImageRequest.Builder(context)
            .data(context.getString(R.string.team_icon_url, team.id))
            .target { drawable ->
                toast.view = ImageView(context).apply {
                    setImageDrawable(drawable)
                }
                toast.show()
            }
            .build()
        ImageLoader(context).enqueue(request)
    }

}
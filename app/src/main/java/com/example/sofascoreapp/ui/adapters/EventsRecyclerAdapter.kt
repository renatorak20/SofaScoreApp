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
import com.example.sofascoreapp.ui.custom.MatchViewHolder
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import java.lang.IllegalArgumentException

private const val VIEW_TYPE_SECTION = 0
private const val VIEW_TYPE_MATCH = 1

class EventsRecyclerAdapter(
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
                    TournamentActivity.start(context, tournament)
                }

            }
        }
    }
}
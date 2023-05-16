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
import com.example.sofascoreapp.data.model.UiModel
import com.example.sofascoreapp.data.model.WinnerCode
import com.example.sofascoreapp.databinding.MatchListItemBinding
import com.example.sofascoreapp.databinding.MatchListLeagueSectionBinding
import com.example.sofascoreapp.databinding.RoundSectionBinding
import com.example.sofascoreapp.ui.custom.MatchViewHolder
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import java.lang.IllegalArgumentException

private const val VIEW_TYPE_SECTION = 0
private const val VIEW_TYPE_MATCH = 1

class EventsPagingAdapter(
    val context: Context,
    private val sectionType: Int
) :
    PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(UiModelComparator) {

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

    class RoundViewHolder(
        private val binding: RoundSectionBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(roundNumber: Int) {
            binding.round.text = context.getString(R.string.round_no, roundNumber)
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

    object UiModelComparator : DiffUtil.ItemCallback<UiModel>() {
        override fun areItemsTheSame(
            oldItem: UiModel,
            newItem: UiModel
        ): Boolean {
            val isSameRepoItem = oldItem is UiModel.Event
                    && newItem is UiModel.Event
                    && oldItem.id == newItem.id

            val isSameSeparatorItem = oldItem is UiModel.SeparatorItem
                    && newItem is UiModel.SeparatorItem
                    && oldItem.description == newItem.description

            return isSameRepoItem || isSameSeparatorItem
        }

        override fun areContentsTheSame(
            oldItem: UiModel,
            newItem: UiModel
        ) = oldItem == newItem

    }
}
package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sofascoreapp.R
import com.example.sofascoreapp.TournamentActivity
import com.example.sofascoreapp.data.model.UiModel
import com.example.sofascoreapp.databinding.MatchListItemBinding
import com.example.sofascoreapp.databinding.MatchListLeagueSectionBinding
import com.example.sofascoreapp.databinding.RoundSectionBinding
import com.example.sofascoreapp.ui.custom.MatchViewHolder
import com.example.sofascoreapp.utils.Utilities.Companion.clear
import java.lang.IllegalArgumentException


class EventsPagingAdapter(
    val context: Context,
    private val sectionType: Int
) :
    PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(UiModelComparator) {

    override fun getItemViewType(position: Int): Int {

        return when (peek(position)) {
            is UiModel.Event -> R.layout.match_list_item
            is UiModel.SeparatorRound -> R.layout.match_list_league_section
            is UiModel.SeparatorTournament -> R.layout.match_list_league_section
            else -> throw IllegalStateException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.match_list_item -> {
                MatchViewHolder(
                    MatchListItemBinding.inflate(
                        inflater,
                        parent,
                        false
                    ), context
                )
            }

            R.layout.match_list_league_section -> {
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
        when (holder) {
            is MatchViewHolder -> holder.bind(getItem(position) as UiModel.Event)
            is SectionViewHolder -> holder.bind(getItem(position) as UiModel.SeparatorTournament)
            is RoundViewHolder -> holder.bind(getItem(position) as UiModel.SeparatorRound)
        }
    }

    class SectionViewHolder(
        private val binding: MatchListLeagueSectionBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tournament: UiModel.SeparatorTournament) {

            resetFields()

            with(binding) {
                country.text = tournament.event.tournament.country.name
                league.text = tournament.event.tournament.name
                leagueIcon.load(
                    context.getString(
                        R.string.tournament_icon_url,
                        tournament.event.tournament.id
                    )
                )

                layout.setOnClickListener {
                    TournamentActivity.start(context, tournament)
                }
            }
        }

        fun resetFields() {
            with(binding) {
                country.clear()
                league.clear()
                leagueIcon.clear()
            }
        }

    }

    class RoundViewHolder(
        private val binding: RoundSectionBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(roundNumber: UiModel.SeparatorRound) {
            binding.round.text = roundNumber.description
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

            val isSameSeparatorRound = oldItem is UiModel.SeparatorRound
                    && newItem is UiModel.SeparatorRound
                    && oldItem.description == newItem.description

            val isSameSeparatorTournament = oldItem is UiModel.SeparatorTournament
                    && newItem is UiModel.SeparatorTournament
                    && oldItem.event.tournament.id == newItem.event.tournament.id

            return isSameRepoItem || isSameSeparatorRound || isSameSeparatorTournament
        }

        override fun areContentsTheSame(
            oldItem: UiModel,
            newItem: UiModel
        ) = oldItem == newItem

    }
}
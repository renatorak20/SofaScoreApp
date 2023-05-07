package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.sofascoreapp.MatchDetailActivity
import com.example.sofascoreapp.PlayerDetailsActivity
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.EventStatusEnum
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.data.model.TeamDetails
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.data.model.WinnerCode
import com.example.sofascoreapp.databinding.MatchListItemBinding
import com.example.sofascoreapp.databinding.MatchListLeagueSectionBinding
import com.example.sofascoreapp.databinding.TeamMemberLayoutBinding
import com.example.sofascoreapp.databinding.TeamMemberSectionBinding
import com.example.sofascoreapp.utils.Utilities
import java.lang.IllegalArgumentException

private const val TYPE_SECTION = 0
private const val TYPE_PLAYER = 1

class TeamMembersRecyclerAdapter(
    val context: Context,
    val array: ArrayList<Any>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int) = when (array[position]) {
        is Player -> TYPE_PLAYER
        is String -> TYPE_SECTION
        else -> throw IllegalArgumentException()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_PLAYER -> {
            MemberViewHolder(
                TeamMemberLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

        else -> {
            SectionViewHolder(
                TeamMemberSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), context
            )
        }

    }

    override fun getItemCount() = array.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MemberViewHolder) {
            holder.bind(array[position] as Player)
        } else if (holder is SectionViewHolder) {
            holder.bind(array[position] as String)
        }

    }

    class MemberViewHolder(private val binding: TeamMemberLayoutBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {
            if (player.id != -1) {
                binding.memberImage.load(context.getString(R.string.player_image_url, player.id)) {
                    transformations(CircleCropTransformation())
                }
                binding.name.text = player.name
                binding.countryName.text = player.country?.name
            } else {
                binding.name.text = player.name
            }

            binding.layout.setOnClickListener {
                if (player.id != -1) {
                    val intent = Intent(context, PlayerDetailsActivity::class.java)
                    intent.putExtra("playerID", player.id)
                    context.startActivity(intent)
                }
            }

        }

    }

    class SectionViewHolder(
        private val binding: TeamMemberSectionBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.title.text = title
        }
    }
}
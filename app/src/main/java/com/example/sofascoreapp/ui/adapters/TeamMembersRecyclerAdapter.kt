package com.example.sofascoreapp.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.sofascoreapp.PlayerDetailsActivity
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.databinding.TeamMemberLayoutBinding
import com.example.sofascoreapp.databinding.TeamMemberSectionBinding

class TeamMembersRecyclerAdapter(
    private val context: Context,
    private val array: List<Any>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_PLAYER = 1
        private const val VIEW_TYPE_SECTION = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (array[position]) {
            is Player -> VIEW_TYPE_PLAYER
            is String -> VIEW_TYPE_SECTION
            else -> throw IllegalArgumentException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_PLAYER -> {
                val binding = TeamMemberLayoutBinding.inflate(inflater, parent, false)
                MemberViewHolder(binding)
            }

            VIEW_TYPE_SECTION -> {
                val binding = TeamMemberSectionBinding.inflate(inflater, parent, false)
                SectionViewHolder(binding)
            }

            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return array.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MemberViewHolder -> holder.bind(array[position] as Player)
            is SectionViewHolder -> holder.bind(array[position] as String)
        }
    }

    inner class MemberViewHolder(private val binding: TeamMemberLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {
            with(binding) {
                memberImage.load(context.getString(R.string.player_image_url, player.id)) {
                    transformations(CircleCropTransformation())
                }
                name.text = player.name
                countryName.text = player.country?.name

                if (player.id != -1) {
                    layout.setOnClickListener {
                        val intent = Intent(context, PlayerDetailsActivity::class.java)
                        intent.putExtra("playerID", player.id)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    inner class SectionViewHolder(private val binding: TeamMemberSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String) {
            binding.title.text = title
        }
    }
}


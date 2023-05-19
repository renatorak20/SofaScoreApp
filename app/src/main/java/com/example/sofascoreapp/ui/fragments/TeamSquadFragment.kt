package com.example.sofascoreapp.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sofascoreapp.PlayerDetailsActivity
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.data.model.TeamDetails
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.TeamDetailsViewModel
import kotlinx.coroutines.flow.observeOn

class TeamSquadFragment : Fragment() {

    /*

    private lateinit var binding: FragmentTeamSquadBinding
    private lateinit var teamDetailsViewModel: TeamDetailsViewModel
    private lateinit var recyclerAdapter: TeamMembersRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTeamSquadBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamDetailsViewModel =
            ViewModelProvider(requireActivity())[TeamDetailsViewModel::class.java]


        teamDetailsViewModel.getTeamPlayers().observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerAdapter =
                    TeamMembersRecyclerAdapter(
                        requireContext(),
                        (Utilities().makeTeamMembersList(
                            Player(
                                -1,
                                teamDetailsViewModel.getTeamDetails().value?.body()!!.managerName,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null
                            ), response.body()!!
                        ))
                    )
                binding.recyclerView.adapter = recyclerAdapter
            }
        }


    }
*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                var teamDetailsViewModel: TeamDetailsViewModel =
                    ViewModelProvider(requireActivity())[TeamDetailsViewModel::class.java]

                var players = teamDetailsViewModel.getTeamPlayers().observeAsState()
                val teamDetails = teamDetailsViewModel.getTeamDetails().observeAsState()

                List(requireContext(), players.value?.body()!!, teamDetails.value?.body()!!)
            }
        }
    }

}


@Composable
fun List(context: Context, players: ArrayList<Player>, teamDetails: TeamDetails) {

    val finalList = Utilities().makeTeamMembersList(
        Player(
            -1,
            teamDetails.managerName,
            null,
            null,
            null,
            null,
            null,
            null
        ), players
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(finalList) { index, player ->
            when (index) {
                0 -> {
                    Separator(text = context.getString(R.string.coach))
                    Coach(context = context, member = player)
                }

                1 -> {
                    Separator(text = context.getString(R.string.players))
                    TeamMember(context = context, member = player)
                }

                else -> TeamMember(context = context, member = player)
            }
        }
    }
}

@Composable
fun TeamMember(context: Context, member: Player) {

    val image = context.getString(R.string.player_image_url, member.id)

    Row(modifier = Modifier
        .fillMaxSize()
        .clickable {
            PlayerDetailsActivity.start(context = context, playerID = member.id)
        }) {
        AsyncImage(
            model = image,
            contentDescription = context.getString(R.string.player),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .padding(8.dp)
                .clip(CircleShape),
            placeholder = painterResource(id = R.drawable.ic_person),
            error = painterResource(id = R.drawable.ic_person)
        )
        Column(modifier = Modifier.padding(6.dp)) {
            Text(
                text = member.name,
                style = TextStyle(
                    color = getColor(color = R.attr.on_surface_on_surface_lv_1)
                )
            )
            Row {
                Text(
                    text = member.country?.name!!,
                    style = TextStyle(color = getColor(color = R.attr.on_surface_on_surface_lv_1))
                )
            }
        }
    }
}

@Composable
fun Coach(context: Context, member: Player) {
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AsyncImage(
            model = null,
            contentDescription = context.getString(R.string.coach),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .padding(8.dp)
                .clip(CircleShape),
            placeholder = painterResource(id = R.drawable.ic_person),
            error = painterResource(id = R.drawable.ic_person)
        )
        Column(modifier = Modifier.padding(6.dp)) {
            Text(
                text = member.name,
                style = TextStyle(color = getColor(color = R.attr.on_surface_on_surface_lv_1))
            )
        }
    }
}

@Composable
fun Separator(text: String) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        Text(
            text = text, modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            fontWeight = FontWeight.Bold,
            style = TextStyle(color = getColor(color = R.attr.on_surface_on_surface_lv_1))
        )
    }
}

@Composable
fun getColor(color: Int): Color {
    return colorResource(LocalContext.current.getColorFromAttrs(color).resourceId)
}

fun Context.getColorFromAttrs(attr: Int): TypedValue {
    return TypedValue().apply {
        theme.resolveAttribute(attr, this, true)
    }
}
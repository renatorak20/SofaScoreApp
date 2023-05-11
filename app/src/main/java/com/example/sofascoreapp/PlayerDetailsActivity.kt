package com.example.sofascoreapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.sofascoreapp.databinding.ActivityPlayerDetailsBinding
import com.example.sofascoreapp.ui.adapters.EventsPagingAdapter
import com.example.sofascoreapp.ui.adapters.LoadStateHeaderFooterAdapter
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.viewmodel.PlayerDetailsViewModel
import com.example.sofascoreapp.viewmodel.SpecificTournamentViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.launch
import kotlin.math.abs

class PlayerDetailsActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private lateinit var binding: ActivityPlayerDetailsBinding
    private lateinit var viewModel: PlayerDetailsViewModel
    private lateinit var recyclerAdapter: EventsPagingAdapter
    private var isExpanded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[PlayerDetailsViewModel::class.java]

        viewModel.setPlayerID(intent.getIntExtra("playerID", 0))

        setSupportActionBar(findViewById(R.id.toolbar))

        viewModel.getPlayerInformation()

        viewModel.getPlayer().observe(this) { response ->
            if (response.isSuccessful) {

                val player = response.body()!!
                binding.collapsingToolbar.title = player.name
                binding.playerImage.load(
                    getString(
                        R.string.player_image_url,
                        viewModel.getPlayerID().value
                    )
                ) {
                    transformations(CircleCropTransformation())
                    error(getDrawable(R.drawable.ic_person))
                }

                binding.content.playerClubLayout.clubIcon.load(
                    getString(
                        R.string.team_icon_url,
                        player.team?.id
                    )
                )
                binding.content.playerClubLayout.clubName.text = player.team?.name

                binding.content.nationalityText.text = player.country?.name?.subSequence(0, 3)
                binding.content.positionText.text = player.position

                if (Preferences(this).getSavedDateFormat()) {
                    binding.content.dateText.text = player.dateOfBirth?.let {
                        Utilities().getDateOfBirth(
                            it
                        )
                    }
                } else {
                    binding.content.dateText.text = player.dateOfBirth?.let {
                        Utilities().getInvertedDateOfBirth(
                            it
                        )
                    }
                }

                binding.content.yearsText.text = getString(R.string.years,
                    player.dateOfBirth?.let { Utilities().calculateYears(it) })
            }
        }

        binding.appbar.addOnOffsetChangedListener(this)

        binding.content.playerClubLayout.layout.setOnClickListener {
            val intent = Intent(this, TeamDetailsActivity::class.java)
            intent.putExtra("teamID", viewModel.getPlayer().value?.body()!!.team?.id)
            startActivity(intent)
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.content.recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = EventsPagingAdapter(this, 0)
        binding.content.recyclerView.adapter = recyclerAdapter.withLoadStateHeaderAndFooter(
            LoadStateHeaderFooterAdapter(), LoadStateHeaderFooterAdapter()
        )
        binding.content.recyclerView.setHasFixedSize(true)

        viewModel.playerEvents.observe(this) { pagingData ->
            lifecycleScope.launch {
                recyclerAdapter.submitData(pagingData)
            }
        }

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val scrollRange = appBarLayout?.totalScrollRange ?: 0

        if (verticalOffset == 0) {
            isExpanded = true
            animatePlayerImageVisibility(View.VISIBLE)
        } else if (abs(verticalOffset) >= scrollRange) {
            isExpanded = false
            animatePlayerImageVisibility(View.GONE)
        } else {
            if (isExpanded) {
                animatePlayerImageVisibility(View.GONE)
            } else {
                animatePlayerImageVisibility(View.VISIBLE)
            }
        }
    }

    private fun animatePlayerImageVisibility(visibility: Int) {
        val animator = ObjectAnimator.ofFloat(
            binding.playerImage,
            "alpha",
            binding.playerImage.alpha,
            if (visibility == View.VISIBLE) 1f else 0f
        )
        animator.duration = 100
        animator.start()

        binding.playerImage.visibility = visibility
    }
}

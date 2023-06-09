package com.example.sofascoreapp

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.sofascoreapp.data.model.DataType
import com.example.sofascoreapp.databinding.ActivityPlayerDetailsBinding
import com.example.sofascoreapp.ui.adapters.EventsPagingAdapter
import com.example.sofascoreapp.ui.adapters.LoadStateHeaderFooterAdapter
import com.example.sofascoreapp.utils.Preferences
import com.example.sofascoreapp.utils.Utilities
import com.example.sofascoreapp.utils.Utilities.Companion.loadCountryImage
import com.example.sofascoreapp.utils.Utilities.Companion.loadImage
import com.example.sofascoreapp.utils.Utilities.Companion.showNoInternetDialog
import com.example.sofascoreapp.viewmodel.PlayerDetailsViewModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.launch
import kotlin.math.abs

class PlayerDetailsActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private lateinit var binding: ActivityPlayerDetailsBinding
    private lateinit var viewModel: PlayerDetailsViewModel
    private lateinit var recyclerAdapter: EventsPagingAdapter
    private var isExpanded = true
    private lateinit var toolbar: Toolbar
    private lateinit var toolbarItem: MenuItem
    private var isFavourite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        toolbar = binding.toolbar
        toolbar.title = ""

        viewModel = ViewModelProvider(this)[PlayerDetailsViewModel::class.java]

        viewModel.setPlayerID(intent.getIntExtra("playerID", 0))

        setSupportActionBar(findViewById(R.id.toolbar))

        getInfo()

        viewModel.getPlayer().observe(this) { response ->
            if (response.isSuccessful) {

                val player = response.body()!!
                binding.collapsingToolbar.title = player.name

                binding.playerImage.loadImage(
                    this,
                    DataType.PLAYER,
                    viewModel.getPlayerID().value!!
                )

                loadCountryImage(player.country?.name!!, binding.content.nationalityIcon)

                binding.content.playerClubLayout.clubIcon.loadImage(
                    this,
                    DataType.TEAM,
                    player.team?.id!!
                )
                binding.content.playerClubLayout.clubName.text = player.team.name

                binding.content.nationalityText.text = player.country?.name?.subSequence(0, 3)
                binding.content.positionText.text = player.position

                if (Preferences.getSavedDateFormat()) {
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
            TeamDetailsActivity.start(this, viewModel.getPlayer().value?.body()!!.team?.id!!)
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.content.recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = EventsPagingAdapter(this, 0)
        binding.content.recyclerView.adapter = recyclerAdapter.withLoadStateHeaderAndFooter(
            LoadStateHeaderFooterAdapter(),
            LoadStateHeaderFooterAdapter()
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

    private fun getInfo() {
        if (Utilities().isNetworkAvailable(this)) {
            viewModel.getPlayerInformation()
        } else {
            showNoInternetDialog(this) { getInfo() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.player_menu, menu)
        toolbarItem = menu!!.findItem(R.id.menu_item_favourite)

        viewModel.getFavourites(this)

        viewModel.favourites.observe(this) { players ->
            isFavourite =
                if (players.map { it.id }.toList().contains(viewModel.getPlayerID().value)) {
                    toolbarItem.setIcon(R.drawable.ic_star_fill)
                    true
                } else {
                    toolbarItem.setIcon(R.drawable.ic_star_outline)
                    false
                }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.menu_item_favourite) {
            if (isFavourite) {
                item.setIcon(R.drawable.ic_star_outline)
                viewModel.removeFromFavourites(this)
            } else {
                item.setIcon(R.drawable.ic_star_fill)
                viewModel.addToFavourite(
                    this
                )
            }
            isFavourite = !isFavourite
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun start(context: Context, playerID: Int) {
            context.startActivity(
                Intent(context, PlayerDetailsActivity::class.java).putExtra(
                    "playerID",
                    playerID
                )
            )
        }
    }

}

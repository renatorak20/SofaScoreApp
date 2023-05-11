package com.example.sofascoreapp.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import coil.load
import com.example.sofascoreapp.R
import com.example.sofascoreapp.data.model.Tournament
import com.example.sofascoreapp.databinding.TournamentSpinnerItemBinding

class SpinnerItem(context: Context, val tournament: Tournament) : LinearLayout(context) {

    private var binding: TournamentSpinnerItemBinding =
        TournamentSpinnerItemBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
        binding.tournamentName.text = tournament.name
    }

}
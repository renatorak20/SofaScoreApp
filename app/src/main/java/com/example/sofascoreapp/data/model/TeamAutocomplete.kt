package com.example.sofascoreapp.data.model

data class TeamAutocomplete(
    val id: Int,
    val name: String,
    val sport: Sport,
    val country: Country
)

data class PlayerAutocomplete(
    val id: Int,
    val name: String,
    val slug: String,
    val sport: Sport,
    val country: Country,
    val position: String
)

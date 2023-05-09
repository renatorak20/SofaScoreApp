package com.example.sofascoreapp.data.model

data class Player(
    val id: Int,
    val name: String,
    val slug: String?,
    val sport: Sport?,
    val team: Team2?,
    val country: Country?,
    val position: String?,
    val dateOfBirth: String?
)

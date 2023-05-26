package com.example.sofascoreapp.data.model

data class TeamAutocomplete(
    val id: Int,
    val name: String,
    val sport: Sport,
    val country: Country
) {
    override fun toString(): String {
        return name
    }
}

data class PlayerAutocomplete(
    val id: Int,
    val name: String,
    val slug: String,
    val sport: Sport,
    val country: Country,
    val position: String
) {
    override fun toString(): String {
        return name
    }
}

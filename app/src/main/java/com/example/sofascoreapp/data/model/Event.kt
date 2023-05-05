package com.example.sofascoreapp.data.model

data class Event(
    val id: Int,
    val slug: String,
    val tournament: Tournament,
    val homeTeam: HomeTeam,
    val awayTeam: AwayTeam,
    val status: EventStatusEnum,
    val startDate: String?,
    val homeScore: ArrayList<HomeScore>,
    val awayScore: ArrayList<AwayScore>,
    val winnerCode: String?,
    val round: Int
)

data class Tournament(
    val id: Int,
    val name: String,
    val slug: String,
    val sport: Sport
)

data class Sport(
    val id: Int,
    val name: String,
    val slug: String
)

data class Country(
    val id: Int,
    val name: String
)

data class HomeTeam(
    val id: Int,
    val name: String,
    val country: Country
)

data class AwayTeam(
    val id: Int,
    val name: String,
    val country: Country
)

data class HomeScore(
    val total: Int,
    val period1: Int,
    val period2: Int
)

data class AwayScore(
    val total: Int,
    val period1: Int,
    val period2: Int
)

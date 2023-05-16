package com.example.sofascoreapp.data.model

data class Event(
    val id: Int,
    val slug: String,
    val tournament: Tournament,
    val homeTeam: Team2,
    val awayTeam: Team2,
    val status: EventStatusEnum,
    val startDate: String?,
    val homeScore: Score,
    val awayScore: Score,
    val winnerCode: WinnerCode?,
    val round: Int
)

data class Tournament(
    val id: Int,
    val name: String,
    val slug: String,
    val sport: Sport,
    val country: Country
)

sealed class UiModel {
    data class Event(
        val id: Int,
        val slug: String,
        val tournament: Tournament,
        val homeTeam: Team2,
        val awayTeam: Team2,
        val status: EventStatusEnum,
        val startDate: String?,
        val homeScore: Score,
        val awayScore: Score,
        val winnerCode: WinnerCode?,
        val round: Int
    ) : UiModel() {
        constructor(event: com.example.sofascoreapp.data.model.Event) : this(
            event.id,
            event.slug,
            event.tournament,
            event.homeTeam,
            event.awayTeam,
            event.status,
            event.startDate,
            event.homeScore,
            event.awayScore,
            event.winnerCode,
            event.round
        )
    }

    data class SeparatorItem(val description: String) : UiModel()
}

data class Sport(
    val id: Int,
    val name: String,
    val slug: String
)

data class Country(
    val id: Int,
    val name: String
)

data class Team2(
    val id: Int,
    val name: String,
    val country: Country
)

data class Score(
    val total: Int?,
    val period1: Int?,
    val period2: Int?,
    val period3: Int?,
    val period4: Int?,
    val overtime: Int?
)


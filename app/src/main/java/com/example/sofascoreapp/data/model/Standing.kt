package com.example.sofascoreapp.data.model

import com.google.gson.annotations.SerializedName


data class Standing(
    val id: Int,
    val tournament: Tournament,
    val type: StandingsTypeEnum,
    val sortedStandingsRows: ArrayList<StandingRow>
)

data class StandingRow(
    val id: Int,
    val team: Team2,
    val points: Int?,
    val scoresFor: Int,
    val scoresAgainst: Int,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val percentage: Float?
)

enum class StandingsTypeEnum {

    @SerializedName("total")
    TOTAL,

    @SerializedName("home")
    HOME,

    @SerializedName("away")
    AWAY

}

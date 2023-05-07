package com.example.sofascoreapp.data.model

import com.google.gson.annotations.SerializedName


data class Incident(
    val player: Player?,
    val scoringTeam: GoalScoringTeamEnum?,
    val teamSide: CardTeamSideEnum?,
    val homeScore: Int?,
    val awayScore: Int?,
    val goalType: GoalTypeEnum?,
    val id: Int,
    val time: Int,
    val type: IncidentEnum,
    val text: String?,
    val color: CardColorEnum?
)


enum class CardTeamSideEnum {
    @SerializedName("home")
    HOME,

    @SerializedName("away")
    AWAY
}

enum class CardColorEnum {
    @SerializedName("yellow")
    YELLOW,

    @SerializedName("yellowred")
    YELLOWRED,

    @SerializedName("red")
    RED
}

enum class IncidentEnum {
    @SerializedName("card")
    CARD,

    @SerializedName("goal")
    GOAL,

    @SerializedName("period")
    PERIOD
}

enum class GoalScoringTeamEnum {
    @SerializedName("home")
    HOME,

    @SerializedName("away")
    AWAY
}

enum class GoalTypeEnum {

    @SerializedName("regular")
    REGULAR,

    @SerializedName("owngoal")
    OWNGOAL,

    @SerializedName("penalty")
    PENALTY,

    @SerializedName("onepoint")
    ONEPOINT,

    @SerializedName("twopoint")
    TWOPOINT,

    @SerializedName("threepoint")
    THREEPOINT,

    @SerializedName("touchdown")
    TOUCHDOWN,

    @SerializedName("safety")
    SAFETY,

    @SerializedName("fieldgoal")
    FIELDGOAL,

    @SerializedName("extrapoint")
    EXTRAPOINT
}


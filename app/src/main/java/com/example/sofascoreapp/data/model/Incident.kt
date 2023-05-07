package com.example.sofascoreapp.data.model

import com.google.gson.annotations.SerializedName

data class IncidentCard(
    val player: Player,
    val teamSide: CardTeamSideEnum,
    val color: CardColorEnum,
    val id: Int,
    val time: Int,
    val type: IncidentEnum
)

data class IncidentGoal(
    val player: Player,
    val scoringTeam: GoalScoringTeamEnum,
    val homeScore: Int,
    val awayScore: Int,
    val goalType: GoalTypeEnum,
    val id: Int,
    val time: Int,
    val type: IncidentEnum
)

data class IncidentPeriod(
    val text: String,
    val id: Int,
    val time: Int,
    val type: IncidentEnum
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


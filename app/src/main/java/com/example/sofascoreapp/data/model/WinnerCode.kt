package com.example.sofascoreapp.data.model

import com.google.gson.annotations.SerializedName

enum class WinnerCode {

    @SerializedName("home")
    HOME,

    @SerializedName("away")
    AWAY,

    @SerializedName("draw")
    DRAW

}
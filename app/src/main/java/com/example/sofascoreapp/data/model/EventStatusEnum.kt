package com.example.sofascoreapp.data.model

import com.google.gson.annotations.SerializedName

enum class EventStatusEnum {

    @SerializedName("notstarted")
    NOTSTARTED,

    @SerializedName("inprogress")
    INPROGRESS,

    @SerializedName("finished")
    FINISHED

}
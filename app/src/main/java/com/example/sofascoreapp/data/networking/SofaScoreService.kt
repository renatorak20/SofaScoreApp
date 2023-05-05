package com.example.sofascoreapp.data.networking

import com.example.sofascoreapp.data.model.Event
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SofaScoreService {

    @GET("/sport/{slug}/events/{date}")
    suspend fun getEventsForSportDate(@Path("slug") slug: String, @Path("date") date: String): Response<ArrayList<Event>>

    @GET("/event/{id}")
    suspend fun getEvent(@Path("id") id: Int): Response<Event>

}
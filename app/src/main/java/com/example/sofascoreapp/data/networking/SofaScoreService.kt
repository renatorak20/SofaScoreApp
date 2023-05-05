package com.example.sofascoreapp.data.networking

import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.Tournament
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SofaScoreService {

    @GET("/sport/{slug}/events/{date}")
    suspend fun getEventsForSport(
        @Path("slug") slug: String,
        @Path("date") date: String
    ): Response<ArrayList<Event>>

    @GET("/event/{id}")
    suspend fun getEvent(@Path("id") id: Int): Response<Event>

    @GET("/sport/{slug}/tournaments")
    suspend fun getTournamentsForSport(@Path("slug") sport: String): Response<ArrayList<Tournament>>


}
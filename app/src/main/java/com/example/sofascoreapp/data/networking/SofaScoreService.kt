package com.example.sofascoreapp.data.networking

import com.example.sofascoreapp.data.model.BasketballEvent
import com.example.sofascoreapp.data.model.FootballEvent
import com.example.sofascoreapp.data.model.Tournament
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SofaScoreService {

    @GET("/sport/{slug}/events/{date}")
    suspend fun getEventsForFootball(
        @Path("slug") slug: String,
        @Path("date") date: String
    ): Response<ArrayList<FootballEvent>>

    @GET("/sport/{slug}/events/{date}")
    suspend fun getEventsForBasketball(
        @Path("slug") slug: String,
        @Path("date") date: String
    ): Response<ArrayList<BasketballEvent>>

    @GET("/event/{id}")
    suspend fun getEvent(@Path("id") id: Int): Response<FootballEvent>

    @GET("/sport/{slug}/tournaments")
    suspend fun getTournamentsForSport(@Path("slug") sport: String): Response<ArrayList<Tournament>>


}
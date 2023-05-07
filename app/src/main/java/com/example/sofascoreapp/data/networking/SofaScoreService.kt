package com.example.sofascoreapp.data.networking

import com.example.sofascoreapp.data.model.Event
import com.example.sofascoreapp.data.model.Player
import com.example.sofascoreapp.data.model.TeamDetails
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

    @GET("/team/{id}")
    suspend fun getTeamDetails(@Path("id") id: Int): Response<TeamDetails>

    @GET("/team/{id}/players")
    suspend fun getTeamPlayers(@Path("id") id: Int): Response<ArrayList<Player>>

    @GET("/team/{id}/tournaments")
    suspend fun getTeamTournaments(@Path("id") id: Int): Response<ArrayList<Tournament>>

    @GET("/team/{id}/events/{span}/{page}")
    suspend fun getTeamEventsPage(
        @Path("id") id: Int,
        @Path("span") span: String,
        @Path("page") page: Int
    ): Response<ArrayList<Event>>

    @GET("/player/{id}")
    suspend fun getPlayerInfo(@Path("id") id: Int): Response<Player>

    @GET("/event/{id}/incidents")
    suspend fun getEventIncidents(@Path("id") id: Int): Response<ArrayList<Any>>

}
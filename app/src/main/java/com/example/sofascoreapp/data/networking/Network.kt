package com.example.sofascoreapp.data.networking

import com.example.sofascoreapp.data.model.Score
import com.example.sofascoreapp.utils.CustomConverter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Network {

    private val service: SofaScoreService
    private val baseURL = "https://academy.dev.sofascore.com/"

    fun getService(): SofaScoreService {
        return service
    }

    init {
        val gson = GsonBuilder()
            .registerTypeAdapter(Score::class.java, CustomConverter())
            .create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()


        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()

        service = retrofit.create(SofaScoreService::class.java)
    }
}



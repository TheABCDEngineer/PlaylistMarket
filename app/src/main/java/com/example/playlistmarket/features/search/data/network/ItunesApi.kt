package com.example.playlistmarket.features.search.data.network

import com.example.playlistmarket.features.search.data.dto.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?entity")
    fun findTrack(@Query("term") text: String): Call<TracksResponse>
}
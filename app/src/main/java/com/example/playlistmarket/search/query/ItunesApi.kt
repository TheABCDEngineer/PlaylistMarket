package com.example.playlistmarket.search.query

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?entity")
    fun findTrack(@Query("term") text: String): Call<TracksResponse>
}
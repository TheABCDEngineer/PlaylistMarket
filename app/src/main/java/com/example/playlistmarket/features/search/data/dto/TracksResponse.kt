package com.example.playlistmarket.features.search.data.dto

data class TracksResponse(
    val results: ArrayList<TrackDto>
): Response()
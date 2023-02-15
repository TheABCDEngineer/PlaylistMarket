package com.example.playlistmarket.data.network

import com.example.playlistmarket.domain.models.Track

data class TracksResponse(
    val results: ArrayList<Track>
) {}
package com.example.playlistmarket.search.query

import com.example.playlistmarket.medialibrary.Track

data class TracksResponse(
    val results: ArrayList<Track>
) {}
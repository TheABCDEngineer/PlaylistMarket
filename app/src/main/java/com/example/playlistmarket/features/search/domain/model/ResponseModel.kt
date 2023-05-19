package com.example.playlistmarket.features.search.domain.model

import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.features.search.domain.enums.QueryError

data class ResponseModel(
    val error: QueryError,
    val resultTrackList: ArrayList<Track>
)
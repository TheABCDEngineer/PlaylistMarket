package com.example.playlistmarket.features.search.domain.repository

import com.example.playlistmarket.features.search.data.dto.Response
import com.example.playlistmarket.features.search.data.dto.TracksRequest

interface NetworkClientRepository {
    var callback: (Response) -> Unit
    fun executeRequest(request: TracksRequest)
}
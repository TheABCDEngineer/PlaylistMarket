package com.example.playlistmarket.features.search.data.network

import com.example.playlistmarket.features.search.data.dto.TracksResponse

interface NetworkClient {
    suspend fun executeRequest(request: Any): TracksResponse
}
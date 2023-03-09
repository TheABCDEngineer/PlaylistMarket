package com.example.playlistmarket.features.search.data.network

import com.example.playlistmarket.features.search.data.dto.Response
import com.example.playlistmarket.features.search.data.dto.TracksRequest

interface NetworkClient {
    var callback: (Response) -> Unit
    fun executeRequest(request: TracksRequest)
}
package com.example.playlistmarket.features.search.domain.repository

import com.example.playlistmarket.features.search.domain.model.ResponseContainer

interface NetworkClientRepository {
    var callback: (ResponseContainer) -> Unit
    fun executeRequest(queryValue: String)
}
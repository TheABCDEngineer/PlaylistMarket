package com.example.playlistmarket.features.search.domain

import com.example.playlistmarket.features.search.domain.model.ResponseModel

interface NetworkClient {
    var callback: (ResponseModel) -> Unit
    fun executeRequest(queryValue: String)
}
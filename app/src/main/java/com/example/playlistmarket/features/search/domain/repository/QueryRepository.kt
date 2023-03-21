package com.example.playlistmarket.features.search.domain.repository

import com.example.playlistmarket.features.search.domain.model.ResponseModel

interface QueryRepository {
    var callback: (ResponseModel) -> Unit
    fun executeRequest(queryValue: String)
}
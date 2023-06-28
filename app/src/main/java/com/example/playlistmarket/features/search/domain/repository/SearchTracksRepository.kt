package com.example.playlistmarket.features.search.domain.repository

import com.example.playlistmarket.features.search.domain.model.ResponseModel
import kotlinx.coroutines.flow.Flow

interface SearchTracksRepository {
    fun searchTracks(expression: String): Flow<ResponseModel>
}
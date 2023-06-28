package com.example.playlistmarket.features.search.data

import com.example.playlistmarket.features.search.data.dataConverter.ResponseConverter
import com.example.playlistmarket.features.search.data.dto.TracksRequest
import com.example.playlistmarket.features.search.data.network.NetworkClient
import com.example.playlistmarket.features.search.domain.model.ResponseModel
import com.example.playlistmarket.features.search.domain.repository.SearchTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTracksRepositoryImplNetwork(
    private val networkClient: NetworkClient
): SearchTracksRepository {
    override fun searchTracks(expression: String): Flow<ResponseModel> = flow {
        val response = networkClient.executeRequest(TracksRequest(expression))
        val responseMap = ResponseConverter.convertToDomain(response)
        emit(responseMap)
    }
}
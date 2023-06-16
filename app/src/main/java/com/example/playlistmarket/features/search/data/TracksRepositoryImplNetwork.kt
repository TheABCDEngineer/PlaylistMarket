package com.example.playlistmarket.features.search.data

import com.example.playlistmarket.features.search.data.dataConverter.ResponseConverter
import com.example.playlistmarket.features.search.data.dto.TracksRequest
import com.example.playlistmarket.features.search.data.network.NetworkClient
import com.example.playlistmarket.features.search.domain.model.ResponseModel
import com.example.playlistmarket.features.search.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImplNetwork(
    private val networkClient: NetworkClient
): TracksRepository {
    override fun searchTracks(expression: String): Flow<ResponseModel> = flow {
        val response = networkClient.executeRequest(TracksRequest(expression))
        val responseMap = ResponseConverter.convertToDomain(response)
        emit(responseMap)
    }
}
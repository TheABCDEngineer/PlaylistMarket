package com.example.playlistmarket.features.search.data.network

import com.example.playlistmarket.features.search.data.dto.TracksRequest
import com.example.playlistmarket.features.search.data.dto.TracksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkClientImpIRetrofit(apiService: ApiService) : NetworkClient {
    private val serviceApi = apiService.createApiService()

    override suspend fun executeRequest(request: Any): TracksResponse {
        if (request !is TracksRequest) return TracksResponse(ArrayList()).apply { responseCode = 400 }

        return withContext(Dispatchers.IO) {
            try {
                val response = serviceApi.findTracks(request.expression)
                response.apply { responseCode = 200 }
            } catch (e: Throwable) {
                TracksResponse(ArrayList()).apply { responseCode = 400 }
            }
        }
    }
}
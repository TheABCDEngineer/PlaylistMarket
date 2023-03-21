package com.example.playlistmarket.features.search.data.network

class ApiService(
    private val networkClient: NetworkClient,
    private val baseUrl: String
) {
    fun createApiService(): ItunesApi {
        return networkClient.getServiceApi(
            baseUrl,
            ItunesApi::class.java)
    }
}
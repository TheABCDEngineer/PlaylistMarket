package com.example.playlistmarket.features.search.data.network

class ApiService(
    private val baseUrl: String
) {
    fun createApiService(): ItunesApi {
        return RetrofitNetworkServiceFactory.getService(
            baseUrl,
            ItunesApi::class.java)
    }
}
package com.example.playlistmarket.features.search.data.network

interface NetworkClient {
    fun <T> getServiceApi(baseUrl: String, service: Class<T>): T
}
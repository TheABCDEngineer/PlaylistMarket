package com.example.playlistmarket.features.search.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientImplRetrofit: NetworkClient {
    override fun <T> getServiceApi(baseUrl: String, service: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(service)
    }
}
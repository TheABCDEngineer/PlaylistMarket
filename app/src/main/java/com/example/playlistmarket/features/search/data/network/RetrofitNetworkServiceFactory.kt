package com.example.playlistmarket.features.search.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkServiceFactory {
    companion object {
        fun <T> getService(baseUrl: String, service: Class<T>): T {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(service)
        }
    }
}
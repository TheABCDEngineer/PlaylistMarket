package com.example.playlistmarket.di

import com.example.playlistmarket.App
import com.example.playlistmarket.features.search.data.network.ApiService
import com.example.playlistmarket.features.search.data.network.NetworkClient
import com.example.playlistmarket.features.search.data.network.NetworkClientImplRetrofit
import org.koin.dsl.module

val networkModule = module {

    single<NetworkClient> {
        NetworkClientImplRetrofit()
    }

    single {
        ApiService(get(), App.SEARCH_TRACKS_BASE_URL)
    }
}
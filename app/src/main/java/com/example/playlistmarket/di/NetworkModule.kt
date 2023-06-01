package com.example.playlistmarket.di

import com.example.playlistmarket.App.Companion.SEARCH_TRACKS_BASE_URL
import com.example.playlistmarket.features.search.data.network.ApiService
import com.example.playlistmarket.features.search.data.network.NetworkClientImpIRetrofit
import com.example.playlistmarket.features.search.data.network.NetworkClient
import org.koin.dsl.module

val networkModule = module {

    single<NetworkClient> {
        NetworkClientImpIRetrofit(get())
    }

    single {
        ApiService(SEARCH_TRACKS_BASE_URL)
    }
}
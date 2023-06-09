package com.example.playlistmarket.di

import com.example.playlistmarket.App.Companion.SEARCH_TRACKS_BASE_URL
import com.example.playlistmarket.features.search.data.network.ApiService
import com.example.playlistmarket.features.search.data.network.NetworkClientImpIRetrofit
import com.example.playlistmarket.features.search.data.network.NetworkClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {

    single {
        ApiService(SEARCH_TRACKS_BASE_URL)
    }

    singleOf(::NetworkClientImpIRetrofit).bind<NetworkClient>()
}
package com.example.playlistmarket.di

import com.example.playlistmarket.features.search.data.TracksRepositoryImplNetwork
import com.example.playlistmarket.features.search.domain.repository.TracksRepository
import com.example.playlistmarket.features.search.presentation.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    single<TracksRepository> {
        TracksRepositoryImplNetwork(get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }
}
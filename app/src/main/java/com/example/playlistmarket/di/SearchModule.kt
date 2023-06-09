package com.example.playlistmarket.di

import com.example.playlistmarket.features.search.data.TracksRepositoryImplNetwork
import com.example.playlistmarket.features.search.domain.repository.TracksRepository
import com.example.playlistmarket.features.search.presentation.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchModule = module {

    singleOf(::TracksRepositoryImplNetwork).bind<TracksRepository>()

    viewModelOf(::SearchViewModel).bind()
}
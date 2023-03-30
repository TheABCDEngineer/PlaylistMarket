package com.example.playlistmarket.di

import com.example.playlistmarket.features.search.data.network.QueryRepositoryImpIRetrofit
import com.example.playlistmarket.features.search.domain.QueryInteractorImpl
import com.example.playlistmarket.features.search.domain.interactors.QueryInteractor
import com.example.playlistmarket.features.search.domain.repository.QueryRepository
import com.example.playlistmarket.features.search.presentation.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {

    single<QueryRepository> {
        QueryRepositoryImpIRetrofit(get())
    }

    single<QueryInteractor> {
        QueryInteractorImpl(get())
    }

    viewModel {
        SearchViewModel(get(), get())
    }
}
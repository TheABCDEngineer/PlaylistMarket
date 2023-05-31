package com.example.playlistmarket.di

import com.example.playlistmarket.features.search.presentation.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }
}
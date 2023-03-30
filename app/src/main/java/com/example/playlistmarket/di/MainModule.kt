package com.example.playlistmarket.di

import com.example.playlistmarket.features.main.presentation.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel {
        MainViewModel(get())
    }
}
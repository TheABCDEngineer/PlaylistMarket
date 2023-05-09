package com.example.playlistmarket.di

import com.example.playlistmarket.base.presentation.viewModel.RootViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel {
        RootViewModel(get())
    }
}
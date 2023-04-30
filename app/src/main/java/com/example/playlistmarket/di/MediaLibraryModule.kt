package com.example.playlistmarket.di

import com.example.playlistmarket.features.medialibrary.presentation.viewModel.FavoritesViewModel
import com.example.playlistmarket.features.medialibrary.presentation.viewModel.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val mediaLibraryModule = module {
    viewModel {
        FavoritesViewModel(
            get()
        )
    }

    viewModel {
        PlaylistsViewModel(
            get()
        )
    }
}

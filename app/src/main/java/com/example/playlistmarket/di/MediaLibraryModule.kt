package com.example.playlistmarket.di

import com.example.playlistmarket.features.medialibrary.data.PlaylistModelsConverter
import com.example.playlistmarket.features.medialibrary.presentation.viewModel.FavoritesViewModel
import com.example.playlistmarket.features.medialibrary.presentation.viewModel.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val mediaLibraryModule = module {

    viewModelOf(::FavoritesViewModel).bind()

    viewModelOf(::PlaylistsViewModel).bind()

    singleOf(::PlaylistModelsConverter).bind()
}

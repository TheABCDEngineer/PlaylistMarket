package com.example.playlistmarket.di

import org.koin.dsl.module
import com.example.playlistmarket.features.playlistCreator.presentation.viewModel.PlaylistCreatorViewModel
import com.example.playlistmarket.root.domain.model.Track
import org.koin.androidx.viewmodel.dsl.viewModel

val playlistCreatorModule = module {

    viewModel { (track: Track?) ->
        PlaylistCreatorViewModel(
            track, get(), get()
        )
    }
}
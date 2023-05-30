package com.example.playlistmarket.di

import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.features.player.data.UrlTrackPlayerImplMediaPlayer
import com.example.playlistmarket.features.player.domain.TrackHandleImpl
import com.example.playlistmarket.features.player.domain.drivers.UrlTrackPlayer
import com.example.playlistmarket.features.player.domain.interactors.TrackHandleInteractor
import com.example.playlistmarket.features.player.presentation.viewModel.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    single<TrackHandleInteractor> {
        TrackHandleImpl(get())
    }

    factory<UrlTrackPlayer> {
        UrlTrackPlayerImplMediaPlayer()
    }

    viewModel { (track: Track) ->
        PlayerViewModel(
            track,
            get(),
            get()
        )
    }
}
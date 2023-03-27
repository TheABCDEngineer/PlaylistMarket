package com.example.playlistmarket.di

import com.example.playlistmarket.base.domain.model.Track
import com.example.playlistmarket.features.player.data.UrlTrackPlayerImplMediaPlayer
import com.example.playlistmarket.features.player.domain.PlaybackControlImpl
import com.example.playlistmarket.features.player.domain.TrackHandleImpl
import com.example.playlistmarket.features.player.domain.drivers.UrlTrackPlayer
import com.example.playlistmarket.features.player.domain.interactors.PlaybackControlInteractor
import com.example.playlistmarket.features.player.domain.interactors.TrackHandleInteractor
import com.example.playlistmarket.features.player.presentation.viewModel.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val playerModule = module {

    single<PlaybackControlInteractor> { (trackUrl: String) ->
        PlaybackControlImpl(trackUrl,get())
    }

    single<TrackHandleInteractor> {
        TrackHandleImpl(get())
    }

    single<UrlTrackPlayer> {
        UrlTrackPlayerImplMediaPlayer()
    }

    viewModel { (track: Track) ->
        PlayerViewModel(
            track,
            get { parametersOf(track.previewUrl) },
            get()
        )
    }
}
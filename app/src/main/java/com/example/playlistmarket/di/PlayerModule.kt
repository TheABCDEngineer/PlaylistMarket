package com.example.playlistmarket.di

import com.example.playlistmarket.features.player.data.UrlTrackPlayerImplMediaPlayer
import com.example.playlistmarket.features.player.domain.TrackHandleImpl
import com.example.playlistmarket.features.player.domain.drivers.UrlTrackPlayer
import com.example.playlistmarket.features.player.domain.interactors.TrackHandleInteractor
import com.example.playlistmarket.features.player.presentation.viewModel.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val playerModule = module {

    singleOf(::TrackHandleImpl).bind<TrackHandleInteractor>()

    factoryOf(::UrlTrackPlayerImplMediaPlayer).bind<UrlTrackPlayer>()

    viewModelOf(::PlayerViewModel).bind()
}
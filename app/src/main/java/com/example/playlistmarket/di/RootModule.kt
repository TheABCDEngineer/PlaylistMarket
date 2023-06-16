package com.example.playlistmarket.di

import android.content.Context
import com.example.playlistmarket.App
import com.example.playlistmarket.root.data.sharedPreferences.PlaylistRepositoryImplSharedPreferences
import com.example.playlistmarket.root.data.sharedPreferences.SettingsRepositoryImplSharedPreferences
import com.example.playlistmarket.root.domain.PlaylistCreator
import com.example.playlistmarket.root.domain.repository.PlaylistRepository
import com.example.playlistmarket.root.domain.repository.SettingsRepository
import com.example.playlistmarket.root.presentation.viewModel.RootViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val rootModule = module {

    viewModelOf(::RootViewModel).bind()

    singleOf(::PlaylistCreator).bind()

    single<PlaylistRepository> {
        PlaylistRepositoryImplSharedPreferences(
            androidContext().getSharedPreferences(App.PLAYLISTS_FILE_NAME, Context.MODE_PRIVATE)
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImplSharedPreferences(
            androidContext().getSharedPreferences(App.SETTINGS_FILE_NAME, Context.MODE_PRIVATE)
        )
    }
}
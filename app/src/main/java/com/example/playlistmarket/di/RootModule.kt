package com.example.playlistmarket.di

import android.content.Context
import androidx.room.Room
import com.example.playlistmarket.App
import com.example.playlistmarket.features.search.data.RecentTracksRepositoryImplSharedPreferences
import com.example.playlistmarket.root.data.sharedPreferences.SettingsRepositoryImplSharedPreferences
import com.example.playlistmarket.features.search.domain.repository.RecentTracksRepository
import com.example.playlistmarket.root.data.database.TracksDatabase
import com.example.playlistmarket.root.domain.repository.TracksRepository
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository
import com.example.playlistmarket.root.domain.repository.SettingsRepository
import com.example.playlistmarket.root.presentation.viewModel.RootViewModel
import com.example.playlistmarket.root.data.database.TracksRepositoryImpDb
import com.example.playlistmarket.root.data.database.PlaylistsRepositoryImplDb
import com.example.playlistmarket.root.data.files.PlaylistArtworksRepositoryImplFile
import com.example.playlistmarket.root.domain.repository.PlaylistArtworksRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val rootModule = module {

    viewModelOf(::RootViewModel).bind()

    single<RecentTracksRepository> {
        RecentTracksRepositoryImplSharedPreferences(
            androidContext().getSharedPreferences(App.RECENT_TRACKS_KEY, Context.MODE_PRIVATE)
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImplSharedPreferences(
            androidContext().getSharedPreferences(App.SETTINGS_FILE_NAME, Context.MODE_PRIVATE)
        )
    }

    singleOf(::TracksRepositoryImpDb).bind<TracksRepository>()

    singleOf(::PlaylistsRepositoryImplDb).bind<PlaylistsRepository>()

    singleOf(::PlaylistArtworksRepositoryImplFile).bind<PlaylistArtworksRepository>()

    single {
        Room.databaseBuilder(androidContext(), TracksDatabase::class.java, "tracksDb11.db")
            .build()
    }
}
package com.example.playlistmarket.creator

import com.example.playlistmarket.App
import com.example.playlistmarket.features.main.viewModel.MainViewModel
import com.example.playlistmarket.features.player.viewModel.PlayerViewModel
import com.example.playlistmarket.features.search.viewModel.SearchViewModel
import com.example.playlistmarket.features.setting.viewModel.SettingsViewModel
import com.example.playlistmarket.features.search.data.network.RetrofitClient
import com.example.playlistmarket.features.search.data.network.ItunesApi
import com.example.playlistmarket.data.sharedPreferences.PlaylistRepositoryImpSharedPreferences
import com.example.playlistmarket.data.sharedPreferences.SettingsRepositoryImpSharedPreferences
import com.example.playlistmarket.domain.entity.Playlist
import com.example.playlistmarket.domain.model.Track
import com.example.playlistmarket.features.player.data.UrlTrackPlayerImpMediaPlayer
import com.example.playlistmarket.features.player.domain.TrackPlaybackControl
import com.example.playlistmarket.features.player.domain.TrackHandleAct
import com.example.playlistmarket.features.search.data.network.NetworkClientImpRetrofit
import com.example.playlistmarket.features.search.domain.QueryExecutor

object Creator {

    fun createMainViewModel(): MainViewModel {
        return MainViewModel(
            SettingsRepositoryImpSharedPreferences(App.settingsFile)
        )
    }

    fun createSearchViewModel(): SearchViewModel {
        App.serviceApi = RetrofitClient.getServiceApi(
            App.SEARCH_TRACKS_BASE_URL,
            ItunesApi::class.java
        )
        return SearchViewModel(
            QueryExecutor(NetworkClientImpRetrofit()),
            createPlaylist(App.RECENT_TRACKS_LIST_KEY, 10)
        )
    }

    fun createSettingsViewModel(): SettingsViewModel {
        return SettingsViewModel(
            SettingsRepositoryImpSharedPreferences(App.settingsFile)
        )
    }

    fun createPlayerViewModel(track: Track): PlayerViewModel {
        return PlayerViewModel(
            track,
            TrackPlaybackControl(track.previewUrl, UrlTrackPlayerImpMediaPlayer()),
            TrackHandleAct(PlaylistRepositoryImpSharedPreferences(App.playlistsFile))
        )
    }

    fun createPlaylist(title: String, limit: Int? = null): Playlist {
        return Playlist(
            PlaylistRepositoryImpSharedPreferences(App.playlistsFile),
            title,
            limit
        )
    }
}
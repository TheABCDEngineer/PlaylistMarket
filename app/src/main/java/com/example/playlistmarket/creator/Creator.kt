package com.example.playlistmarket.creator

import com.example.playlistmarket.App
import com.example.playlistmarket.features.main.presentation.viewModel.MainViewModel
import com.example.playlistmarket.features.player.presentation.viewModel.PlayerViewModel
import com.example.playlistmarket.features.search.presentation.viewModel.SearchViewModel
import com.example.playlistmarket.features.setting.presentation.viewModel.SettingsViewModel
import com.example.playlistmarket.features.main.data.sharedPreferences.PlaylistRepositoryImplSharedPreferences
import com.example.playlistmarket.features.main.data.sharedPreferences.SettingsRepositoryImplSharedPreferences
import com.example.playlistmarket.features.main.domain.entity.Playlist
import com.example.playlistmarket.features.main.domain.model.Track
import com.example.playlistmarket.features.player.data.UrlTrackPlayerImplMediaPlayer
import com.example.playlistmarket.features.player.domain.PlaybackControlImpl
import com.example.playlistmarket.features.player.domain.TrackHandleImpl
import com.example.playlistmarket.features.search.data.network.ApiService
import com.example.playlistmarket.features.search.data.network.NetworkClientImplRetrofit
import com.example.playlistmarket.features.search.data.network.QueryRepositoryImpIRetrofit
import com.example.playlistmarket.features.search.domain.QueryInteractorImpl

object Creator {

    fun createMainViewModel(): MainViewModel {
        return MainViewModel(
            SettingsRepositoryImplSharedPreferences(App.settingsFile)
        )
    }

    fun createSearchViewModel(): SearchViewModel {
        return SearchViewModel(
            QueryInteractorImpl(
                QueryRepositoryImpIRetrofit(
                    ApiService(NetworkClientImplRetrofit(), App.SEARCH_TRACKS_BASE_URL)
                )
            ),
            createPlaylist(App.RECENT_TRACKS_LIST_KEY, 10)
        )
    }

    fun createSettingsViewModel(): SettingsViewModel {
        return SettingsViewModel(
            SettingsRepositoryImplSharedPreferences(App.settingsFile)
        )
    }

    fun createPlayerViewModel(track: Track): PlayerViewModel {
        return PlayerViewModel(
            track,
            PlaybackControlImpl(track.previewUrl, UrlTrackPlayerImplMediaPlayer()),
            TrackHandleImpl(PlaylistRepositoryImplSharedPreferences(App.playlistsFile))
        )
    }

    fun createPlaylist(title: String, limit: Int? = null): Playlist {
        return Playlist(
            PlaylistRepositoryImplSharedPreferences(App.playlistsFile),
            title,
            limit
        )
    }
}
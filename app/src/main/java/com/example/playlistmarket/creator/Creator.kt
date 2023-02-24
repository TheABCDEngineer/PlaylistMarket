package com.example.playlistmarket.creator

import android.media.MediaPlayer
import com.example.playlistmarket.App
import com.example.playlistmarket.features.main.presenter.MainPresenter
import com.example.playlistmarket.features.main.presenter.MainView
import com.example.playlistmarket.features.player.presenter.PlayerPresenter
import com.example.playlistmarket.features.player.presenter.PlayerView
import com.example.playlistmarket.features.search.presenter.SearchPresenter
import com.example.playlistmarket.features.search.presenter.SearchView
import com.example.playlistmarket.features.setting.presenter.SettingsPresenter
import com.example.playlistmarket.features.setting.presenter.SettingsView
import com.example.playlistmarket.data.network.RetrofitClient
import com.example.playlistmarket.data.network.ItunesApi
import com.example.playlistmarket.data.sharedPreferences.PlaylistStorageImpSharedPreferences
import com.example.playlistmarket.data.sharedPreferences.SettingsStorageImpSharedPreferences
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.features.player.domain.PlaybackImpMediaPlayer
import com.example.playlistmarket.features.player.domain.TrackHandleAct
import com.example.playlistmarket.features.search.domain.QueryImpRetrofit2

object Creator {
    var mainPresenter: MainPresenter? = null
    var searchPresenter: SearchPresenter? = null
    var settingsPresenter: SettingsPresenter? = null
    var playerPresenter: PlayerPresenter? = null

    fun createMainPresenter(view: MainView) {
        if (mainPresenter != null) {
            mainPresenter!!.view = view
            return
        }
        mainPresenter = MainPresenter(
            view,
            SettingsStorageImpSharedPreferences(App.settingsFile)
        )
    }

    fun createSearchPresenter(view: SearchView) {
        if (searchPresenter != null) {
            searchPresenter!!.view = view
            return
        }
        App.serviceApi = RetrofitClient.getServiceApi(
            App.SEARCH_TRACKS_BASE_URL,
            ItunesApi::class.java
        )
        searchPresenter = SearchPresenter(
            view,
            QueryImpRetrofit2(),
            createPlaylist(App.RECENT_TRACKS_LIST_KEY, 10)
        )
    }

    fun createSettingsPresenter(view: SettingsView) {
        if (settingsPresenter != null) {
            settingsPresenter!!.view = view
            return
        }
        settingsPresenter = SettingsPresenter(
            view,
            SettingsStorageImpSharedPreferences(App.settingsFile)
        )
    }

    fun createPlayerPresenter(view: PlayerView, track: Track) {
        if (playerPresenter != null) {
            playerPresenter!!.view = view
            return
        }
        playerPresenter = PlayerPresenter(
            view,
            track,
            PlaybackImpMediaPlayer(track, MediaPlayer()),
            TrackHandleAct(PlaylistStorageImpSharedPreferences(App.playlistsFile))
        )
    }

    fun createPlaylist(title: String, limit: Int? = null): Playlist {
        return Playlist(
            PlaylistStorageImpSharedPreferences(App.playlistsFile),
            title,
            limit
        )
    }
}
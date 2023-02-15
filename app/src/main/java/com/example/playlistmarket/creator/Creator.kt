package com.example.playlistmarket.creator

import android.media.MediaPlayer
import com.example.playlistmarket.controller.main.MainController
import com.example.playlistmarket.controller.main.MainView
import com.example.playlistmarket.controller.player.PlayerController
import com.example.playlistmarket.controller.player.PlayerView
import com.example.playlistmarket.controller.search.SearchController
import com.example.playlistmarket.controller.search.SearchView
import com.example.playlistmarket.controller.settings.SettingsController
import com.example.playlistmarket.controller.settings.SettingsView
import com.example.playlistmarket.data.network.RetrofitClient
import com.example.playlistmarket.data.network.ItunesApi
import com.example.playlistmarket.data.sharedPreferences.PlaylistStorageImpSharedPreferences
import com.example.playlistmarket.data.sharedPreferences.SettingsStorageImpSharedPreferences
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.domain.player.PlaybackImpMediaPlayer
import com.example.playlistmarket.domain.player.TrackHandleAct
import com.example.playlistmarket.domain.player.TrackPropertiesAct
import com.example.playlistmarket.domain.search.QueryImpRetrofit2

object Creator {
    var mainController: MainController? = null
    var searchController: SearchController? = null
    var settingsController: SettingsController? = null
    var playerController: PlayerController? = null

    fun createMainController(view: MainView) {
        if (mainController != null) {
            mainController!!.view = view
            return
        }
        mainController = MainController(
            view,
            SettingsStorageImpSharedPreferences(App.settingsFile)
        )
    }

    fun createSearchController(view: SearchView) {
        if (searchController != null) {
            searchController!!.view = view
            return
        }
        App.serviceApi = RetrofitClient.getServiceApi(
            App.SEARCH_TRACKS_BASE_URL,
            ItunesApi::class.java
        )
        searchController = SearchController(
            view,
            QueryImpRetrofit2(),
            createPlaylist(App.RECENT_TRACKS_LIST_KEY, 10)
        )
    }

    fun createSettingsController(view: SettingsView) {
        if (settingsController != null) {
            settingsController!!.view = view
            return
        }
        settingsController = SettingsController(
            view,
            SettingsStorageImpSharedPreferences(App.settingsFile)
        )
    }

    fun createPlayerController(view: PlayerView, track: Track) {
        if (playerController != null) {
            playerController!!.view = view
            return
        }
        playerController = PlayerController(
            view,
            track,
            PlaybackImpMediaPlayer(track, MediaPlayer()),
            TrackHandleAct(PlaylistStorageImpSharedPreferences(App.playlistsFile)),
            TrackPropertiesAct()
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
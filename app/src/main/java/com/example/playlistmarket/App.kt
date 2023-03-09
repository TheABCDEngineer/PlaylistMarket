package com.example.playlistmarket

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import com.example.playlistmarket.features.search.data.network.ItunesApi

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        settingsFile = getSharedPreferences(SETTINGS_FILE_NAME, MODE_PRIVATE)
        playlistsFile = getSharedPreferences(PLAYLISTS_FILE_NAME, MODE_PRIVATE)
    }

    companion object {
        const val SETTINGS_FILE_NAME = "APP_preferences"
        const val PLAYLISTS_FILE_NAME = "Playlists"
        const val DARK_MODE_STATUS_KEY = "dark_mode_status"
        const val RECENT_TRACKS_LIST_KEY = " recent_tracks_list "
        const val FAVORITES_LIST_KEY = " favorites "
        const val PLAYLIST_TITLES_KEY = " playlists "
        const val SEARCH_TRACKS_BASE_URL = "https://itunes.apple.com"
        const val TRACK_KEY = "track"

        lateinit var appContext: Context
        lateinit var settingsFile: SharedPreferences
        lateinit var playlistsFile: SharedPreferences

        var serviceApi: ItunesApi? = null
        val mainHandler = Handler(Looper.getMainLooper())
        var playerAllowed = true
    }

}
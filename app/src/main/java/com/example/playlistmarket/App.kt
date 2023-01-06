package com.example.playlistmarket

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmarket.search.query.ItunesApi
import com.example.playlistmarket.search.query.SearchQuery

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        sharedPref = getSharedPreferences(FILE_NAME, MODE_PRIVATE)
        favoritesFile = getSharedPreferences(FAVORITES_LIST_KEY, MODE_PRIVATE)
    }

    companion object {
        const val SEARCH_CONTENT_EDIT_TEXT_KEY = "searchContentEditText"
        const val FILE_NAME = "MP_preferences"
        const val DARK_MODE_STATUS_KEY = "dark_mode_status"
        const val RECENT_TRACKS_LIST_KEY = "recent_tracks_list"
        const val FAVORITES_LIST_KEY = "favorites"
        const val PLAYLIST_TITLES= "playlists"
        const val SEARCH_TRACKS_BASE_URL = "https://itunes.apple.com"
        const val TRACK_KEY = "track"
        const val TRACK_ADD = "add_track"
        const val TRACK_DELETE = "delete_track"

        lateinit var appContext: Context
        lateinit var sharedPref: SharedPreferences
        lateinit var favoritesFile: SharedPreferences

        var searchQuery: SearchQuery<ItunesApi>? = null
    }

}
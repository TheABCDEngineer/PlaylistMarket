package com.example.playlistmarket

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.playlistmarket.base.domain.repository.SettingsRepository
import com.example.playlistmarket.di.networkModule
import com.example.playlistmarket.di.playerModule
import com.example.playlistmarket.di.baseModule
import com.example.playlistmarket.di.mainModule
import com.example.playlistmarket.di.mediaLibraryModule
import com.example.playlistmarket.di.searchModule
import com.example.playlistmarket.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        startKoin {
            androidContext(this@App)
            modules(networkModule, mainModule, baseModule, playerModule, searchModule, settingsModule, mediaLibraryModule)
        }
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
        lateinit var settingsRepository: SettingsRepository
        val mainHandler = Handler(Looper.getMainLooper())
        var playerAllowed = true
    }

}








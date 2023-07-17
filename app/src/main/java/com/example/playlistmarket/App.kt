package com.example.playlistmarket

import android.app.Application
import android.content.Context
import com.example.playlistmarket.di.networkModule
import com.example.playlistmarket.di.playerModule
import com.example.playlistmarket.di.rootModule
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
            modules(networkModule, rootModule, playerModule, searchModule, settingsModule, mediaLibraryModule)
        }
    }

    companion object {
        const val SETTINGS_FILE_NAME = "APP_preferences"
        const val THEME_MODE_STATUS_KEY = "theme_mode_status"
        const val RECENT_TRACKS_KEY = "recent_tracks"
        const val FAVORITES_UNIQUE_KEY = " favorites//SYSTEM_PLAYLIST "
        const val SEARCH_TRACKS_BASE_URL = "https://itunes.apple.com"
        const val TRACK_KEY = "track"
        const val CLICK_DEBOUNCE_DELAY = 200L

        lateinit var appContext: Context

    }

}








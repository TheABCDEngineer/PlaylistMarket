package com.example.playlistmarket.features.main.presenter

import android.content.Intent
import com.example.playlistmarket.creator.Presenter
import com.example.playlistmarket.App
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.clickDebounce
import com.example.playlistmarket.data.repository.storage.SettingsStorageRepository
import com.example.playlistmarket.features.medialibrary.ui.MediaLibraryActivity
import com.example.playlistmarket.features.search.ui.SearchActivity
import com.example.playlistmarket.features.setting.ui.SettingsActivity

class MainPresenter(
    var view: MainView,
    private val settingsStorage: SettingsStorageRepository
) : Presenter() {
    private var isSearchAllowed = true
    private var isMediaLibraryAllowed = true
    private var isSettingAllowed = true

    init {
        view.setDarkTheme(settingsStorage.getDarkModeStatusValue())
    }

    fun startSearchScreen() {
        if (!clickDebounce(isSearchAllowed) { isSearchAllowed = it }) return
        val intent = Intent(App.appContext, SearchActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        App.appContext.startActivity(intent)
    }

    fun startMediaLibraryScreen() {
        if (!clickDebounce(isMediaLibraryAllowed) { isMediaLibraryAllowed = it }) return
        val intent = Intent(App.appContext, MediaLibraryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        App.appContext.startActivity(intent)
    }

    fun startSettingsScreen() {
        if (!clickDebounce(isSettingAllowed) { isSettingAllowed = it }) return
        val intent = Intent(App.appContext, SettingsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        App.appContext.startActivity(intent)
    }

    override fun implementPresenterDestroyingMethod(): () -> Unit = {
        Creator.mainPresenter = null
    }
}
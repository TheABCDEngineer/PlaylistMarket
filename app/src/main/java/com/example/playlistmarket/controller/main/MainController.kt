package com.example.playlistmarket.controller.main

import android.content.Intent
import com.example.playlistmarket.controller.Controller
import com.example.playlistmarket.creator.App
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.clickDebounce
import com.example.playlistmarket.data.interactors.storage.SettingsStorageInteractor
import com.example.playlistmarket.ui.medialibrary.MediaLibraryActivity
import com.example.playlistmarket.ui.search.SearchActivity
import com.example.playlistmarket.ui.settings.SettingsActivity

class MainController(
    var view: MainView,
    private val settingsStorage: SettingsStorageInteractor
) : Controller() {
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

    override fun implementControllerDestroyingMethod(): () -> Unit = {
        Creator.mainController = null
    }
}
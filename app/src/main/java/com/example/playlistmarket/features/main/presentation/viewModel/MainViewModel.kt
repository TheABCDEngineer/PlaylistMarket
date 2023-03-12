package com.example.playlistmarket.features.main.presentation.viewModel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmarket.App
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.clickDebounce
import com.example.playlistmarket.features.main.domain.repository.SettingsRepository
import com.example.playlistmarket.features.medialibrary.presentation.ui.MediaLibraryActivity
import com.example.playlistmarket.features.search.presentation.ui.SearchActivity
import com.example.playlistmarket.features.setting.presentation.ui.SettingsActivity

class MainViewModel(
    private val settingsStorage: SettingsRepository
) : ViewModel() {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer { Creator.createMainViewModel() }
        }
    }

    private var isSearchAllowed = true
    private var isMediaLibraryAllowed = true
    private var isSettingAllowed = true

    private val darkThemeLiveData = MutableLiveData<Boolean>()
    fun darkThemeObserve(): LiveData<Boolean> = darkThemeLiveData

    init {
        darkThemeLiveData.postValue(settingsStorage.getDarkModeStatusValue())
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
}
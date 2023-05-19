package com.example.playlistmarket.root.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.playlistmarket.root.domain.repository.SettingsRepository

class RootViewModel(
    private val settingsStorage: SettingsRepository
) : ViewModel() {

    fun getAppDarkModeValue(): Int {
        return settingsStorage.getThemeModeValue()
    }
}
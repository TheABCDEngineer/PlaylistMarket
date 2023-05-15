package com.example.playlistmarket.base.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.playlistmarket.base.domain.repository.SettingsRepository

class RootViewModel(
    private val settingsStorage: SettingsRepository
) : ViewModel() {

    fun getAppDarkModeValue(): Boolean {
        return settingsStorage.getDarkModeStatusValue()
    }
}
package com.example.playlistmarket.base.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.playlistmarket.App
import com.example.playlistmarket.base.domain.repository.SettingsRepository

class RootViewModel(
    private val settingsStorage: SettingsRepository
) : ViewModel() {

    init {
        App.settingsRepository = settingsStorage
    }

    fun getAppDarkModeValue(): Boolean {
        return App.settingsRepository.getDarkModeStatusValue()
    }
}
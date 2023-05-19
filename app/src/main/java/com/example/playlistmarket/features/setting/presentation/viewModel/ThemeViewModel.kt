package com.example.playlistmarket.features.setting.presentation.viewModel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.example.playlistmarket.root.domain.repository.SettingsRepository

class ThemeViewModel(
    private val settingsStorage: SettingsRepository
) : ViewModel() {

    fun getThemeMode(): Int {
        return settingsStorage.getThemeModeValue()
    }

    fun onUserModeSelected(mode: Int) {
        settingsStorage.putThemeModeValue(mode)
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
package com.example.playlistmarket.base.data.sharedPreferences

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.App
import com.example.playlistmarket.base.domain.repository.SettingsRepository

class SettingsRepositoryImplSharedPreferences(
    private val file: SharedPreferences
) : SettingsRepository {

    override fun getDarkModeStatusValue(): Boolean {
        return file.getBoolean(
            App.DARK_MODE_STATUS_KEY,
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    override fun putDarkModeStatusValue(value: Boolean) {
        file.edit()
            .putBoolean(App.DARK_MODE_STATUS_KEY, value)
            .apply()
    }
}
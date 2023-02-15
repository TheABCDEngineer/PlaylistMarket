package com.example.playlistmarket.data.sharedPreferences

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.creator.App
import com.example.playlistmarket.data.interactors.storage.SettingsStorageInteractor

class SettingsStorageImpSharedPreferences(
    private val file: SharedPreferences
) : SettingsStorageInteractor {

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
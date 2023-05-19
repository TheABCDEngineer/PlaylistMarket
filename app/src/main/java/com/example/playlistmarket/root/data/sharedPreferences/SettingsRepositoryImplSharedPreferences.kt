package com.example.playlistmarket.root.data.sharedPreferences

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.App
import com.example.playlistmarket.root.domain.repository.SettingsRepository

class SettingsRepositoryImplSharedPreferences(
    private val file: SharedPreferences
) : SettingsRepository {

    override fun getThemeModeValue(): Int {
        return file.getInt(
            App.THEME_MODE_STATUS_KEY,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
    }

    override fun putThemeModeValue(value: Int) {
        file.edit()
            .putInt(App.THEME_MODE_STATUS_KEY, value)
            .apply()
    }
}
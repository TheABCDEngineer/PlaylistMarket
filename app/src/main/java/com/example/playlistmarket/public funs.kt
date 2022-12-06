package com.example.playlistmarket

import androidx.appcompat.app.AppCompatDelegate

fun setDarkMode(status:Boolean) {
    when (status) {
        true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
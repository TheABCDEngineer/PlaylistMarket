package com.example.playlistmarket.data.repository

interface SettingsRepository {
    fun getDarkModeStatusValue(): Boolean
    fun putDarkModeStatusValue(value: Boolean)
}
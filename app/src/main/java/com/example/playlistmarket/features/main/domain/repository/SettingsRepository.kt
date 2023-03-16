package com.example.playlistmarket.features.main.domain.repository

interface SettingsRepository {
    fun getDarkModeStatusValue(): Boolean
    fun putDarkModeStatusValue(value: Boolean)
}
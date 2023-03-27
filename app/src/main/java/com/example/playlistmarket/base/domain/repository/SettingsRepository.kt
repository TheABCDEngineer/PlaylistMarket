package com.example.playlistmarket.base.domain.repository

interface SettingsRepository {
    fun getDarkModeStatusValue(): Boolean
    fun putDarkModeStatusValue(value: Boolean)
}
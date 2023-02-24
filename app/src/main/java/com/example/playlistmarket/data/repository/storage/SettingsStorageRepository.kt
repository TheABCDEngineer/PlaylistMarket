package com.example.playlistmarket.data.repository.storage

interface SettingsStorageRepository {
    fun getDarkModeStatusValue(): Boolean
    fun putDarkModeStatusValue(value: Boolean)
}
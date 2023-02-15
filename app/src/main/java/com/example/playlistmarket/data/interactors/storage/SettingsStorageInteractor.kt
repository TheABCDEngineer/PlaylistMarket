package com.example.playlistmarket.data.interactors.storage

interface SettingsStorageInteractor {
    fun getDarkModeStatusValue(): Boolean
    fun putDarkModeStatusValue(value: Boolean)
}
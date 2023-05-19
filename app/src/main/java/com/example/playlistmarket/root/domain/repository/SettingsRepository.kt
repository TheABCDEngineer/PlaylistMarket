package com.example.playlistmarket.root.domain.repository

interface SettingsRepository {
    fun getThemeModeValue(): Int
    fun putThemeModeValue(value: Int)
}
package com.example.playlistmarket.base.domain.repository

interface SettingsRepository {
    fun getThemeModeValue(): Int
    fun putThemeModeValue(value: Int)
}
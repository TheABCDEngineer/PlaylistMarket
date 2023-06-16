package com.example.playlistmarket.di

import com.example.playlistmarket.features.setting.presentation.viewModel.SettingsViewModel
import com.example.playlistmarket.features.setting.presentation.viewModel.ThemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsModule = module {

    viewModelOf(::SettingsViewModel).bind()

    viewModelOf(::ThemeViewModel).bind()
}
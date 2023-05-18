package com.example.playlistmarket.di

import com.example.playlistmarket.features.setting.presentation.viewModel.SettingsViewModel
import com.example.playlistmarket.features.setting.presentation.viewModel.ThemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        ThemeViewModel(get())
    }
}
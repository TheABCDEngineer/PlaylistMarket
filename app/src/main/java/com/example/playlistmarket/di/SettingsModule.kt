package com.example.playlistmarket.di

import com.example.playlistmarket.features.setting.presentation.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {

    viewModel {
        SettingsViewModel(get())
    }
}
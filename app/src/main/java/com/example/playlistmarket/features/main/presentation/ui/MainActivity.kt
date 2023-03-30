package com.example.playlistmarket.features.main.presentation.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmarket.R
import com.example.playlistmarket.features.main.presentation.viewModel.MainViewModel
import com.example.playlistmarket.base.setDarkMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val searchButton: Button by lazy { findViewById(R.id.main_SearchButton) }
    private val mediaLibraryButton: Button by lazy { findViewById(R.id.main_MediaLibraryButton) }
    private val settingsButton: Button by lazy { findViewById(R.id.main_SettingsButton) }
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        viewModel.darkThemeObserve().observe(this){
            setDarkTheme(it)
        }

        searchButton.setOnClickListener {
            viewModel.startSearchScreen()
        }

        mediaLibraryButton.setOnClickListener {
            viewModel.startMediaLibraryScreen()
        }

        settingsButton.setOnClickListener {
            viewModel.startSettingsScreen()
        }
    }

    private fun setDarkTheme(isDarkTheme: Boolean) {
        setDarkMode(isDarkTheme)
    }
}
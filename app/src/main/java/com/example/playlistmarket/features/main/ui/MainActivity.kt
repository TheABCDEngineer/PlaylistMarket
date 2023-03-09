package com.example.playlistmarket.features.main.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmarket.R
import com.example.playlistmarket.features.main.viewModel.MainViewModel
import com.example.playlistmarket.creator.setDarkMode

class MainActivity : AppCompatActivity() {
    private val searchButton: Button by lazy { findViewById(R.id.main_SearchButton) }
    private val mediaLibraryButton: Button by lazy { findViewById(R.id.main_MediaLibraryButton) }
    private val settingsButton: Button by lazy { findViewById(R.id.main_SettingsButton) }
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModel.getViewModelFactory())[MainViewModel::class.java] }

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
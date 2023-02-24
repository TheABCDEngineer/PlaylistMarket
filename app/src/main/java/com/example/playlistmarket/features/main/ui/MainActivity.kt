package com.example.playlistmarket.features.main.ui

import android.os.Bundle
import android.widget.Button
import com.example.playlistmarket.R
import com.example.playlistmarket.features.main.presenter.MainPresenter
import com.example.playlistmarket.features.main.presenter.MainView
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.setDarkMode
import com.example.playlistmarket.creator.ActivityByPresenter

class MainActivity : ActivityByPresenter(), MainView {
    private val searchButton: Button by lazy { findViewById(R.id.main_SearchButton) }
    private val mediaLibraryButton: Button by lazy { findViewById(R.id.main_MediaLibraryButton) }
    private val settingsButton: Button by lazy { findViewById(R.id.main_SettingsButton) }
    override val presenter: MainPresenter by lazy { Creator.mainPresenter!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        createPresenter()

        searchButton.setOnClickListener {
            presenter.startSearchScreen()
        }

        mediaLibraryButton.setOnClickListener {
            presenter.startMediaLibraryScreen()
        }

        settingsButton.setOnClickListener {
            presenter.startSettingsScreen()
        }
    }

    override fun createPresenter() {
        Creator.createMainPresenter(this)
    }

    override fun setDarkTheme(isDarkTheme: Boolean) {
        setDarkMode(isDarkTheme)
    }
}
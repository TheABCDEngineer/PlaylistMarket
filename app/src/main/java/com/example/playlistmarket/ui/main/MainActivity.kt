package com.example.playlistmarket.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.playlistmarket.R
import com.example.playlistmarket.controller.main.MainController
import com.example.playlistmarket.controller.main.MainView
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.setDarkMode
import com.example.playlistmarket.ui.ControlledActivity

class MainActivity : AppCompatActivity(), MainView, ControlledActivity {
    private val searchButton: Button by lazy { findViewById(R.id.main_SearchButton) }
    private val mediaLibraryButton: Button by lazy { findViewById(R.id.main_MediaLibraryButton) }
    private val settingsButton: Button by lazy { findViewById(R.id.main_SettingsButton) }
    override val controller: MainController by lazy { Creator.mainController!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        createController()

        searchButton.setOnClickListener {
            controller.startSearchScreen()
        }

        mediaLibraryButton.setOnClickListener {
            controller.startMediaLibraryScreen()
        }

        settingsButton.setOnClickListener {
            controller.startSettingsScreen()
        }
    }

    override fun createController() {
        Creator.createMainController(this)
    }

    override fun setDarkTheme(isDarkTheme: Boolean) {
        setDarkMode(isDarkTheme)
    }

    override fun onResume() {
        super<AppCompatActivity>.onResume()
        super<ControlledActivity>.onResume()
    }

    override fun onBackPressed() {
        super<ControlledActivity>.onBackPressed()
        super<AppCompatActivity>.onBackPressed()
    }

    override fun onStop() {
        super<ControlledActivity>.onStop()
        super<AppCompatActivity>.onStop()
    }

    override fun onDestroy() {
        super<ControlledActivity>.onDestroy()
        super<AppCompatActivity>.onDestroy()
    }
}
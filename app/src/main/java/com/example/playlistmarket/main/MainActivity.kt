package com.example.playlistmarket.main

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.medialibrary.MediaLibraryActivity
import com.example.playlistmarket.R
import com.example.playlistmarket.getSharePreferences
import com.example.playlistmarket.search.SearchActivity
import com.example.playlistmarket.setDarkMode
import com.example.playlistmarket.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var buttonSearch: Button
    private lateinit var buttonMediaLibrary: Button
    private lateinit var buttonSetting: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        initializeVariables()

        setOnClickListenersAtViews()

        val darkModeKey = getString(R.string.dark_mode_status_key)
        val file = getSharePreferences()
        setDarkMode(
            file.getBoolean(
                darkModeKey,
                AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            )
        )
    }

    private fun initializeVariables() {
        buttonSearch = findViewById(R.id.main_SearchButton)
        buttonMediaLibrary = findViewById(R.id.main_MediaLibraryButton)
        buttonSetting = findViewById(R.id.main_SettingsButton)
    }

    private fun setOnClickListenersAtViews() {
        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        buttonMediaLibrary.setOnClickListener {
            val displayIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(displayIntent)
        }

        buttonSetting.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}
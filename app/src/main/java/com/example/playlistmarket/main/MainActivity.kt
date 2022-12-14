package com.example.playlistmarket.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.App
import com.example.playlistmarket.medialibrary.MediaLibraryActivity
import com.example.playlistmarket.R
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

        val file = App.sharedPref
        setDarkMode(
            file.getBoolean(
                App.DARK_MODE_STATUS_KEY,
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
package com.example.playlistmarket

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    private lateinit var buttonSearch: Button
    private lateinit var buttonMediaLibrary: Button
    private lateinit var buttonSetting: Button

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var fileName: String
    private lateinit var darkModeKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        initializeVariables()

        setOnClickListenersAtViews()

        setDarkMode(
            sharedPrefs.getBoolean(
                darkModeKey,
                AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            )
        )
    }

    private fun initializeVariables() {
        buttonSearch = findViewById(R.id.main_SearchButton)
        buttonMediaLibrary = findViewById(R.id.main_MediaLibraryButton)
        buttonSetting = findViewById(R.id.main_SettingsButton)

        fileName = getString(R.string.app_preference_file_name)
        darkModeKey = getString(R.string.dark_mode_status_key)
        sharedPrefs = getSharedPreferences(fileName, MODE_PRIVATE)
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
package com.example.playlistmarket.medialibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.playlistmarket.R

class MediaLibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_library)
        supportActionBar?.hide()

        val goBackButton = findViewById<ImageView>(R.id.media_library_goBack)

        goBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}
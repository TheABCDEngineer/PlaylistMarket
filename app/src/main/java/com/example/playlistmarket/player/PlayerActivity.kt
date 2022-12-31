package com.example.playlistmarket.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.Track
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {
    private lateinit var goBackButton: ImageButton

    private lateinit var playButton: ImageButton
    private var isTrackPlaying = false

    private lateinit var addToPlaylistButton: ImageButton
    private var isTrackAtPlaylist = false

    private lateinit var addToFavoritesButton: ImageButton
    private var isTrackAtFavorites = false

    private lateinit var playerTrackArtwork: ImageView
    private lateinit var playerTrackName: TextView
    private lateinit var playerArtistName: TextView
    private lateinit var playerTimer: TextView

    private lateinit var infoTrackLenght: TextView
    private lateinit var infoTrackAlbum: TextView
    private lateinit var infoTrackRelease: TextView
    private lateinit var infoTrackGenre: TextView
    private lateinit var infoTrackCountry: TextView

    private lateinit var track: Track
    //private lateinit var track2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        supportActionBar?.hide()

        initializeVariables()

        setOnClickListenersAtViews()

        showTrackProperties()
    }

    private fun initializeVariables() {
        goBackButton = findViewById(R.id.player_back_button)
        playButton = findViewById(R.id.player_play_button)
        addToPlaylistButton = findViewById(R.id.player_add_to_playlist_button)
        addToFavoritesButton = findViewById(R.id.player_add_to_favorites_button)

        playerTrackArtwork = findViewById(R.id.player_artwork)
        playerTrackName = findViewById(R.id.player_track_name)
        playerArtistName = findViewById(R.id.player_artist_name)
        playerTimer = findViewById(R.id.player_track_timer)

        infoTrackLenght = findViewById(R.id.player_track_value_length)
        infoTrackAlbum = findViewById(R.id.player_track_value_album)
        infoTrackRelease = findViewById(R.id.player_track_value_release)
        infoTrackGenre = findViewById(R.id.player_track_value_genre)
        infoTrackCountry = findViewById(R.id.player_track_value_country)

        track = intent.getParcelableExtra(getString(R.string.intent_extra_track_key))!!
    }

    private fun setOnClickListenersAtViews() {
        goBackButton.setOnClickListener {
            onBackPressed()
        }

        addToPlaylistButton.setOnClickListener {
            isTrackAtPlaylist = !isTrackAtPlaylist
            setViewOnTrackAtPlaylistStatus(isTrackAtPlaylist)
        }

        addToFavoritesButton.setOnClickListener {
            isTrackAtFavorites = !isTrackAtFavorites
            setViewOnTrackAtFavoritesStatus(isTrackAtFavorites)
        }

        playButton.setOnClickListener {
            isTrackPlaying = !isTrackPlaying
            setViewOnTrackPlayingStatus(isTrackPlaying)
        }
    }

    private fun setViewOnTrackAtPlaylistStatus(isAtPlaylist: Boolean) {
        if (isAtPlaylist) {
            addToPlaylistButton.setImageResource(R.drawable.player_add_to_playlist_done)
        } else {
            addToPlaylistButton.setImageResource(R.drawable.player_add_to_playlist_available)
        }
    }

    private fun setViewOnTrackAtFavoritesStatus(isAtFavorites: Boolean) {
        if (isAtFavorites) {
            addToFavoritesButton.setImageResource(R.drawable.player_add_to_favorites_done)
        } else {
            addToFavoritesButton.setImageResource(R.drawable.player_add_to_favorites_available)
        }
    }

    private fun setViewOnTrackPlayingStatus(isPlaying: Boolean) {
        if (isPlaying) {
            playButton.setImageResource(R.drawable.player_pause_icon)
        } else {
            playButton.setImageResource(R.drawable.player_play_icon)
        }
    }

    private fun showTrackProperties() {
        Glide.with(applicationContext)
            .load(track.getArtwork(512))
            .centerCrop()
            .placeholder(R.drawable.default_album_image)
            .into(playerTrackArtwork)

        playerTrackName.text = track.getTrackName()
        playerArtistName.text = track.getArtistName()
        playerTimer.text = track.getTrackTime("mm:ss")

        infoTrackLenght.text = track.getTrackTime("mm:ss")
        infoTrackAlbum.text = track.getCollectionName()
        infoTrackRelease.text = track.getYearFromReleaseDate()
        infoTrackGenre.text = track.getGenre()
        infoTrackCountry.text = track.getCountry()
    }
}
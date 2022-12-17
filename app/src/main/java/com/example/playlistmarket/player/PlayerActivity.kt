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
    private lateinit var addToPlaylistButton: ImageButton
    private lateinit var addToFavoritesButton: ImageButton

    private lateinit var playerTrackArtwork: ImageView
    private lateinit var playerTrackName: TextView
    private lateinit var playerArtistName: TextView
    private lateinit var playerTimer: TextView

    private lateinit var infoTrackLenght: TextView
    private lateinit var infoTrackAlbum: TextView
    private lateinit var infoTrackRelease: TextView
    private lateinit var infoTrackGenre: TextView
    private lateinit var infoTrackCountry: TextView

    private lateinit var trackHandle: TrackHandle

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

        trackHandle = TrackHandle(
            Gson().fromJson(intent.getStringExtra(getString(R.string.intent_extra_track_key)), Track::class.java)
        )
    }

    private fun setOnClickListenersAtViews() {
        goBackButton.setOnClickListener {
            onBackPressed()
        }

        addToPlaylistButton.setOnClickListener {
            trackHandle.isTrackAtPlaylist = !trackHandle.isTrackAtPlaylist
            setViewOnTrackAtPlaylistStatus(trackHandle.isTrackAtPlaylist)
        }

        addToFavoritesButton.setOnClickListener {
            trackHandle.isTrackAtFavorites = !trackHandle.isTrackAtFavorites
            setViewOnTrackAtFavoritesStatus(trackHandle.isTrackAtFavorites)
        }

        playButton.setOnClickListener {
            trackHandle.isTrackPlaying = !trackHandle.isTrackPlaying
            setViewOnTrackPlayingStatus(trackHandle.isTrackPlaying)
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
        val track = trackHandle.track

        Glide.with(applicationContext)
            .load(track.getCoverArtwork())
            .centerCrop()
            .placeholder(R.drawable.default_album_image)
            .into(playerTrackArtwork)

        playerTrackName.text = track.trackName
        playerArtistName.text = track.artistName
        playerTimer.text = track.formatTrackTimeFromMillis()

        infoTrackLenght.text = track.formatTrackTimeFromMillis()
        infoTrackAlbum.text = track.collectionName
        infoTrackRelease.text = track.getYearFromReleaseDate()
        infoTrackGenre.text = track.primaryGenreName
        infoTrackCountry.text = track.country
    }

}
package com.example.playlistmarket.player.widgets

import android.media.MediaPlayer
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.playlistmarket.App
import com.example.playlistmarket.medialibrary.Track
import com.example.playlistmarket.R
import com.example.playlistmarket.convertMSecToClockFormat
import kotlinx.coroutines.Runnable

class TrackHandleWidget(
    private val track: Track,
    private val playButton: ImageButton,
    private val addToPlaylistButton: ImageButton,
    private val addToFavoritesButton: ImageButton,
    private val playerTimerView: TextView,
    private val progressBar: ProgressBar
) {
    lateinit var playerTimer: Runnable
    val mediaPlayer = MediaPlayer()

    var isTrackAtFavorites = false
        set(value) {
            field = value
            setOnTrackAtFavoritesStatus(value)
        }

    var isTrackAtPlaylist = false
        set(value) {
            field = value
            setOnTrackAtPlaylistStatus(value)
        }
    var isTrackPlaying = false
        set(value) {
            field = value
            setOnTrackPlayingStatus(value)
        }

    init {
        playButton.isEnabled = false
        setOnClickListenersAtViews()
        preparePlayer()
    }

    private fun setOnClickListenersAtViews() {
        addToPlaylistButton.setOnClickListener {
            isTrackAtPlaylist = !isTrackAtPlaylist
        }

        addToFavoritesButton.setOnClickListener {
            isTrackAtFavorites = !isTrackAtFavorites
        }

        playButton.setOnClickListener {
            isTrackPlaying = !isTrackPlaying
        }
    }

    private fun setOnTrackAtPlaylistStatus(isAtPlaylist: Boolean) {
        if (isAtPlaylist) {
            addToPlaylistButton.setImageResource(R.drawable.player_add_to_playlist_done)
        } else {
            addToPlaylistButton.setImageResource(R.drawable.player_add_to_playlist_available)
        }
    }

    private fun setOnTrackAtFavoritesStatus(isAtFavorites: Boolean) {
        if (isAtFavorites) {
            addToFavoritesButton.setImageResource(R.drawable.player_add_to_favorites_done)
        } else {
            addToFavoritesButton.setImageResource(R.drawable.player_add_to_favorites_available)
        }
    }

    private fun setOnTrackPlayingStatus(isPlaying: Boolean) {
        if (isPlaying) {
            playButton.setImageResource(R.drawable.player_pause_icon)
            App.mainHandler.post(playerTimer)
            mediaPlayer.start()
        } else {
            playButton.setImageResource(R.drawable.player_play_icon)
            App.mainHandler.removeCallbacks(playerTimer)
            if (mediaPlayer.isPlaying) mediaPlayer.pause()
        }
    }

    private fun setPlayerTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (mediaPlayer.currentPosition < mediaPlayer.duration) {
                    playerTimerView.text = convertMSecToClockFormat(
                        (mediaPlayer.duration - mediaPlayer.currentPosition).toString()
                    )
                    App.mainHandler.postDelayed(this, 1000L)
                }
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.getPreviewUrl())
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            progressBar.isVisible = false
            playerTimerView.text = convertMSecToClockFormat(mediaPlayer.duration.toString())
            playerTimer = setPlayerTimer()
            playButton.isEnabled = true
        }
        mediaPlayer.setOnCompletionListener {
            App.mainHandler.removeCallbacks(playerTimer)
            playerTimerView.text = convertMSecToClockFormat(mediaPlayer.duration.toString())
            isTrackPlaying = false
        }
    }
}
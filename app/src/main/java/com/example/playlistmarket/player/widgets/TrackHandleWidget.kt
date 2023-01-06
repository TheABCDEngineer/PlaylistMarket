package com.example.playlistmarket.player.widgets

import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmarket.medialibrary.Track
import com.example.playlistmarket.R
import com.example.playlistmarket.player.PlayerActivity

class TrackHandleWidget(
    private val track: Track,
    activity: PlayerActivity,
    playButtonId: Int,
    addToPlaylistButtonId: Int,
    addToFavoritesButtonId: Int,
    playerTimerId:Int
) {
    private val playButton: ImageButton = activity.findViewById(playButtonId)
    private val addToPlaylistButton: ImageButton = activity.findViewById(addToPlaylistButtonId)
    private val addToFavoritesButton: ImageButton = activity.findViewById(addToFavoritesButtonId)
    private val playerTimer: TextView = activity.findViewById(playerTimerId)

    var isTrackAtFavorites = false
        set(value) {
            field = value
            setViewOnTrackAtFavoritesStatus(value)
        }

    var isTrackAtPlaylist = false
        set(value) {
            field = value
            setViewOnTrackPlayingStatus(value)
        }
    private var isTrackPlaying = false

    init {
        setOnClickListenersAtViews()
        setTrackTimer()
    }

    private fun setOnClickListenersAtViews() {
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

    fun setTrackTimer() {
        playerTimer.text = track.getTrackTime("mm:ss")
    }
}
package com.example.playlistmarket.player.widgets

import android.os.CountDownTimer
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmarket.medialibrary.Track
import com.example.playlistmarket.R
import com.example.playlistmarket.convertMSecToClockFormat
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
            setViewOnTrackAtPlaylistStatus(value)
        }
    private var isTrackPlaying = false
        set(value) {
            field = value
            setViewOnTrackPlayingStatus(value)
        }

    private lateinit var timer: CountDownTimer
    private var timeCount = track.getFormattedTrackTime(false).toLong()

    init {
        setOnClickListenersAtViews()
        playerTimer.text = track.getFormattedTrackTime(true)
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
            setTrackTimer(timeCount)
            if (track.getFormattedTrackTime(false).toLong() > 0) timer.start()
        } else {
            playButton.setImageResource(R.drawable.player_play_icon)
            if (track.getFormattedTrackTime(false).toLong() > 0) timer.cancel()
        }
    }

    private fun setTrackTimer(timeInMillis: Long) {
        if (timeInMillis <= 0) return
        timer = object : CountDownTimer(timeInMillis,1000) {
            override fun onTick(millisUntilFinished: Long) {
                playerTimer.text =  convertMSecToClockFormat(millisUntilFinished.toString())
                timeCount = millisUntilFinished
            }
            override fun onFinish() {
                isTrackPlaying = false
                playerTimer.text = track.getFormattedTrackTime(true)
                timeCount = track.getFormattedTrackTime(false).toLong()
            }
        }
    }
}
package com.example.playlistmarket.features.player.domain

import android.media.MediaPlayer
import com.example.playlistmarket.features.player.viewModel.enums.PlayerPlayback
import com.example.playlistmarket.features.player.viewModel.interactors.PlaybackInteractor
import com.example.playlistmarket.App
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.domain.dataConverter.TrackConverter
import com.example.playlistmarket.domain.model.Track

class PlaybackImpMediaPlayer(
    private val track: Track,
    override val player: MediaPlayer
) : PlaybackInteractor {

    private val playerTimer: Runnable by lazy { setPlayerTimer() }
    private var isPlayerPrepared = false
    private lateinit var observer: Observer

    override fun addObserver(observer: Observer) {
        this.observer = observer
    }

    override fun preparePlayer() {
        observer.notifyObserver(PlayerPlayback.IS_PREPARED, false)
        player.setDataSource(track.previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            observer.notifyObserver(PlayerPlayback.IS_PREPARED, true)
            observer.notifyObserver(
                PlayerPlayback.PLAYBACK_TIMER,
                TrackConverter.convertMSecToClockFormat(player.duration)
            )
            isPlayerPrepared = true
        }
        player.setOnCompletionListener {
            App.mainHandler.removeCallbacks(playerTimer)
            observer.notifyObserver(PlayerPlayback.PLAYBACK_FINISHED, false)
        }
    }

    override fun getPrepareStatus() = isPlayerPrepared

    override fun setPlayerTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (player.currentPosition < player.duration) {
                    observer.notifyObserver(
                        PlayerPlayback.PLAYBACK_TIMER,
                        TrackConverter.convertMSecToClockFormat(
                            (player.duration - player.currentPosition)
                        )
                    )
                    App.mainHandler.postDelayed(this, 1000L)
                }
            }
        }
    }

    override fun executePlayback(isPlaying: Boolean) {
        if (isPlaying) {
            App.mainHandler.post(playerTimer)
            player.start()
        } else {
            App.mainHandler.removeCallbacks(playerTimer)
            if (player.isPlaying) player.pause()
        }
    }

    override fun getPlaybackDuration() = TrackConverter.convertMSecToClockFormat(player.duration)

    override fun destroyPlayer() {
        player.release()
        App.mainHandler.removeCallbacks(playerTimer)
    }
}
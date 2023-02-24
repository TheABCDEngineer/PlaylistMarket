package com.example.playlistmarket.features.player.domain

import android.media.MediaPlayer
import com.example.playlistmarket.features.player.presenter.enums.PlayerPlayback
import com.example.playlistmarket.features.player.presenter.interactors.PlaybackInteractor
import com.example.playlistmarket.App
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.domain.dataConverter.TrackConverter
import com.example.playlistmarket.domain.models.Track

class PlaybackImpMediaPlayer(
    private val track: Track,
    override val player: MediaPlayer
) : PlaybackInteractor {

    private val playerTimer: Runnable by lazy { setPlayerTimer() }
    private var isPlayerPrepared = false
    private lateinit var controller: Observer

    override fun addObserver(observer: Observer) {
        controller = observer
    }

    override fun preparePlayer() {
        controller.notifyObserver(PlayerPlayback.IS_PREPARED, false)
        player.setDataSource(track.previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            controller.notifyObserver(PlayerPlayback.IS_PREPARED, true)
            controller.notifyObserver(
                PlayerPlayback.PLAYBACK_TIMER,
                TrackConverter.convertMSecToClockFormat(player.duration)
            )
            isPlayerPrepared = true
        }
        player.setOnCompletionListener {
            App.mainHandler.removeCallbacks(playerTimer)
            controller.notifyObserver(PlayerPlayback.PLAYBACK_FINISHED, true)
        }
    }

    override fun getPrepareStatus() = isPlayerPrepared

    override fun setPlayerTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                if (player.currentPosition < player.duration) {
                    controller.notifyObserver(
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
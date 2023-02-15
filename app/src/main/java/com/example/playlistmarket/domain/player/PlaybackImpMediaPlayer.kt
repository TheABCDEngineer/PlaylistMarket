package com.example.playlistmarket.domain.player

import android.media.MediaPlayer
import com.example.playlistmarket.controller.player.enums.PlayerPlayback
import com.example.playlistmarket.controller.player.interactors.PlaybackInteractor
import com.example.playlistmarket.creator.App
import com.example.playlistmarket.creator.convertMSecToClockFormat
import com.example.playlistmarket.creator.interactors.Observer
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
        player.setDataSource(track.getPreviewUrl())
        player.prepareAsync()
        player.setOnPreparedListener {
            controller.notifyObserver(PlayerPlayback.IS_PREPARED, true)
            controller.notifyObserver(
                PlayerPlayback.PLAYBACK_TIMER,
                convertMSecToClockFormat(player.duration.toString())
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
                        PlayerPlayback.PLAYBACK_TIMER, convertMSecToClockFormat(
                            (player.duration - player.currentPosition).toString()
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

    override fun getPlaybackDuration() = convertMSecToClockFormat(player.duration.toString())

    override fun destroyPlayer() {
        player.release()
        App.mainHandler.removeCallbacks(playerTimer)
    }
}
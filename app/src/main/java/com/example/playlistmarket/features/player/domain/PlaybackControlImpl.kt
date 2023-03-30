package com.example.playlistmarket.features.player.domain

import com.example.playlistmarket.features.player.domain.enums.PlayerPlayback
import com.example.playlistmarket.features.player.domain.interactors.PlaybackControlInteractor
import com.example.playlistmarket.App
import com.example.playlistmarket.base.observe.Observer
import com.example.playlistmarket.base.domain.util.convertMSecToClockFormat
import com.example.playlistmarket.features.player.domain.drivers.UrlTrackPlayer

class PlaybackControlImpl(
    trackUrl: String,
    private val player: UrlTrackPlayer
) : PlaybackControlInteractor {

    private val playerTimerAction: Runnable by lazy { createPlayerTimerAction() }
    private lateinit var observer: Observer

    override fun addObserver(observer: Observer) {
        this.observer = observer
    }

    init {
        player.playerReadyToUse = {
            observer.notifyObserver(
                PlayerPlayback.IS_PREPARED,
                convertMSecToClockFormat(player.getTrackDuration())
            )
        }
        player.playbackIsFinished = {
            App.mainHandler.removeCallbacks(playerTimerAction)
            observer.notifyObserver(
                PlayerPlayback.IS_FINISHED,
                convertMSecToClockFormat(player.getTrackDuration())
            )
        }
        player.setTrackUrl(trackUrl)
    }

    private fun createPlayerTimerAction(): Runnable {
        return object : Runnable {
            override fun run() {
                val timeRemainedToFinished = player.getTrackDuration() - player.getPlaybackCurrentPosition()
                if (timeRemainedToFinished > 0) {
                    observer.notifyObserver(
                        PlayerPlayback.TIMER_ACTION,
                        convertMSecToClockFormat(timeRemainedToFinished)
                    )
                    App.mainHandler.postDelayed(this, 1000L)
                }
            }
        }
    }

    override fun start() {
        App.mainHandler.post(playerTimerAction)
        player.startPlayback()
    }

    override fun paused() {
        App.mainHandler.removeCallbacks(playerTimerAction)
        player.stopPlayback()
    }

    override fun releaseResources() {
        player.releasePlayerResources()
        App.mainHandler.removeCallbacks(playerTimerAction)
    }
}
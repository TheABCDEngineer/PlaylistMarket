package com.example.playlistmarket.features.player.data

import android.media.MediaPlayer
import com.example.playlistmarket.features.player.domain.drivers.UrlTrackPlayer

class UrlTrackPlayerImplMediaPlayer : UrlTrackPlayer {
    private val player = MediaPlayer()

    override lateinit var playerReadyToUse: () -> Unit
    override lateinit var playbackIsFinished: () -> Unit

    override fun setTrackUrl(url: String) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerReadyToUse.invoke()
        }
        player.setOnCompletionListener {
            playbackIsFinished.invoke()
        }
    }

    override fun getTrackDuration(): Int = player.duration

    override fun getPlaybackCurrentPosition(): Int = player.currentPosition

    override fun startPlayback() {
        player.start()
    }

    override fun stopPlayback() {
        player.pause()
    }

    override fun releasePlayerResources() {
        player.release()
    }

}
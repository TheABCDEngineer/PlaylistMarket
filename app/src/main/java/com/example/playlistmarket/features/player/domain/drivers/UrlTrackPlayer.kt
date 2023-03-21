package com.example.playlistmarket.features.player.domain.drivers

interface UrlTrackPlayer {
    var playerReadyToUse: () -> Unit
    var playbackIsFinished: () -> Unit

    fun setTrackUrl(url: String)
    fun getTrackDuration(): Int
    fun getPlaybackCurrentPosition(): Int
    fun startPlayback()
    fun stopPlayback()
    fun releasePlayerResources()
}
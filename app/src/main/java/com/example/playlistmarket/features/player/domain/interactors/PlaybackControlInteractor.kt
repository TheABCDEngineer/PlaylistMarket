package com.example.playlistmarket.features.player.domain.interactors

import com.example.playlistmarket.root.observe.Observable

interface PlaybackControlInteractor : Observable {
    fun start()
    fun paused()
    fun releaseResources()
}
package com.example.playlistmarket.features.player.domain.interactors

import com.example.playlistmarket.base.observe.Observable

interface PlaybackControlInteractor : Observable {
    fun start()
    fun paused()
    fun releaseResources()
}
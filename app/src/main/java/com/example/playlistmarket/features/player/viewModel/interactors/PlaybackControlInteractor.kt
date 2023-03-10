package com.example.playlistmarket.features.player.viewModel.interactors

import com.example.playlistmarket.creator.observe.Observable
import com.example.playlistmarket.creator.observe.Observer

interface PlaybackControlInteractor : Observable {
    fun start()
    fun paused()
    fun releaseResources()
}
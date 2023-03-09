package com.example.playlistmarket.features.player.viewModel.interactors

import com.example.playlistmarket.creator.observe.Observable
import com.example.playlistmarket.creator.observe.Observer

interface PlaybackInteractor : Observable {
    val player: Any

    fun preparePlayer()
    fun setPlayerTimer(): Runnable
    fun getPrepareStatus(): Boolean
    fun executePlayback(isPlaying: Boolean)
    fun getPlaybackDuration(): String
    fun destroyPlayer()
    override fun addObserver(observer: Observer)
}
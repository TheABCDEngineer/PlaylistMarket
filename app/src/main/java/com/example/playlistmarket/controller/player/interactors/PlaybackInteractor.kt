package com.example.playlistmarket.controller.player.interactors

import com.example.playlistmarket.creator.interactors.Observable
import com.example.playlistmarket.creator.interactors.Observer

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
package com.example.playlistmarket.creator.observe

interface Observer {
    fun <S, T> notifyObserver(event: S?, data: T?)
}
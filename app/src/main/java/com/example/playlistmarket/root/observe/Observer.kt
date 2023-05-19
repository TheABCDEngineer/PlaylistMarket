package com.example.playlistmarket.root.observe

interface Observer {
    fun <S, T> notifyObserver(event: S?, data: T?)
}
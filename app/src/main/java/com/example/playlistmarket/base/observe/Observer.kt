package com.example.playlistmarket.base.observe

interface Observer {
    fun <S, T> notifyObserver(event: S?, data: T?)
}
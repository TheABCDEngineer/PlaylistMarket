package com.example.playlistmarket

interface Observer {
    fun <S, T> notifyObserver(event: S?, data: T)
}

interface Observable {
    fun addObserver(observer: Observer)
}
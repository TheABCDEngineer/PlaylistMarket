package com.example.playlistmarket.creator.interactors

interface Observer {
    fun <S, T> notifyObserver(event: S?, data: T?)
}
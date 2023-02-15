package com.example.playlistmarket.creator.interactors

interface Observable {
    fun addObserver(observer: Observer)
}
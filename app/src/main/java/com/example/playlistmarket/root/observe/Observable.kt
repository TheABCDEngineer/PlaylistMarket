package com.example.playlistmarket.root.observe

interface Observable {
    fun addObserver(observer: Observer)
}
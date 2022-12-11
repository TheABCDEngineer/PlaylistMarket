package com.example.playlistmarket


interface HistoryObservable {
    fun addObserver(observer: HistoryObserver)
}

interface QueryStatusObservable {
    fun addObserver(observer: QueryStatusObserver)
}
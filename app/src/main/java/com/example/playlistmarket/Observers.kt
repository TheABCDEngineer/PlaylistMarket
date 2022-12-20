package com.example.playlistmarket

import com.example.playlistmarket.search.query.ResponseHandle

interface HistoryObserver {
    fun addTrackToRecentList(track: Track)
}

interface QueryStatusObserver {
    fun showQueryResults(trackList: ArrayList<Track>, error: ResponseHandle?)
}

interface NotifyAdapterObserver {
    fun notifyAdapterDataSetChange()
}
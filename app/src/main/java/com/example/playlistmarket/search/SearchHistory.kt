package com.example.playlistmarket.search

import android.content.SharedPreferences
import com.example.playlistmarket.*
import com.google.gson.Gson

class SearchHistory(

) : HistoryObserver, NotifyAdapterObservable {
    private val file = getSharePreferences()
    private val key = App.appContext.getString(R.string.recent_tracks_list_key)
    var recentTracksList = ArrayList<Track>()
        private set

    private lateinit var adapterOwner: NotifyAdapterObserver

    init {
        loadFromFile()
    }

    override fun addTrackToRecentList(track: Track) {
        for (i in recentTracksList) {
            if (i.trackId == track.trackId) {
                recentTracksList.remove(i)
                break
            }
        }
        recentTracksList.reverse()
        recentTracksList.add(track)
        recentTracksList.reverse()

        if (recentTracksList.size > 10) {
            recentTracksList.removeAt(10)
        }

        adapterOwner.notifyAdapterDataSetChange()
    }

    fun clearHistory() {
        recentTracksList.clear()
        saveToFile()
    }

    private fun loadFromFile() {
        val json: String = file.getString(key, null) ?: return
        recentTracksList.addAll(Gson().fromJson(json, Array<Track>::class.java))
    }

    fun saveToFile() {
        val json = Gson().toJson(recentTracksList)
        file.edit()
            .putString(key, json)
            .apply()
    }

    override fun addObserver(observer: NotifyAdapterObserver) {
        adapterOwner = observer
    }

}
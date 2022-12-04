package com.example.playlistmarket

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(
    private val file: SharedPreferences,
    private val key: String
) : Observer {

    var recentTracksList = ArrayList<Track>()
        private set

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
    }

    fun clearHistory() {
        recentTracksList.clear()
        saveToFile()
    }

    fun loadFromFile() {
        val json: String = file.getString(key, null) ?: return
        recentTracksList.addAll(Gson().fromJson(json, Array<Track>::class.java))
    }

    fun saveToFile() {
        val json = Gson().toJson(recentTracksList)
        file.edit()
            .putString(key, json)
            .apply()
    }
}
package com.example.playlistmarket

import android.content.SharedPreferences
import com.google.gson.Gson

class TrackListHandler(
    private val file: SharedPreferences,
    private val key: String,
    listSizeLimit: Int?
) : Observer {

    private var limit: Int? = null

    var items = ArrayList<Track>()
        private set

    private lateinit var adapterOwner: Observer

    init {
        if (listSizeLimit != null) {
            limit = if (listSizeLimit < 1) 1 else listSizeLimit
        }
        loadFromFile()
    }

    fun addTrack(track: Track) {
        if (checkTrackAtList(track)) deleteTrack(track)
        items.add(0, track)

        if ((limit != null) && (items.size > limit!!)) {
            items.removeAt(limit!!)
        }
        saveToFile()
    }

    fun clear() {
        items.clear()
        saveToFile()
    }

    fun deleteTrack(track: Track) {
        if (!checkTrackAtList(track)) return
        items.remove(track)
        saveToFile()
    }

    fun checkTrackAtList(track: Track): Boolean {
        for (i in items) {
            if (i.getTrackId() == track.getTrackId()) {
                return true
            }
        }
        return false
    }

    private fun loadFromFile() {
        val json: String = file.getString(key, null) ?: return
        items.addAll(Gson().fromJson(json, Array<Track>::class.java))
    }

    private fun saveToFile() {
        val json = Gson().toJson(items)
        file.edit()
            .putString(key, json)
            .apply()
    }

    override fun <S, T> notifyObserver(event: S?, data: T) {
        if ((event == null) || (data == null)) return
        if (event as String == App.TRACK_ADD) addTrack(data as Track)
        if (event as String == App.TRACK_DELETE) deleteTrack(data as Track)
    }
}
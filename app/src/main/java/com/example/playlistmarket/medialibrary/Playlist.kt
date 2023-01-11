package com.example.playlistmarket.medialibrary

import android.content.SharedPreferences
import com.example.playlistmarket.App
import com.example.playlistmarket.Observer
import com.example.playlistmarket.deleteFromFileOnKey
import com.example.playlistmarket.loadTrackListFromFileOnKey
import com.example.playlistmarket.saveListToFileOnKey

class Playlist(
    private val file: SharedPreferences,
    var title: String,
    private var limit: Int? = null
) : Observer {
    var items = ArrayList<Track>()
        private set

    init {
        if (limit != null) {
            limit = if (limit!! < 1) 1 else limit
        }
        if (file.getString(title,null) == null) {
            saveListToFileOnKey(file,title,items)
        } else {
            items.addAll(loadTrackListFromFileOnKey(file,title))
        }
    }

    fun addTrack(track: Track) {
        if (checkTrackAtList(track)) deleteTrack(track)
        items.add(0, track)

        if ((limit != null) && (items.size > limit!!)) {
            items.removeAt(limit!!)
        }
        saveListToFileOnKey(file,title,items)
    }

    fun clear() {
        items.clear()
        saveListToFileOnKey(file,title,items)
    }

    fun deleteTrack(track: Track) {
        if (!checkTrackAtList(track)) return
        items.remove(track)
        saveListToFileOnKey(file,title,items)
    }

    fun checkTrackAtList(track: Track): Boolean {
        for (i in items) {
            if (i.getTrackId() == track.getTrackId()) {
                return true
            }
        }
        return false
    }

    fun renameTitle(newTitle: String) {
        if (newTitle == "") return
        deletePlaylist()
        title = newTitle
        saveListToFileOnKey(file,title,items)
    }

    fun deletePlaylist() {
        deleteFromFileOnKey(file,title)
    }

    override fun <S, T> notifyObserver(event: S?, data: T) {
        if ((event == null) || (data == null)) return
        if (event as String == App.TRACK_ADD) addTrack(data as Track)
        if (event as String == App.TRACK_DELETE) deleteTrack(data as Track)
    }
}
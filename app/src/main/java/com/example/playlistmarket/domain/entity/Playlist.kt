package com.example.playlistmarket.domain.entity

import com.example.playlistmarket.creator.enums.PlaylistHandle
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.data.repository.PlaylistRepository
import com.example.playlistmarket.domain.model.Track

class Playlist(
    private val playlistStorage: PlaylistRepository,
    var title: String,
    private var limit: Int? = null
) : Observer {

    var items = ArrayList<Track>()
        private set

    init {
        if (limit != null) {
            limit = if (limit!! < 1) 1 else limit
        }
        if (playlistStorage.loadPlaylist(title).isEmpty()) {
            playlistStorage.savePlaylist(title, items)
        } else {
            items.addAll(playlistStorage.loadPlaylist(title))
        }
    }

    fun addTrack(track: Track) {
        if (checkTrackAtList(track)) deleteTrack(track)
        items.add(0, track)

        if ((limit != null) && (items.size > limit!!)) {
            items.removeAt(limit!!)
        }
        playlistStorage.savePlaylist(title, items)
    }

    fun clear() {
        items.clear()
        playlistStorage.savePlaylist(title, items)
    }

    fun deleteTrack(track: Track) {
        if (!checkTrackAtList(track)) return
        items.remove(track)
        playlistStorage.savePlaylist(title, items)
    }

    fun checkTrackAtList(track: Track): Boolean {
        for (i in items) {
            if (i.trackId == track.trackId) {
                return true
            }
        }
        return false
    }

    fun renameTitle(newTitle: String) {
        if (newTitle == "") return
        deletePlaylist()
        title = newTitle
        playlistStorage.savePlaylist(title, items)
    }

    fun deletePlaylist() {
        playlistStorage.deletePlaylist(title)
    }

    override fun <S, T> notifyObserver(event: S?, data: T?) {
        if ((event == null) || (data == null)) return
        if (event as PlaylistHandle == PlaylistHandle.ADD_TRACK) addTrack(data as Track)
        if (event as PlaylistHandle == PlaylistHandle.DELETE_TRACK) deleteTrack(data as Track)
    }
}
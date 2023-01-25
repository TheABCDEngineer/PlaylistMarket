package com.example.playlistmarket.player

import android.content.SharedPreferences
import com.example.playlistmarket.App
import com.example.playlistmarket.loadListFromFileOnKey
import com.example.playlistmarket.medialibrary.Playlist
import com.example.playlistmarket.medialibrary.Track

class TrackHandle(
    private val file: SharedPreferences,
    private val favoritesFile: SharedPreferences,
    private val track: Track,
    private val activity: PlayerActivity
) {

    init {
        activity.trackHandleWidget.apply {
            isTrackAtFavorites = checkTrackInFavorites()
            isTrackAtPlaylist = checkTrackInAllPlaylists()
        }
    }

    private fun checkTrackInFavorites(): Boolean {
        val favorites = Playlist(favoritesFile, App.FAVORITES_LIST_KEY)
        if (favorites.items.size == 0) return false
        return favorites.checkTrackAtList(track)
    }

    fun checkTrackInAllPlaylists(): Boolean {
        val playlistTitles = ArrayList<String>()
        playlistTitles.addAll(
            loadListFromFileOnKey(
                file,
                App.PLAYLIST_TITLES,
                Array<String>::class.java
            )
        )

        if (playlistTitles.size == 0) return false

        for (title in playlistTitles) {
            val playlist = Playlist(file, title)
            if (playlist.checkTrackAtList(track)) return true
        }
        return false
    }

    private fun addTrackToFavorites() {
        manageTrackOfList(null, App.TRACK_ADD)
    }

    fun addTrackToPlayList(playlistTitle: String) {
        manageTrackOfList(playlistTitle, App.TRACK_ADD)
    }

    private fun deleteTrackFromFavorites() {
        manageTrackOfList(null, App.TRACK_DELETE)
    }

    fun deleteTrackFromPlaylist(playlistTitle: String) {
        manageTrackOfList(playlistTitle, App.TRACK_DELETE)
    }

    private fun manageTrackOfList(list: String?, event: String) {
        val playlist = if (list == null) {
            Playlist(favoritesFile, App.FAVORITES_LIST_KEY)
        } else {
            Playlist(file, list)
        }

        playlist.notifyObserver(event, track)
    }

    fun onPausedParentActivity() {
        if (activity.trackHandleWidget.isTrackAtFavorites) {
            if (!checkTrackInFavorites()) {
                addTrackToFavorites()
            }
        }

        if (!activity.trackHandleWidget.isTrackAtFavorites) {
            if (checkTrackInFavorites()) {
                deleteTrackFromFavorites()
            }
        }
    }
}
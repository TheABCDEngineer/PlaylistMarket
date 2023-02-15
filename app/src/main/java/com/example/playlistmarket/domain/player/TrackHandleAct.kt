package com.example.playlistmarket.domain.player

import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.controller.player.interactors.TrackHandleInteractor
import com.example.playlistmarket.creator.App
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.enums.PlaylistHandle
import com.example.playlistmarket.data.interactors.storage.PlaylistStorageInteractor


class TrackHandleAct(
    private val playlistFile: PlaylistStorageInteractor
) : TrackHandleInteractor {

    override fun getTrackInFavoritesStatus(track: Track): Boolean {
        val favorites = Creator.createPlaylist(App.FAVORITES_LIST_KEY)
        if (favorites.items.size == 0) return false
        return favorites.checkTrackAtList(track)
    }

    override fun getTrackInPlaylistsStatus(track: Track): Boolean {
        if (getPlaylistTitleIfTrackInPlaylist(track) != null) return true
        return false
    }

    override fun getPlaylistTitleIfTrackInPlaylist(track: Track): String? {
        val playlistTitles = ArrayList<String>()
        playlistTitles.addAll(playlistFile.loadTitlesList())

        if (playlistTitles.size == 0) return null

        for (title in playlistTitles) {
            val playlist = Creator.createPlaylist(title)
            if (playlist.checkTrackAtList(track)) return title
        }
        return null
    }

    override fun addTrackToPlaylist(track: Track, playlistTitle: String) {
        manageTrackOfList(track, playlistTitle, PlaylistHandle.ADD_TRACK)
    }

    override fun deleteTrackFromPlaylist(track: Track, playlistTitle: String) {
        manageTrackOfList(track, playlistTitle, PlaylistHandle.DELETE_TRACK)
    }

    private fun manageTrackOfList(track: Track, playlistTitle: String, event: PlaylistHandle) {
        val playlist = Creator.createPlaylist(playlistTitle)
        playlist.notifyObserver(event, track)
    }
}
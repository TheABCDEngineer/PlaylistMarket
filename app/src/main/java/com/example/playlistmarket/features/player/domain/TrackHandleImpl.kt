package com.example.playlistmarket.features.player.domain

import com.example.playlistmarket.base.domain.model.Track
import com.example.playlistmarket.features.player.domain.interactors.TrackHandleInteractor
import com.example.playlistmarket.App
import com.example.playlistmarket.base.domain.model.enums.PlaylistHandle
import com.example.playlistmarket.base.domain.PlaylistCreator


class TrackHandleImpl(
    private val playlistCreator: PlaylistCreator
) : TrackHandleInteractor {

    override fun getTrackInFavoritesStatus(track: Track): Boolean {
        val favorites = playlistCreator.createPlaylist(App.FAVORITES_LIST_KEY)
        if (favorites.items.size == 0) return false
        return favorites.checkTrackAtList(track)
    }

    override fun getTrackInPlaylistsStatus(track: Track): Boolean {
        if (getPlaylistTitleIfTrackInPlaylist(track) != null) return true
        return false
    }

    override fun getPlaylistTitleIfTrackInPlaylist(track: Track): String? {
        val playlistTitles = ArrayList<String>()
        playlistTitles.addAll(playlistCreator.playlistStorage.loadTitlesList())

        if (playlistTitles.size == 0) return null

        for (title in playlistTitles) {
            val playlist = playlistCreator.createPlaylist(title)
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
        val playlist = playlistCreator.createPlaylist(playlistTitle)
        playlist.notifyObserver(event, track)
    }
}
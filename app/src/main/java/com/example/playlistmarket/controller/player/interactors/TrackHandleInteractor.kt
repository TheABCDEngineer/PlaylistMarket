package com.example.playlistmarket.controller.player.interactors

import com.example.playlistmarket.domain.models.Track

interface TrackHandleInteractor {
    fun getTrackInFavoritesStatus(track: Track): Boolean
    fun getTrackInPlaylistsStatus(track: Track): Boolean
    fun getPlaylistTitleIfTrackInPlaylist(track: Track): String?

    fun addTrackToPlaylist(track: Track, playlistTitle: String)
    fun deleteTrackFromPlaylist(track: Track, playlistTitle: String)
}
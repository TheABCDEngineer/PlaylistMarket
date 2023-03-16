package com.example.playlistmarket.features.player.domain.interactors

import com.example.playlistmarket.features.main.domain.model.Track

interface TrackHandleInteractor {
    fun getTrackInFavoritesStatus(track: Track): Boolean
    fun getTrackInPlaylistsStatus(track: Track): Boolean
    fun getPlaylistTitleIfTrackInPlaylist(track: Track): String?

    fun addTrackToPlaylist(track: Track, playlistTitle: String)
    fun deleteTrackFromPlaylist(track: Track, playlistTitle: String)
}
package com.example.playlistmarket.features.player.viewModel.interactors

import com.example.playlistmarket.domain.model.Track

interface TrackHandleInteractor {
    fun getTrackInFavoritesStatus(track: Track): Boolean
    fun getTrackInPlaylistsStatus(track: Track): Boolean
    fun getPlaylistTitleIfTrackInPlaylist(track: Track): String?

    fun addTrackToPlaylist(track: Track, playlistTitle: String)
    fun deleteTrackFromPlaylist(track: Track, playlistTitle: String)
}
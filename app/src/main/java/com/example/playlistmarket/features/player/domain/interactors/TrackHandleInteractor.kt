package com.example.playlistmarket.features.player.domain.interactors

import com.example.playlistmarket.root.domain.model.Playlist
import com.example.playlistmarket.root.domain.model.Track

interface TrackHandleInteractor {
   suspend fun setFavoritesPlaylist(playlist: Playlist)
   suspend fun getTrackInFavoritesStatus(track: Track): Boolean
   suspend fun saveTrackInFavorites(track: Track)
   suspend fun deleteTrackFromFavorites(track: Track)
}
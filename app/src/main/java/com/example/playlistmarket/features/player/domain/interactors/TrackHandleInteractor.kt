package com.example.playlistmarket.features.player.domain.interactors

import com.example.playlistmarket.root.domain.model.Track

interface TrackHandleInteractor {
   fun getFavoritesPlaylistId(): Int
   suspend fun getTrackInFavoritesStatus(track: Track): Boolean
   suspend fun getTrackInPlaylistsStatus(trackId: Int): Boolean
   suspend fun getTrackPlaylistOwnersIdList(trackId: Int): ArrayList<Int>
}
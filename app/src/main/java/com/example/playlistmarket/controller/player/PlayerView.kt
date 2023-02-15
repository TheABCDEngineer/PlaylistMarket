package com.example.playlistmarket.controller.player

import com.example.playlistmarket.domain.models.TrackPropertiesModel

interface PlayerView {
    fun updateTrackPlayingStatus(isPlaying: Boolean)
    fun updatePlaybackTimer(time: String)
    fun updateProgressOnPrepare(isPrepared: Boolean)

    fun updateTrackInFavoritesStatus(isInFavorites: Boolean)
    fun updateTrackInPlaylistsStatus(isInPlaylists: Boolean)

    fun showTrackProperties(model: TrackPropertiesModel)
}
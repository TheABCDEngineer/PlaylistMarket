package com.example.playlistmarket.features.player.presenter

interface PlayerView {
    fun updateTrackPlayingStatus(isPlaying: Boolean)
    fun updatePlaybackTimer(time: String)
    fun updateProgressOnPrepare(isPrepared: Boolean)

    fun updateTrackInFavoritesStatus(isInFavorites: Boolean)
    fun updateTrackInPlaylistsStatus(isInPlaylists: Boolean)
}
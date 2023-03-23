package com.example.playlistmarket.features.main.domain

import com.example.playlistmarket.features.main.domain.entity.Playlist
import com.example.playlistmarket.features.main.domain.repository.PlaylistRepository

class PlaylistCreator(
    val playlistStorage: PlaylistRepository
) {
    fun createPlaylist(title: String, limit: Int? = null): Playlist {
        return Playlist(
            playlistStorage,
            title,
            limit
        )
    }
}
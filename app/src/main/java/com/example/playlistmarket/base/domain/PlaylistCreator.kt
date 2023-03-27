package com.example.playlistmarket.base.domain

import com.example.playlistmarket.base.domain.entity.Playlist
import com.example.playlistmarket.base.domain.repository.PlaylistRepository

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
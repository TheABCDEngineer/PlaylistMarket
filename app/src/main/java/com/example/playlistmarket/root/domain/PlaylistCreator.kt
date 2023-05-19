package com.example.playlistmarket.root.domain

import com.example.playlistmarket.root.domain.entity.Playlist
import com.example.playlistmarket.root.domain.repository.PlaylistRepository

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
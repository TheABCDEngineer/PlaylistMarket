package com.example.playlistmarket.root.domain.repository

import com.example.playlistmarket.root.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun savePlaylist(playlist: Playlist)
    suspend fun loadPlaylists(): Flow<ArrayList<Playlist>>
    suspend fun loadFavoritesPlaylist(): Playlist
    suspend fun deletePlaylist(playlist: Playlist)
}
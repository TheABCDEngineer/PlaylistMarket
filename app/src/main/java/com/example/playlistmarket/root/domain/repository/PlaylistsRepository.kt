package com.example.playlistmarket.root.domain.repository

import com.example.playlistmarket.root.domain.model.Playlist

interface PlaylistsRepository {
    suspend fun savePlaylist(playlist: Playlist): Int
    suspend fun loadPlaylists(): ArrayList<Playlist>
    suspend fun loadPlaylist(playlistId: Int): Playlist?
    suspend fun loadFavoritesPlaylist(): Playlist
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getPlaylistsOfTrack(trackId: Int): ArrayList<Playlist>
}
package com.example.playlistmarket.root.domain.repository

import com.example.playlistmarket.root.domain.model.Track

interface FavoritesRepository {
    suspend fun saveTrack(track: Track)
    suspend fun loadTracks(): ArrayList<Track>
    suspend fun deleteTrack(track: Track): Boolean
}
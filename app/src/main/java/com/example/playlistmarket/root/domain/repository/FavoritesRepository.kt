package com.example.playlistmarket.root.domain.repository

import com.example.playlistmarket.root.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun saveTrack(track: Track)
    suspend fun loadTracks(): Flow<ArrayList<Track>>
    suspend fun deleteTrack(track: Track): Boolean
}
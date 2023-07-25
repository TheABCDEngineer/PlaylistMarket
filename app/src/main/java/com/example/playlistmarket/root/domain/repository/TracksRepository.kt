package com.example.playlistmarket.root.domain.repository

import com.example.playlistmarket.root.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    suspend fun saveTrackToPlaylist(track: Track, playlistId: Int)
    suspend fun loadTracksFromPlaylist(playlistId: Int): Flow<ArrayList<Track>>
    suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Int)
}
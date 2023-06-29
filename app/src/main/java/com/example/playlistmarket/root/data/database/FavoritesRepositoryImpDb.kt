package com.example.playlistmarket.root.data.database

import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpDb(
    private val database: TracksDatabase
) : FavoritesRepository {
    override suspend fun saveTrack(track: Track) {
        val trackEntity = DbConverter.map(track)
        database.trackDao().insertTrack(trackEntity)
    }

    override suspend fun loadTracks(): Flow<ArrayList<Track>> = flow {
        val trackEntityList = database.trackDao().getTracks()
        val tracks = ArrayList<Track>()

        if (trackEntityList.isNotEmpty()) {
            for (item in trackEntityList) {
                tracks.add(
                    DbConverter.map(item)
                )
            }
        }
        emit(tracks)
    }

    override suspend fun deleteTrack(track: Track): Boolean {
        return (database.trackDao().deleteTrack(track.trackId)) > 0
    }
}
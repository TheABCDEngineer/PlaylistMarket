package com.example.playlistmarket.root.data

import com.example.playlistmarket.root.data.database.DbConverter
import com.example.playlistmarket.root.data.database.TracksDatabase
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.domain.repository.FavoritesRepository

class FavoritesRepositoryImpDb(
    private val database: TracksDatabase
): FavoritesRepository {
    override suspend fun saveTrack(track: Track) {
        val trackEntity = DbConverter.map(track)
        database.trackDao().insertTrack(trackEntity)
    }

    override suspend fun loadTracks(): ArrayList<Track> {
        val trackEntityList = database.trackDao().getTracks()
        val tracks = ArrayList<Track>()
        if (trackEntityList.isEmpty()) return tracks

        for (item in trackEntityList) {
            tracks.add(
                DbConverter.map(item)
            )
        }
        return tracks
    }

    override suspend fun deleteTrack(track: Track): Boolean {
        return (database.trackDao().deleteTrack(track.trackId)) > 0
    }
}
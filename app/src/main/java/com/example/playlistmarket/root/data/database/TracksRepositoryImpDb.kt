package com.example.playlistmarket.root.data.database

import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpDb(
    private val database: TracksDatabase
) : TracksRepository {
    override suspend fun saveTrackToPlaylist(track: Track, playlistId: Int) {
        val trackEntity = DbConverter.mapTrack(track)
        database.trackDao().insertTrack(trackEntity)

        val trackLibrary = DbConverter.createRelationship(track.trackId,playlistId)
        database.trackLibraryDao().insertRelationship(trackLibrary)
    }

    override suspend fun loadTracksFromPlaylist(playlistId: Int): Flow<ArrayList<Track>> = flow {
        val trackEntityList = database.trackDao().getTracksFromPlaylist(playlistId)
        val tracks = ArrayList<Track>()

        if (trackEntityList.isNotEmpty()) {
            for (item in trackEntityList) {
                tracks.add(
                    DbConverter.mapTrack(item)
                )
            }
        }
        emit(tracks)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlistId: Int) {
        val trackLibrary = DbConverter.createRelationship(track.trackId,playlistId)
        database.trackLibraryDao().deleteRelationship(trackLibrary)

        val playlistEntityList = database.playlistDao().getPlaylistsOfTrack(track.trackId)
        if (playlistEntityList.isNotEmpty()) return

        database.trackDao().deleteTrack(track.trackId)
    }
}
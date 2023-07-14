package com.example.playlistmarket.root.data.database

import com.example.playlistmarket.root.domain.model.Playlist
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImplDb(
    val database: TracksDatabase
): PlaylistsRepository {
    override suspend fun savePlaylist(playlist: Playlist) {
        database.playlistDao().insertPlaylist(
            DbConverter.mapPlaylist(playlist)
        )
    }

    override suspend fun loadPlaylists(): Flow<ArrayList<Playlist>> = flow {
        val playlistEntityList = database.playlistDao().getPlaylists()
        val playlists = ArrayList<Playlist>()

        if (playlistEntityList.isNotEmpty()) {
            for (item in playlistEntityList) {
                playlists.add(
                    DbConverter.mapPlaylist(item)
                )
            }
        }
        emit(playlists)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val tracks = database.trackDao().getTracksFromPlaylist(playlist.id)
        database.playlistDao().deletePlaylist(playlist.id)
        database.trackLibraryDao().deleteRelationshipOfPlaylist(playlist.id)
        if (tracks.isEmpty()) return

        for (track in tracks) {
            val playlists = database.playlistDao().getPlaylistsOfTrack(track.id)
            if (playlists.isEmpty()) {
                database.trackDao().deleteTrack(track.id)
            }
        }
    }
}
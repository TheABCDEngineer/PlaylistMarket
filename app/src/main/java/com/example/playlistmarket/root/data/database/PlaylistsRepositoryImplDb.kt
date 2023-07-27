package com.example.playlistmarket.root.data.database

import com.example.playlistmarket.App
import com.example.playlistmarket.root.data.database.entity.PlaylistEntity
import com.example.playlistmarket.root.domain.model.Playlist
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository

class PlaylistsRepositoryImplDb(
    val database: TracksDatabase
): PlaylistsRepository {
    override suspend fun savePlaylist(playlist: Playlist): Int {
        database.playlistDao().insertPlaylist(
            DbConverter.mapPlaylist(playlist)
        )
        val playlistEntityList = database.playlistDao().getPlaylists()
        return playlistEntityList[0].id
    }

    override suspend fun loadPlaylists(): ArrayList<Playlist> {
        val playlistEntityList = database.playlistDao().getPlaylists()
        val playlists = ArrayList<Playlist>()

        if (playlistEntityList.isNotEmpty()) {
            for (item in playlistEntityList) {
                playlists.add(
                    DbConverter.mapPlaylist(item)
                )
            }
        }
        if (playlists.isNotEmpty()) playlists.removeAt(playlists.size-1)
        return playlists
    }

    override suspend fun loadPlaylist(playlistId: Int): Playlist? {
        val playlistEntityList = database.playlistDao().getPlaylist(playlistId)
        if (playlistEntityList.isEmpty()) return null
        return DbConverter.mapPlaylist(
            playlistEntityList[0]
        )
    }

    override suspend fun loadFavoritesPlaylist(): Playlist {
        val playlistEntityList = database.playlistDao().getPlaylists()

        if (playlistEntityList.isNotEmpty()) {
            for (item in playlistEntityList) {
                if (item.title == App.FAVORITES_UNIQUE_KEY) return DbConverter.mapPlaylist(item)
            }
        }

        val favoritesEntity = PlaylistEntity(
            0, App.FAVORITES_UNIQUE_KEY, "", 0, 0
        )
        database.playlistDao().insertPlaylist(favoritesEntity)
        return loadFavoritesPlaylist()
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        val tracks = database.trackDao().getTracksFromPlaylist(playlistId)
        database.playlistDao().deletePlaylist(playlistId)
        database.trackLibraryDao().deleteRelationshipOfPlaylist(playlistId)
        if (tracks.isEmpty()) return

        for (track in tracks) {
            val playlists = database.playlistDao().getPlaylistsOfTrack(track.id)
            if (playlists.isEmpty()) {
                database.trackDao().deleteTrack(track.id)
            }
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        database.playlistDao().updatePlaylist(
            playlist.id,
            playlist.title,
            playlist.description,
            playlist.trackQuantity
        )
    }

    override suspend fun getPlaylistsOfTrack(trackId: Int): ArrayList<Playlist> {
        val playlistEntityList = database.playlistDao().getPlaylistsOfTrack(trackId)
        val playlists = ArrayList<Playlist>()

        if (playlistEntityList.isNotEmpty()) {
            val favoritesPlaylist = loadFavoritesPlaylist()
            for (playlist in playlistEntityList) {
                if (playlist.id != favoritesPlaylist.id)
                    playlists.add(
                        DbConverter.mapPlaylist(playlist)
                    )
            }
        }
        return playlists
    }
}
package com.example.playlistmarket.root.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmarket.root.data.database.entity.PlaylistEntity

@Dao
interface PlaylistDao {
    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("" +
            "SELECT * " +
            "FROM playlists_table " +
            "ORDER BY creationTimeTag DESC"
    )
    suspend fun getPlaylists(): Array<PlaylistEntity>

    @Query("" +
            "SELECT * " +
            "FROM playlists_table " +
            "INNER JOIN library_table ON " +
            "playlists_table.id = library_table.playlist_id WHERE " +
            "library_table.track_id = :trackId"
    )
    suspend fun getPlaylistsOfTrack(trackId: Int): Array<PlaylistEntity>

    @Query("" +
            "DELETE " +
            "FROM playlists_table " +
            "WHERE id = :playlistId"
    )
    suspend fun deletePlaylist(playlistId: Int): Int

    @Query("" +
            "UPDATE playlists_table " +
            "SET title = :title, description = :description, trackQuantity = :trackQuantity " +
            "WHERE id = :playlistId"
    )
    suspend fun updatePlaylist(playlistId: Int, title: String, description: String, trackQuantity: Int)

    @Query("" +
            "SELECT * " +
            "FROM playlists_table " +
            "WHERE id = :playlistId"
    )
    suspend fun getPlaylist(playlistId: Int): Array<PlaylistEntity>
}
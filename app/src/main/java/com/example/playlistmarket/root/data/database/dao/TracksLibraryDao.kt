package com.example.playlistmarket.root.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmarket.root.data.database.entity.TracksLibraryEntity

@Dao
interface TracksLibraryDao {
    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertRelationship(tracksLibraryEntity: TracksLibraryEntity)

    @Delete
    suspend fun deleteRelationship(tracksLibraryEntity: TracksLibraryEntity): Int

    @Query("" +
            "DELETE " +
            "FROM library_table " +
            "WHERE playlist_id = :playlistId"
    )
    suspend fun deleteRelationshipOfPlaylist(playlistId: Int): Int
}
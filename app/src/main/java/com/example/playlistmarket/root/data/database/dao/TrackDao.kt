package com.example.playlistmarket.root.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmarket.root.data.database.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertTrack(track: TrackEntity)

    @Query("" +
            "SELECT * " +
            "FROM tracks_table " +
            "ORDER BY creationTimeTag DESC"
    )
    suspend fun getTracks(): Array<TrackEntity>

    @Query("" +
            "DELETE " +
            "FROM tracks_table " +
            "WHERE Id = :trackId"
    )
    suspend fun deleteTrack(trackId: Int): Int
}
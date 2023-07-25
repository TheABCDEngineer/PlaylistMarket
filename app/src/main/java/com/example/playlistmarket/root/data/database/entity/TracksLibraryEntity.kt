package com.example.playlistmarket.root.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "library_table",
    primaryKeys = ["track_id", "playlist_id"],
    foreignKeys = [
        ForeignKey(entity = TrackEntity::class,
            parentColumns = ["id"],
            childColumns = ["track_id"]
        ),
        ForeignKey(entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlist_id"]
        )
    ]
)
data class TracksLibraryEntity(
    @ColumnInfo(name = "track_id") val trackId: Int,
    @ColumnInfo(name = "playlist_id") val playlistId: Int
)
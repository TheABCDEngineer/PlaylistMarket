package com.example.playlistmarket.root.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlists_table"
)

data class PlaylistEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val artworkPath: String,
    val description: String,
    val creationTimeTag: Int
)
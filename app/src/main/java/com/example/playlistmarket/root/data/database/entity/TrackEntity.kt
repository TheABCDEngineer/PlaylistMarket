package com.example.playlistmarket.root.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tracks_table"
)
data class TrackEntity(
    @PrimaryKey val Id: Int,
    val trackName: String,
    val artist: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collection: String,
    val releaseYear: String,
    val genre: String,
    val country: String,
    val previewUrl: String,
    val creationTimeTag: Int
)
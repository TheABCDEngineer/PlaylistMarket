package com.example.playlistmarket.root.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmarket.root.data.database.dao.PlaylistDao
import com.example.playlistmarket.root.data.database.dao.TrackDao
import com.example.playlistmarket.root.data.database.dao.TracksLibraryDao
import com.example.playlistmarket.root.data.database.entity.PlaylistEntity
import com.example.playlistmarket.root.data.database.entity.TrackEntity
import com.example.playlistmarket.root.data.database.entity.TracksLibraryEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        TracksLibraryEntity::class
    ]
)
abstract class TracksDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackLibraryDao(): TracksLibraryDao
}
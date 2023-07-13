package com.example.playlistmarket.root.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmarket.root.data.database.dao.TrackDao
import com.example.playlistmarket.root.data.database.entity.TrackEntity

@Database(
    version = 1,
    entities = [
        TrackEntity::class
    ]
)
abstract class TracksDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}
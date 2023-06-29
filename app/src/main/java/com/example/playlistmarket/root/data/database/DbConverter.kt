package com.example.playlistmarket.root.data.database

import com.example.playlistmarket.root.currentTimeTag
import com.example.playlistmarket.root.data.database.entity.TrackEntity
import com.example.playlistmarket.root.domain.model.Track

class DbConverter {
    companion object {
        fun map(trackEntity: TrackEntity): Track = with(trackEntity) {
            Track(
                Id,
                trackName,
                artist,
                trackTimeMillis,
                artworkUrl100,
                collection,
                releaseYear,
                genre,
                country,
                previewUrl
            )
        }

        fun map(track: Track): TrackEntity = with(track) {
            TrackEntity(
                trackId,
                trackName,
                artist,
                trackTimeMillis,
                artworkUrl100,
                collection,
                releaseYear,
                genre,
                country,
                previewUrl,
                currentTimeTag()
            )
        }
    }
}
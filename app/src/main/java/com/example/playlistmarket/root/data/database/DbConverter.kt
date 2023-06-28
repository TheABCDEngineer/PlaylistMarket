package com.example.playlistmarket.root.data.database

import com.example.playlistmarket.root.data.database.entity.TrackEntity
import com.example.playlistmarket.root.domain.model.Track

class DbConverter {
    companion object {
        fun map(trackEntity: TrackEntity): Track {
            return Track(
                trackEntity.Id,
                trackEntity.trackName,
                trackEntity.artist,
                trackEntity.trackTimeMillis,
                trackEntity.artworkUrl100,
                trackEntity.collection,
                trackEntity.releaseYear,
                trackEntity.genre,
                trackEntity.country,
                trackEntity.previewUrl
            )
        }

        fun map(track: Track): TrackEntity {
            return TrackEntity(
                track.trackId,
                track.trackName,
                track.artist,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collection,
                track.releaseYear,
                track.genre,
                track.country,
                track.previewUrl
            )
        }
    }
}
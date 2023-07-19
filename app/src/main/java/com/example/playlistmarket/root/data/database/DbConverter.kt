package com.example.playlistmarket.root.data.database

import com.example.playlistmarket.root.currentTimeTag
import com.example.playlistmarket.root.data.database.entity.PlaylistEntity
import com.example.playlistmarket.root.data.database.entity.TrackEntity
import com.example.playlistmarket.root.data.database.entity.TracksLibraryEntity
import com.example.playlistmarket.root.domain.model.Playlist
import com.example.playlistmarket.root.domain.model.Track

class DbConverter {
    companion object {
        fun mapTrack(trackEntity: TrackEntity): Track = with(trackEntity) {
            Track(
                id,
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

        fun mapTrack(track: Track): TrackEntity = with(track) {
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

        fun mapPlaylist(playlistEntity: PlaylistEntity): Playlist = with(playlistEntity) {
            Playlist(
                title,
                //artworkFileName,
                description,
                trackQuantity,
                id
            )
        }

        fun mapPlaylist(playlist: Playlist): PlaylistEntity = with(playlist) {
            PlaylistEntity(
                id,
                title,
                //artworkFileName,
                description,
                trackQuantity,
                currentTimeTag()
            )
        }

        fun createRelationship(trackId: Int, playlistId: Int): TracksLibraryEntity =
            TracksLibraryEntity(trackId, playlistId)
    }
}
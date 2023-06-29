package com.example.playlistmarket.features.player.domain

import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.features.player.domain.interactors.TrackHandleInteractor
import com.example.playlistmarket.root.domain.repository.FavoritesRepository

class TrackHandleImpl(
    private val repository: FavoritesRepository
) : TrackHandleInteractor {

    override suspend fun getTrackInFavoritesStatus(track: Track): Boolean {
        val favorites = ArrayList<Track>()
        repository
            .loadTracks()
            .collect {
                favorites.addAll(it)
            }

        if (favorites.isEmpty()) return false

        val previousSize = favorites.size
        favorites.remove(track)
        return previousSize > favorites.size
    }

    override suspend fun saveTrackInFavorites(track: Track) {
        repository.saveTrack(track)
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        repository.deleteTrack(track)
    }

}
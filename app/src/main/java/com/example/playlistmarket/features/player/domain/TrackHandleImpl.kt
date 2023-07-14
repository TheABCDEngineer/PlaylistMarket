package com.example.playlistmarket.features.player.domain

import com.example.playlistmarket.App
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.features.player.domain.interactors.TrackHandleInteractor
import com.example.playlistmarket.root.domain.repository.TracksRepository

class TrackHandleImpl(
    private val repository: TracksRepository
) : TrackHandleInteractor {

    private val favoritesPlaylist = App.getFavoritesPlaylist()

    override suspend fun getTrackInFavoritesStatus(track: Track): Boolean {
        val favorites = ArrayList<Track>()
        repository
            .loadTracksFromPlaylist(favoritesPlaylist.id)
            .collect {
                favorites.addAll(it)
            }

        if (favorites.isEmpty()) return false

        val previousSize = favorites.size
        favorites.remove(track)
        return previousSize > favorites.size
    }

    override suspend fun saveTrackInFavorites(track: Track) {
        repository.saveTrackToPlaylist(track, favoritesPlaylist.id)
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        repository.deleteTrackFromPlaylist(track, favoritesPlaylist.id)
    }

}
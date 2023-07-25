package com.example.playlistmarket.features.player.domain

import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.features.player.domain.interactors.TrackHandleInteractor
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository
import com.example.playlistmarket.root.domain.repository.TracksRepository

class TrackHandleImpl(
    private val tracksRepository: TracksRepository,
    private val playlistsRepository: PlaylistsRepository
) : TrackHandleInteractor {

    private var favoritesPlaylistId: Int? = null
    override fun getFavoritesPlaylistId(): Int {
        return favoritesPlaylistId!!
    }

    override suspend fun getTrackInFavoritesStatus(track: Track): Boolean {
        if (favoritesPlaylistId == null) favoritesPlaylistId =
            playlistsRepository.loadFavoritesPlaylist().id
        val favorites = ArrayList<Track>()
        tracksRepository
            .loadTracksFromPlaylist(favoritesPlaylistId!!)
            .collect {
                favorites.addAll(it)
            }

        if (favorites.isEmpty()) return false

        val previousSize = favorites.size
        favorites.remove(track)
        return previousSize > favorites.size
    }

    override suspend fun getTrackInPlaylistsStatus(trackId: Int): Boolean {
        val playlistIdList = getTrackPlaylistOwnersIdList(trackId)
        if (playlistIdList.isEmpty()) return false
        return true
    }

    override suspend fun getTrackPlaylistOwnersIdList(trackId: Int): ArrayList<Int> {
        val playlists = playlistsRepository.getPlaylistsOfTrack(trackId)
        val playlistIdList = ArrayList<Int>()

        if (playlists.isNotEmpty()) {
            for (playlist in playlists) playlistIdList.add(playlist.id)
        }
        return playlistIdList
    }
}
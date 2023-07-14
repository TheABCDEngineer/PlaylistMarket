package com.example.playlistmarket.features.medialibrary.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.App
import com.example.playlistmarket.root.domain.model.Playlist
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository
import com.example.playlistmarket.root.domain.repository.TracksRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val tracksRepository: TracksRepository,
    private val playlistsRepository: PlaylistsRepository
): ViewModel() {

    private val favoritesPlaylist = App.getFavoritesPlaylist()

    private val favoritesFeedLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeFavoritesFeedState(): LiveData<ArrayList<Track>> = favoritesFeedLiveData

    fun onUiResume() {
        viewModelScope.launch {
            tracksRepository
                .loadTracksFromPlaylist(favoritesPlaylist.id)
                .collect {
                    favoritesFeedLiveData.postValue(it)
                    if (it.isEmpty()) initFavoritesPlaylist()
                }
                this.cancel()
        }
    }

    private suspend fun initFavoritesPlaylist() {
        val playlists = ArrayList<Playlist>()
        playlistsRepository
            .loadPlaylists()
            .collect {
                playlists.addAll(it)
            }

        for (playlist in playlists) {
            if (playlist.id == favoritesPlaylist.id) return
        }
        playlistsRepository.savePlaylist(favoritesPlaylist)
    }
}
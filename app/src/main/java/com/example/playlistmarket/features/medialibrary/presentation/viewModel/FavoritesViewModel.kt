package com.example.playlistmarket.features.medialibrary.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository
import com.example.playlistmarket.root.domain.repository.TracksRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val tracksRepository: TracksRepository,
    private val playlistsRepository: PlaylistsRepository
): ViewModel() {

    private val favoritesFeedLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeFavoritesFeedState(): LiveData<ArrayList<Track>> = favoritesFeedLiveData

    fun onUiResume() {
        viewModelScope.launch {
            val favoritesPlaylist = playlistsRepository.loadFavoritesPlaylist()
            tracksRepository
                .loadTracksFromPlaylist(favoritesPlaylist.id)
                .collect {
                    favoritesFeedLiveData.postValue(it)
                }
                this.cancel()
        }
    }
}
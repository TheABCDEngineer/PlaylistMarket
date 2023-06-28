package com.example.playlistmarket.features.medialibrary.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.domain.repository.FavoritesRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository
): ViewModel() {

    private val favoritesFeedLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeFavoritesFeedState(): LiveData<ArrayList<Track>> = favoritesFeedLiveData

    fun onUiResume() {
        viewModelScope.launch {
            val favorites = favoritesRepository.loadTracks()
            favoritesFeedLiveData.postValue(favorites)
        }
    }
}
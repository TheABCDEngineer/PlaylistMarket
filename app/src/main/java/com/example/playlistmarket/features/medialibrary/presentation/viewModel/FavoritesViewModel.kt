package com.example.playlistmarket.features.medialibrary.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmarket.root.domain.model.Track

class FavoritesViewModel(): ViewModel() {

    private val favoritesFeedLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeFavoritesFeedState(): LiveData<ArrayList<Track>> = favoritesFeedLiveData

    fun onUiResume() {
        //favoritesFeedLiveData.postValue(favorites.items)
    }
}
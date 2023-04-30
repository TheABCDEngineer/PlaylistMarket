package com.example.playlistmarket.features.medialibrary.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmarket.App
import com.example.playlistmarket.base.domain.PlaylistCreator
import com.example.playlistmarket.features.search.presentation.ui.recyclerView.SearchTrackAdapter

class FavoritesViewModel(
    private val playlistCreator: PlaylistCreator
): ViewModel() {

    private val trackFeedLiveData = MutableLiveData<SearchTrackAdapter>()
    fun observeTrackFeedState(): LiveData<SearchTrackAdapter> = trackFeedLiveData

    init {
        val favorites = playlistCreator.createPlaylist(App.FAVORITES_LIST_KEY)
        trackFeedLiveData.postValue(
            SearchTrackAdapter(favorites.items)
        )
    }
}
package com.example.playlistmarket.features.medialibrary.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.playlistmarket.App
import com.example.playlistmarket.root.domain.PlaylistCreator
import com.example.playlistmarket.root.domain.entity.Playlist
import com.example.playlistmarket.root.domain.model.Track

class FavoritesViewModel(
    private val playlistCreator: PlaylistCreator
): ViewModel() {

    fun getFavoritesList(): ArrayList<Track> {
        val favorites: Playlist = playlistCreator.createPlaylist(App.FAVORITES_LIST_KEY)
        return favorites.items
    }
}
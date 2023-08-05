package com.example.playlistmarket.features.medialibrary.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.features.medialibrary.data.PlaylistModelsConverter
import com.example.playlistmarket.features.medialibrary.domain.PlaylistRecyclerModel
import com.example.playlistmarket.features.playlistCreator.PlaylistCreator
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val converter: PlaylistModelsConverter
) : ViewModel() {

    private val feedLiveData = MutableLiveData<ArrayList<PlaylistRecyclerModel>>()
    fun observeFeedState(): LiveData<ArrayList<PlaylistRecyclerModel>> = feedLiveData

    fun onUiResume() {
        viewModelScope.launch {
            val playlists = playlistsRepository.loadPlaylists()
            val playlistModels = ArrayList<PlaylistRecyclerModel>()

            for (playlist in playlists) {
                playlistModels.add(
                    converter.mapToRecyclerModel(playlist)
                )
            }
            feedLiveData.postValue(playlistModels)
        }
    }

    fun onNewPlaylistButtonClicked() {
        PlaylistCreator.start()
    }
}
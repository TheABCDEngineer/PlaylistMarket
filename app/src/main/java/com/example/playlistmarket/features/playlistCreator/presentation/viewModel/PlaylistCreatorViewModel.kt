package com.example.playlistmarket.features.playlistCreator.presentation.viewModel

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.features.playlistCreator.presentation.EditScreenState
import com.example.playlistmarket.root.domain.model.Playlist
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.domain.repository.PlaylistArtworksRepository
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository
import com.example.playlistmarket.root.domain.repository.TracksRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlaylistCreatorViewModel(
    private val track: Track?,
    private val playlistArtworksRepository: PlaylistArtworksRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val tracksRepository: TracksRepository
): ViewModel() {
    private val playlistsTitles = ArrayList<String>()
    private lateinit var title: String
    private var description: String = ""
    private lateinit var pickImage: ActivityResultLauncher<PickVisualMediaRequest>

    private val artworkImageLiveData = MutableLiveData<Uri>()
    fun observeArtworkImage(): LiveData<Uri> = artworkImageLiveData

    private val titleFieldStateLiveData = MutableLiveData<EditScreenState>()
    fun observeTitleFieldState(): LiveData<EditScreenState> = titleFieldStateLiveData

    private val descriptionFieldStateLiveData = MutableLiveData<EditScreenState>()
    fun observeDescriptionFieldState(): LiveData<EditScreenState> = descriptionFieldStateLiveData

    fun onUiCreate(activity: AppCompatActivity) {
        pickImage = activity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
            if (uri != null) {
                artworkImageLiveData.postValue(uri)
            }
        }
        viewModelScope.launch {
            val playlists = playlistsRepository.loadPlaylists()
            if (playlists.isEmpty()) return@launch
            for (playlist in playlists) {
                playlistsTitles.add(playlist.title)
            }
        }
    }

    fun onTitleFieldTextChange(charSequence: CharSequence?) {
        title = charSequence?.toString() ?: ""
        val state = if (title == "") EditScreenState.INACTIVE_FIELD else EditScreenState.ACTIVE_FIELD
        titleFieldStateLiveData.postValue(state)
        viewModelScope.launch { postTitleUniqueWarning() }
    }

    fun onDescriptionFieldTextChange(charSequence: CharSequence?) {
        description = charSequence?.toString() ?: ""
        val state = if (description == "") EditScreenState.INACTIVE_FIELD else EditScreenState.ACTIVE_FIELD
        descriptionFieldStateLiveData.postValue(state)
    }

    fun setArtwork() {
        pickImage.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    fun createPlayList(): String {
        val trackQuantity = if (track != null) 1 else 0
        val newPlayList = Playlist(title.trim(),description,trackQuantity)

        runBlocking {
            val newPlaylistId = playlistsRepository.savePlaylist(newPlayList)
            if (track != null) tracksRepository.saveTrackToPlaylist(track, newPlaylistId)
            if (artworkImageLiveData.value != null)
                saveArtworkToAppStorage(artworkImageLiveData.value!!, newPlaylistId)
        }
        return title.trim()
    }

    private fun saveArtworkToAppStorage(uri: Uri, playlistId: Int) {
        viewModelScope.launch {
            playlistArtworksRepository.saveArtwork(uri, playlistId.toString())
        }
    }

    private fun postTitleUniqueWarning() {
        for (title in playlistsTitles) {
            if (this.title.trim() == title) titleFieldStateLiveData.postValue(EditScreenState.UNIQUE_TITLE)
        }
    }
}
package com.example.playlistmarket.features.medialibrary.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.features.medialibrary.data.PlaylistModelsConverter
import com.example.playlistmarket.features.medialibrary.domain.PlaylistScreenModel
import com.example.playlistmarket.features.playlistCreator.PlaylistCreator
import com.example.playlistmarket.root.domain.model.Playlist
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository
import com.example.playlistmarket.root.domain.repository.TracksRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlaylistPropertiesViewModel(
    private val playlistId: Int?,
    private val playlistsRepository: PlaylistsRepository,
    private val tracksRepository: TracksRepository,
    private val converter: PlaylistModelsConverter
): ViewModel() {
    private var playlist: Playlist? = null

    private val playlistNullException = MutableLiveData<Boolean>()
    fun observePlaylistNullException(): LiveData<Boolean> = playlistNullException

    private val playlistProperties = MutableLiveData<PlaylistScreenModel>()
    fun observePlaylistProperties(): LiveData<PlaylistScreenModel> = playlistProperties

    private val tracksData = MutableLiveData<ArrayList<Track>>()
    fun observeTracks(): LiveData<ArrayList<Track>> = tracksData

    fun onUiResume() {
        if (playlistId == null) {
            playlistNullException.postValue(true)
            return
        }
        viewModelScope.launch {
            playlist = playlistsRepository.loadPlaylist(playlistId)
            if (playlist != null) {
                val tracks = loadTracks(playlistId)
                updatePlaylistInfo(tracks)
            } else
                playlistNullException.postValue(true)
        }
    }

    fun deleteTrackFromPlaylist(track: Track) {
        viewModelScope.launch {
            tracksRepository.deleteTrackFromPlaylist(track, playlist!!.id)
            val tracks = tracksData.value!!
            tracks.remove(track)

            playlist!!.trackQuantity -= 1
            playlistsRepository.updatePlaylist(playlist!!)

            updatePlaylistInfo(tracks)
        }
    }

    fun sharePlaylist() {

    }

    fun modifyPlaylist() {
        PlaylistCreator.start(playlistId = playlist?.id)
    }

    fun deletePlaylist() {
        runBlocking {
            playlistsRepository.deletePlaylist(playlistId!!)
        }
    }

    private fun getTotalTracksDuration(tracks: ArrayList<Track>): Int {
        var totalDuration = 0
        for (track in tracks) {
            totalDuration += track.trackTimeMillis
        }
        return totalDuration
    }

    private suspend fun loadTracks(playlistId: Int): ArrayList<Track> {
        val tracks = ArrayList<Track>()
        tracksRepository.loadTracksFromPlaylist(playlistId).collect{ items ->
            tracks.addAll(items)
        }
        return tracks
    }

    private fun updatePlaylistInfo(tracks: ArrayList<Track>) {
        tracksData.postValue(tracks)
        playlistProperties.postValue(
            converter.mapToScreenModel(
                playlist!!,
                getTotalTracksDuration(tracks)
            )
        )
    }
}
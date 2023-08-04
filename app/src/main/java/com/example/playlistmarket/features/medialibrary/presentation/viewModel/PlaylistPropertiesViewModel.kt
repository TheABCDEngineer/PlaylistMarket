package com.example.playlistmarket.features.medialibrary.presentation.viewModel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.App
import com.example.playlistmarket.features.medialibrary.data.PlaylistModelsConverter
import com.example.playlistmarket.features.medialibrary.domain.PlaylistScreenModel
import com.example.playlistmarket.features.playlistCreator.PlaylistCreator
import com.example.playlistmarket.root.debounce
import com.example.playlistmarket.root.domain.model.Playlist
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository
import com.example.playlistmarket.root.domain.repository.TracksRepository
import com.example.playlistmarket.root.domain.util.getFormattedTrackTime
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlaylistPropertiesViewModel(
    private val playlistId: Int?,
    private val playlistsRepository: PlaylistsRepository,
    private val tracksRepository: TracksRepository,
    private val converter: PlaylistModelsConverter
) : ViewModel() {
    private var playlist: Playlist? = null

    private val isPlaylistLoaded = MutableLiveData<Boolean>()
    fun observePlaylistLoading(): LiveData<Boolean> = isPlaylistLoaded

    private val playlistProperties = MutableLiveData<PlaylistScreenModel>()
    fun observePlaylistProperties(): LiveData<PlaylistScreenModel> = playlistProperties

    private val tracksData = MutableLiveData<ArrayList<Track>>()
    fun observeTracks(): LiveData<ArrayList<Track>> = tracksData

    fun onUiResume() {
        if (playlistId == null) {
            isPlaylistLoaded.postValue(false)
            return
        }
        viewModelScope.launch {
            playlist = playlistsRepository.loadPlaylist(playlistId)
            if (playlist != null) {
                val tracks = loadTracks(playlistId)
                updatePlaylistInfo(tracks)
            } else
                isPlaylistLoaded.postValue(false)
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

    fun sharePlaylist(): Boolean {
        if (tracksData.value!!.isNullOrEmpty()) return false
        val message = createSharedPlaylistPropertiesMessage()
        val action = debounce<String>(App.CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope) { body ->
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, body)
            shareIntent.type = "text/plain"
            val intent = Intent.createChooser(shareIntent, null)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            App.appContext.startActivity(intent)
        }
        action.invoke(message)
        return true
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
        tracksRepository.loadTracksFromPlaylist(playlistId).collect { items ->
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

    private fun createSharedPlaylistPropertiesMessage(): String {
        val model = playlistProperties.value!!
        val tracks = tracksData.value!!
        var message = "${model.title}\n${model.description}\n${model.trackQuantity}"
        var count = 1
        for (track in tracks) {
            message = message + "\n" + "$count. ${track.artist} - ${track.trackName} (${
                getFormattedTrackTime(track.trackTimeMillis)
            })"
            count += 1
        }
        return message
    }
}
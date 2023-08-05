package com.example.playlistmarket.features.player.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.features.medialibrary.data.PlaylistModelsConverter
import com.example.playlistmarket.features.medialibrary.domain.PlaylistRecyclerModel
import com.example.playlistmarket.features.player.domain.interactors.TrackHandleInteractor
import com.example.playlistmarket.features.player.domain.drivers.UrlTrackPlayer
import com.example.playlistmarket.features.playlistCreator.PlaylistCreator
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository
import com.example.playlistmarket.root.domain.repository.TracksRepository
import com.example.playlistmarket.root.domain.util.convertMSecToClockFormat
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val player: UrlTrackPlayer,
    private val trackHandle: TrackHandleInteractor,
    private val tracksRepository: TracksRepository,
    private val playlistsRepository: PlaylistsRepository,
    private val converter: PlaylistModelsConverter
) : ViewModel() {
    private var timerJob: Job? = null
    private val trackPlaylistOwnersIdList = ArrayList<Int>()

    private val feedLiveData = MutableLiveData<ArrayList<PlaylistRecyclerModel>>()
    fun feedStateObserve(): LiveData<ArrayList<PlaylistRecyclerModel>> = feedLiveData

    private val trackPlayingStatusLiveData = MutableLiveData<Boolean>()
    fun trackPlayingStatusObserve(): LiveData<Boolean> = trackPlayingStatusLiveData

    private val playbackTimerActionLiveData = MutableLiveData<String>()
    fun playbackTimerActionObserve(): LiveData<String> = playbackTimerActionLiveData

    private val playerPrepareStatusLiveData = MutableLiveData<Boolean>()
    fun playerPrepareStatusObserve(): LiveData<Boolean> = playerPrepareStatusLiveData

    private val trackInFavoritesStatusLiveData = MutableLiveData<Boolean>()
    fun trackInFavoritesStatusObserve(): LiveData<Boolean> = trackInFavoritesStatusLiveData

    private val trackInPlaylistStatusLiveData = MutableLiveData<Boolean>()
    fun trackInPlaylistStatusObserve(): LiveData<Boolean> = trackInPlaylistStatusLiveData

    init {
        player.playerReadyToUse = {
            playerPrepareStatusLiveData.postValue(true)
            playbackTimerActionLiveData.postValue(
                convertMSecToClockFormat(player.getTrackDuration())
            )
        }
        player.playbackIsFinished = {
            timerJob?.cancel()
            trackPlayingStatusLiveData.postValue(false)
            playbackTimerActionLiveData.postValue(
                convertMSecToClockFormat(player.getTrackDuration())
            )
        }
        player.setTrackUrl(track.previewUrl)
        trackPlayingStatusLiveData.postValue(false)

        viewModelScope.launch {
            var status = trackHandle.getTrackInFavoritesStatus(track)
            trackInFavoritesStatusLiveData.postValue(status)

            status = trackHandle.getTrackInPlaylistsStatus(track.trackId)
            trackInPlaylistStatusLiveData.postValue(status)

            trackPlaylistOwnersIdList.addAll(
                trackHandle.getTrackPlaylistOwnersIdList(track.trackId)
            )
        }
    }

    override fun onCleared() {
        player.releasePlayerResources()
        super.onCleared()
    }

    fun changePlaybackState() {
        val isTrackPlaying = trackPlayingStatusLiveData.value!!
        if (isTrackPlaying) {
            player.stopPlayback()
            timerJob?.cancel()
        } else {
            player.startPlayback()
            startTimer()
        }
        trackPlayingStatusLiveData.postValue(!isTrackPlaying)
    }

    fun changeTrackInFavoritesState() {
        trackInFavoritesStatusLiveData.postValue(!trackInFavoritesStatusLiveData.value!!)
    }

    fun onPaused() {
        viewModelScope.launch {
            if (trackPlayingStatusLiveData.value!!) changePlaybackState()
            favoritesHandle()
            this.cancel()
        }
    }

    fun onNewPlaylistButtonClicked() {
        PlaylistCreator.start(track)
    }

    fun onPlaylistChoose(playlistId: Int, playlistTitle: String): String {
        for (item in trackPlaylistOwnersIdList) {
            if (item == playlistId) return (
                    App.appContext.getString(R.string.track_already_add) + " \"" + playlistTitle + "\""
                    )
        }
        viewModelScope.launch {
            tracksRepository.saveTrackToPlaylist(track, playlistId)
            val playlist = playlistsRepository.loadPlaylist(playlistId)
            if (playlist != null) {
                playlist.trackQuantity += 1
                playlistsRepository.updatePlaylist(playlist)
            }
            trackPlaylistOwnersIdList.add(playlistId)
            trackInPlaylistStatusLiveData.postValue(true)
        }
        return (
                App.appContext.getString(R.string.added_to_playlist) + " \"" + playlistTitle + "\""
                )
    }

    fun onAddPlaylistButtonClicked() {
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

    private suspend fun favoritesHandle() {
        val isFavoritesFlagChecked = trackInFavoritesStatusLiveData.value!!
        val isTrackAlreadyInFavorites = trackHandle.getTrackInFavoritesStatus(track)
        when (isFavoritesFlagChecked) {
            true -> {
                if (!isTrackAlreadyInFavorites) tracksRepository.saveTrackToPlaylist(track, trackHandle.getFavoritesPlaylistId())
            }
            false -> {
                if (isTrackAlreadyInFavorites) tracksRepository.deleteTrackFromPlaylist(track, trackHandle.getFavoritesPlaylistId())
            }
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (player.isPlaying) {
                delay(300L)
                playbackTimerActionLiveData.postValue(
                    convertMSecToClockFormat(
                        player.getTrackDuration() - player.getPlaybackCurrentPosition()
                    )
                )
            }
        }
    }
}
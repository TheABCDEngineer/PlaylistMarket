package com.example.playlistmarket.features.player.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.features.player.domain.interactors.TrackHandleInteractor
import com.example.playlistmarket.features.player.domain.drivers.UrlTrackPlayer
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.domain.repository.PlaylistsRepository
import com.example.playlistmarket.root.domain.util.convertMSecToClockFormat
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val player: UrlTrackPlayer,
    private val trackHandle: TrackHandleInteractor,
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {
    private  var timerJob: Job? = null

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
            trackHandle.setFavoritesPlaylist(
                playlistsRepository.loadFavoritesPlaylist()
            )
            val status = trackHandle.getTrackInFavoritesStatus(track)
            trackInFavoritesStatusLiveData.postValue(status)
        }
        trackInPlaylistStatusLiveData.postValue(
            false
        )
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

    fun changeTrackInPlaylistState() {
        trackInPlaylistStatusLiveData.postValue(!trackInPlaylistStatusLiveData.value!!)
    }

    fun onPaused() {
        viewModelScope.launch {
            if (trackPlayingStatusLiveData.value!!) changePlaybackState()
            favoritesHandle()
            this.cancel()
        }
    }

    private suspend fun favoritesHandle() {
        val isFavoritesFlagChecked = trackInFavoritesStatusLiveData.value!!
        val isTrackAlreadyInFavorites = trackHandle.getTrackInFavoritesStatus(track)
        when (isFavoritesFlagChecked) {
            true -> {
                if (!isTrackAlreadyInFavorites) trackHandle.saveTrackInFavorites(track)
            }
            false -> {
                if (isTrackAlreadyInFavorites) trackHandle.deleteTrackFromFavorites(track)
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
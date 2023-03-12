package com.example.playlistmarket.features.player.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmarket.features.player.domain.enums.PlayerPlayback
import com.example.playlistmarket.features.player.domain.interactors.PlaybackControlInteractor
import com.example.playlistmarket.features.player.domain.interactors.TrackHandleInteractor
import com.example.playlistmarket.App
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.features.main.domain.model.Track

class PlayerViewModel(
    private val track: Track,
    private val playbackControl: PlaybackControlInteractor,
    private val trackHandle: TrackHandleInteractor
) : ViewModel(), Observer {

    companion object {
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer { Creator.createPlayerViewModel(track) }
        }
    }

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
        playbackControl.addObserver(this)
        trackPlayingStatusLiveData.postValue(false)
        trackInFavoritesStatusLiveData.postValue(trackHandle.getTrackInFavoritesStatus(track))
        trackInPlaylistStatusLiveData.postValue(trackHandle.getTrackInPlaylistsStatus(track))
    }

    fun changePlaybackState() {
        val isTrackPlaying = trackPlayingStatusLiveData.value!!
        if (isTrackPlaying) {
            playbackControl.paused()
        } else {
            playbackControl.start()
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
        val isFavoritesFlagChecked = trackInFavoritesStatusLiveData.value!!
        val isTrackAlreadyInFavorites = trackHandle.getTrackInFavoritesStatus(track)

        when (isFavoritesFlagChecked) {
            true -> {
                if (!isTrackAlreadyInFavorites) trackHandle.addTrackToPlaylist(
                    track,
                    App.FAVORITES_LIST_KEY
                )
            }
            false -> {
                if (isTrackAlreadyInFavorites) trackHandle.deleteTrackFromPlaylist(
                    track,
                    App.FAVORITES_LIST_KEY
                )
            }
        }

        if (trackPlayingStatusLiveData.value!!) changePlaybackState()
    }

    override fun onCleared() {
        playbackControl.releaseResources()
        super.onCleared()
    }

    override fun <S, T> notifyObserver(event: S?, data: T?) {
        when (event) {
            PlayerPlayback.IS_PREPARED -> {
                playerPrepareStatusLiveData.postValue(true)
                playbackTimerActionLiveData.postValue(data as String)
            }
            PlayerPlayback.IS_FINISHED -> {
                trackPlayingStatusLiveData.postValue(false)
                playbackTimerActionLiveData.postValue(data as String)
            }
            PlayerPlayback.TIMER_ACTION -> {
                playbackTimerActionLiveData.postValue(data as String)
            }
        }
    }
}
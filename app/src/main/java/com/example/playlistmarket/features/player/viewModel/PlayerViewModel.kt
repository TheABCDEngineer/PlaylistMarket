package com.example.playlistmarket.features.player.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmarket.features.player.viewModel.enums.PlayerPlayback
import com.example.playlistmarket.features.player.viewModel.interactors.PlaybackInteractor
import com.example.playlistmarket.features.player.viewModel.interactors.TrackHandleInteractor
import com.example.playlistmarket.App
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.domain.model.Track

class PlayerViewModel(
    private val track: Track,
    private val playback: PlaybackInteractor,
    private val trackHandle: TrackHandleInteractor
) : ViewModel(), Observer {

    companion object {
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer { Creator.createPlayerViewModel(track) }
        }
    }

    private val trackPlayingStatusLiveData = MutableLiveData<Boolean>()
    fun trackPlayingStatusObserve(): LiveData<Boolean> = trackPlayingStatusLiveData

    private val playbackTimerLiveData = MutableLiveData<String>()
    fun playbackTimerObserve(): LiveData<String> = playbackTimerLiveData

    private val playerPrepareStatusLiveData = MutableLiveData<Boolean>()
    fun playerPrepareStatusObserve(): LiveData<Boolean> = playerPrepareStatusLiveData

    private val trackInFavoritesStatusLiveData = MutableLiveData<Boolean>()
    fun trackInFavoritesStatusObserve(): LiveData<Boolean> = trackInFavoritesStatusLiveData

    private val trackInPlaylistStatusLiveData = MutableLiveData<Boolean>()
    fun trackInPlaylistStatusObserve(): LiveData<Boolean> = trackInPlaylistStatusLiveData

    init {
        playback.addObserver(this)
        playback.preparePlayer()
        trackPlayingStatusLiveData.postValue(false)
        trackInFavoritesStatusLiveData.postValue(trackHandle.getTrackInFavoritesStatus(track))
        trackInPlaylistStatusLiveData.postValue(trackHandle.getTrackInPlaylistsStatus(track))
    }

    fun changePlaybackState() {
        val isTrackPlaying = !trackPlayingStatusLiveData.value!!
        trackPlayingStatusLiveData.postValue(isTrackPlaying)
        startPlayback(isTrackPlaying)
    }

    fun changeTrackInFavoritesState() {
        trackInFavoritesStatusLiveData.postValue(!trackInFavoritesStatusLiveData.value!!)
    }

    fun changeTrackInPlaylistState() {
        trackInPlaylistStatusLiveData.postValue(!trackInPlaylistStatusLiveData.value!!)
    }

    private fun startPlayback(isPlayback: Boolean) {
        playback.executePlayback(isPlayback)
    }

    fun onResume() {
        if (playback.getPrepareStatus()) {
            playerPrepareStatusLiveData.postValue(true)
        } else {
            playerPrepareStatusLiveData.postValue(false)
        }
    }

    fun onPaused() {
        if (trackInFavoritesStatusLiveData.value!!) {
            if (!trackHandle.getTrackInFavoritesStatus(track)) {
                trackHandle.addTrackToPlaylist(track, App.FAVORITES_LIST_KEY)
            }
        } else {
            if (!trackHandle.getTrackInFavoritesStatus(track)) {
                trackHandle.deleteTrackFromPlaylist(track, App.FAVORITES_LIST_KEY)
            }
        }
        if (trackPlayingStatusLiveData.value!!) changePlaybackState()
    }

    override fun onCleared() {
        playback.destroyPlayer()
        super.onCleared()
    }

    override fun <S, T> notifyObserver(event: S?, data: T?) {
        when (event) {
            PlayerPlayback.IS_PREPARED -> {
                playerPrepareStatusLiveData.postValue(data as Boolean)
                if (data) playbackTimerLiveData.postValue(playback.getPlaybackDuration())
            }
            PlayerPlayback.PLAYBACK_FINISHED -> {
                trackPlayingStatusLiveData.postValue(data as Boolean)
                playbackTimerLiveData.postValue(playback.getPlaybackDuration())
            }
            PlayerPlayback.PLAYBACK_TIMER -> playbackTimerLiveData.postValue(data as String)
        }
    }
}
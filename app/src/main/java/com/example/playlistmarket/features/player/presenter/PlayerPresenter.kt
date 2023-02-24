package com.example.playlistmarket.features.player.presenter

import com.example.playlistmarket.creator.Presenter
import com.example.playlistmarket.features.player.presenter.enums.PlayerPlayback
import com.example.playlistmarket.features.player.presenter.interactors.PlaybackInteractor
import com.example.playlistmarket.features.player.presenter.interactors.TrackHandleInteractor
import com.example.playlistmarket.App
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.domain.models.Track

class PlayerPresenter(
    var view: PlayerView,
    private val track: Track,
    private val playback: PlaybackInteractor,
    private val trackHandle: TrackHandleInteractor
) : Presenter(), Observer {

    private var isTrackInFavorites = trackHandle.getTrackInFavoritesStatus(track)
    private var isTrackInPlaylist = trackHandle.getTrackInPlaylistsStatus(track)
    private var isTrackPlaying = false

    init {
        playback.addObserver(this)
        playback.preparePlayer()
    }

    fun changePlaybackState() {
        isTrackPlaying = !isTrackPlaying
        startPlayback(isTrackPlaying)
        view.updateTrackPlayingStatus(isTrackPlaying)
    }

    fun changeTrackInFavoritesState() {
        isTrackInFavorites = !isTrackInFavorites
        view.updateTrackInFavoritesStatus(isTrackInFavorites)
    }

    fun changeTrackInPlaylistState() {
        isTrackInPlaylist = !isTrackInPlaylist
        view.updateTrackInPlaylistsStatus(isTrackInPlaylist)
    }

    private fun startPlayback(isPlayback: Boolean) {
        playback.executePlayback(isPlayback)
    }

    override fun onViewResume() {
        super.onViewResume()
        view.apply {
            //showTrackProperties()
            updateTrackInFavoritesStatus(isTrackInFavorites)
            updateTrackInPlaylistsStatus(isTrackInPlaylist)
            updateProgressOnPrepare(false)
        }

        if (playback.getPrepareStatus()) {
            view.updateProgressOnPrepare(true)
        }
    }

    override fun onViewStop() {
        if (isTrackInFavorites) {
            if (!trackHandle.getTrackInFavoritesStatus(track)) {
                trackHandle.addTrackToPlaylist(track, App.FAVORITES_LIST_KEY)
            }
        }

        if (!isTrackInFavorites) {
            if (!trackHandle.getTrackInFavoritesStatus(track)) {
                trackHandle.deleteTrackFromPlaylist(track, App.FAVORITES_LIST_KEY)
            }
        }
        if (isTrackPlaying) changePlaybackState()

        super.onViewStop()
    }

    override fun onViewDestroy() {
        if (isDestroyAllowed) playback.destroyPlayer()
        super.onViewDestroy()
    }

    override fun <S, T> notifyObserver(event: S?, data: T?) {
        when (event) {
            PlayerPlayback.IS_PREPARED -> {
                view.updateProgressOnPrepare(data as Boolean)
                if (data) view.updatePlaybackTimer(playback.getPlaybackDuration())
            }
            PlayerPlayback.PLAYBACK_FINISHED -> {
                view.updateTrackPlayingStatus(data as Boolean)
                view.updatePlaybackTimer(playback.getPlaybackDuration())
            }
            PlayerPlayback.PLAYBACK_TIMER -> view.updatePlaybackTimer(data as String)
        }
    }

    override fun implementPresenterDestroyingMethod(): () -> Unit = {
        Creator.playerPresenter = null
    }
}
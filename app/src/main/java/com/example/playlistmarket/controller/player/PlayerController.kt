package com.example.playlistmarket.controller.player

import com.example.playlistmarket.controller.Controller
import com.example.playlistmarket.controller.player.enums.PlayerPlayback
import com.example.playlistmarket.controller.player.interactors.PlaybackInteractor
import com.example.playlistmarket.controller.player.interactors.TrackHandleInteractor
import com.example.playlistmarket.controller.player.interactors.TrackPropertiesInteractor
import com.example.playlistmarket.creator.App
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.interactors.Observer
import com.example.playlistmarket.domain.models.Track

class PlayerController(
    var view: PlayerView,
    private val track: Track,
    private val playback: PlaybackInteractor,
    private val trackHandle: TrackHandleInteractor,
    private val trackProperties: TrackPropertiesInteractor
) : Controller(), Observer {

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
            showTrackProperties(trackProperties.getTrackProperties(track))
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

    override fun onViewDestroy(destroyController: () -> Unit) {
        if (isDestroyAllowed) playback.destroyPlayer()
        super.onViewDestroy(destroyController)
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

    override fun implementControllerDestroyingMethod(): () -> Unit = {
        Creator.playerController = null
    }
}
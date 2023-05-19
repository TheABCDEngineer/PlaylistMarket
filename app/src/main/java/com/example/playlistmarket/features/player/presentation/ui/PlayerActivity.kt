package com.example.playlistmarket.features.player.presentation.ui

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.features.player.presentation.viewModel.PlayerViewModel
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.features.player.presentation.ui.widgets.TrackPropertiesWidget
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    private val track: Track by lazy { intent.getParcelableExtra(App.TRACK_KEY)!! }
    private val goBackButton: ImageButton by lazy { findViewById(R.id.player_back_button) }
    private val playButton: ImageButton by lazy { findViewById(R.id.player_play_button) }
    private val addToPlaylistButton: ImageButton by lazy { findViewById(R.id.player_add_to_playlist_button) }
    private val addToFavoritesButton: ImageButton by lazy { findViewById(R.id.player_add_to_favorites_button) }
    private val playerTimer: TextView by lazy { findViewById(R.id.player_track_timer) }
    private val progressBar: ProgressBar by lazy { findViewById(R.id.player_progressBar) }
    private val trackPropertiesWidget: TrackPropertiesWidget by lazy { TrackPropertiesWidget(this@PlayerActivity) }

    private val viewModel by viewModel<PlayerViewModel> {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        supportActionBar?.hide()

        viewModel.apply {
            trackPlayingStatusObserve().observe(this@PlayerActivity) {
                updateTrackPlayingStatus(it)
            }
            playbackTimerActionObserve().observe(this@PlayerActivity) {
                updatePlaybackTimer(it)
            }
            playerPrepareStatusObserve().observe(this@PlayerActivity) {
                updateViewOnPlayerPrepared(it)
            }
            trackInFavoritesStatusObserve().observe(this@PlayerActivity) {
                updateTrackInFavoritesStatus(it)
            }
            trackInPlaylistStatusObserve().observe(this@PlayerActivity) {
                updateTrackInPlaylistsStatus(it)
            }
        }

        goBackButton.setOnClickListener {
            onBackPressed()
        }
        playButton.setOnClickListener {
            viewModel.changePlaybackState()
        }
        addToFavoritesButton.setOnClickListener {
            viewModel.changeTrackInFavoritesState()
        }
        addToPlaylistButton.setOnClickListener {
            viewModel.changeTrackInPlaylistState()
        }

        trackPropertiesWidget.showTrackProperties(track)
    }

    private fun updateTrackPlayingStatus(isPlaying: Boolean) {
        when (isPlaying) {
            true -> playButton.setImageResource(R.drawable.player_pause_icon)
            false -> playButton.setImageResource(R.drawable.player_play_icon)
        }
    }

    private fun updatePlaybackTimer(time: String) {
        playerTimer.text = time
    }

    private fun updateViewOnPlayerPrepared(isPrepared: Boolean) {
        progressBar.isVisible = !isPrepared
        playButton.isEnabled = isPrepared
    }

    private fun updateTrackInFavoritesStatus(isInFavorites: Boolean) {
        when (isInFavorites) {
            true -> addToFavoritesButton.setImageResource(R.drawable.player_add_to_favorites_done)
            false -> addToFavoritesButton.setImageResource(R.drawable.player_add_to_favorites_available)
        }
    }

    private fun updateTrackInPlaylistsStatus(isInPlaylists: Boolean) {
        when (isInPlaylists) {
            true -> addToPlaylistButton.setImageResource(R.drawable.player_add_to_playlist_done)
            false -> addToPlaylistButton.setImageResource(R.drawable.player_add_to_playlist_available)
        }
    }

    override fun onPause() {
        viewModel.onPaused()
        super.onPause()
    }
}
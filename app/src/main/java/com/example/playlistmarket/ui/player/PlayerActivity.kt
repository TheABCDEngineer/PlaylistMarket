package com.example.playlistmarket.ui.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.playlistmarket.creator.App
import com.example.playlistmarket.R
import com.example.playlistmarket.controller.player.PlayerController
import com.example.playlistmarket.controller.player.PlayerView
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.domain.models.TrackPropertiesModel
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.ControlledActivity
import com.example.playlistmarket.ui.player.widgets.TrackPropertiesWidget

class PlayerActivity : AppCompatActivity(), PlayerView, ControlledActivity {
    private val track: Track by lazy { intent.getParcelableExtra(App.TRACK_KEY)!! }
    private val goBackButton: ImageButton by lazy { findViewById(R.id.player_back_button) }
    private val playButton: ImageButton by lazy { findViewById(R.id.player_play_button) }
    private val addToPlaylistButton: ImageButton by lazy { findViewById(R.id.player_add_to_playlist_button) }
    private val addToFavoritesButton: ImageButton by lazy { findViewById(R.id.player_add_to_favorites_button) }
    private val playerTimer: TextView by lazy { findViewById(R.id.player_track_timer) }
    private val progressBar: ProgressBar by lazy { findViewById(R.id.player_progressBar) }
    private val trackPropertiesWidget: TrackPropertiesWidget by lazy { TrackPropertiesWidget(this@PlayerActivity) }
    override val controller: PlayerController by lazy { Creator.playerController!! }

    companion object {
        private const val PLAYBACK_TIMER_VALUE = "playback_timer_value"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        supportActionBar?.hide()

        createController()

        goBackButton.setOnClickListener {
            onBackPressed()
        }
        playButton.setOnClickListener {
            controller.changePlaybackState()
        }
        addToFavoritesButton.setOnClickListener {
            controller.changeTrackInFavoritesState()
        }
        addToPlaylistButton.setOnClickListener {
            controller.changeTrackInPlaylistState()
        }

        playerTimer.text = savedInstanceState?.getString(PLAYBACK_TIMER_VALUE)
    }

    override fun createController() {
        Creator.createPlayerController(this@PlayerActivity, track)
    }

    override fun updateTrackPlayingStatus(isPlaying: Boolean) {
        when (isPlaying) {
            true -> playButton.setImageResource(R.drawable.player_pause_icon)
            false -> playButton.setImageResource(R.drawable.player_play_icon)
        }
    }

    override fun updatePlaybackTimer(time: String) {
        playerTimer.text = time
    }

    override fun updateProgressOnPrepare(isPrepared: Boolean) {
        progressBar.isVisible = !isPrepared
        playButton.isEnabled = isPrepared
    }

    override fun updateTrackInFavoritesStatus(isInFavorites: Boolean) {
        when (isInFavorites) {
            true -> addToFavoritesButton.setImageResource(R.drawable.player_add_to_favorites_done)
            false -> addToFavoritesButton.setImageResource(R.drawable.player_add_to_favorites_available)
        }
    }

    override fun updateTrackInPlaylistsStatus(isInPlaylists: Boolean) {
        when (isInPlaylists) {
            true -> addToPlaylistButton.setImageResource(R.drawable.player_add_to_playlist_done)
            false -> addToPlaylistButton.setImageResource(R.drawable.player_add_to_playlist_available)
        }
    }

    override fun showTrackProperties(model: TrackPropertiesModel) {
        trackPropertiesWidget.showTrackProperties(model)
    }

    override fun onResume() {
        super<AppCompatActivity>.onResume()
        super<ControlledActivity>.onResume()
    }

    override fun onBackPressed() {
        super<ControlledActivity>.onBackPressed()
        super<AppCompatActivity>.onBackPressed()
    }

    override fun onStop() {
        super<ControlledActivity>.onStop()
        super<AppCompatActivity>.onStop()
    }

    override fun onDestroy() {
        super<ControlledActivity>.onDestroy()
        super<AppCompatActivity>.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PLAYBACK_TIMER_VALUE, playerTimer.text.toString())
    }
}
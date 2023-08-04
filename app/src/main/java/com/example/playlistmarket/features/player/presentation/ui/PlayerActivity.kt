package com.example.playlistmarket.features.player.presentation.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.features.medialibrary.domain.PlaylistRecyclerModel
import com.example.playlistmarket.features.player.presentation.ui.recyclerView.PlayerPlaylistAdapter
import com.example.playlistmarket.features.player.presentation.viewModel.PlayerViewModel
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.features.player.presentation.ui.widgets.TrackPropertiesWidget
import com.example.playlistmarket.root.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    private lateinit var track: Track
    private val goBackButton: ImageButton by lazy { findViewById(R.id.player_back_button) }
    private val playButton: ImageButton by lazy { findViewById(R.id.player_play_button) }
    private val addToPlaylistButton: ImageButton by lazy { findViewById(R.id.player_add_to_playlist_button) }
    private val addToFavoritesButton: ImageButton by lazy { findViewById(R.id.player_add_to_favorites_button) }
    private val playerTimer: TextView by lazy { findViewById(R.id.player_track_timer) }
    private val progressBar: ProgressBar by lazy { findViewById(R.id.player_progressBar) }
    private val trackPropertiesWidget: TrackPropertiesWidget by lazy { TrackPropertiesWidget(this@PlayerActivity) }

    private val bottomSheetBehavior: BottomSheetBehavior<LinearLayout> by lazy {
        BottomSheetBehavior.from(findViewById(R.id.player_bottom_sheet))
    }
    private val newPlaylistButton: Button by lazy { findViewById(R.id.player_bottom_sheet_new_playlist_button) }
    private val playlistTable: RecyclerView by lazy { findViewById(R.id.player_bottom_sheet_playlists_table) }
    private val onAdapterItemClickedAction: (PlaylistRecyclerModel) -> Unit
        get() = debounce(App.CLICK_DEBOUNCE_DELAY_MILLIS, lifecycleScope) { playlist ->
            makeToast(
                viewModel.onPlaylistChoose(playlist.id, playlist.title)
            )
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    private val adapter = PlayerPlaylistAdapter(ArrayList(), onAdapterItemClickedAction)
    private val overlay: View by lazy { findViewById(R.id.player_overlay) }

    private val viewModel by viewModel<PlayerViewModel> {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        supportActionBar?.hide()

        track = if (Build.VERSION.SDK_INT < 33)
            intent.getParcelableExtra(App.TRACK_KEY)!!
        else
            intent.getParcelableExtra(App.TRACK_KEY, Track::class.java)!!

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
            feedStateObserve().observe(this@PlayerActivity) {
                updatePlaylistFeed(it)
            }
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.isVisible = false
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = ((slideOffset + 1.0) / 2).toFloat()
            }
        })

        goBackButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        playButton.setOnClickListener {
            viewModel.changePlaybackState()
        }
        addToFavoritesButton.setOnClickListener {
            viewModel.changeTrackInFavoritesState()
        }
        addToPlaylistButton.setOnClickListener {
            overlay.alpha = 0F
            overlay.isVisible = true
            viewModel.onAddPlaylistButtonClicked()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        newPlaylistButton.setOnClickListener {
            viewModel.onNewPlaylistButtonClicked()
            debounce<Unit>(1000,lifecycleScope) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }.invoke(Unit)
        }
        playlistTable.adapter = adapter
        trackPropertiesWidget.showTrackProperties(track)
    }

    override fun onPause() {
        viewModel.onPaused()
        super.onPause()
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

    private fun updatePlaylistFeed(items: ArrayList<PlaylistRecyclerModel>) {
        adapter.updateItems(items)
        playlistTable.adapter?.notifyDataSetChanged()
    }

    private fun makeToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}
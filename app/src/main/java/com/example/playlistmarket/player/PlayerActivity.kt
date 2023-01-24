package com.example.playlistmarket.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.medialibrary.Track
import com.example.playlistmarket.player.widgets.TrackHandleWidget
import com.example.playlistmarket.player.widgets.TrackPropertiesWidget

class PlayerActivity : AppCompatActivity() {
    private lateinit var goBackButton: ImageButton
    private lateinit var trackHandle: TrackHandle
    lateinit var trackHandleWidget: TrackHandleWidget
    private lateinit var trackPropertiesWidget: TrackPropertiesWidget
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        supportActionBar?.hide()

        initializeVariables()

        setOnClickListenersAtViews()
    }

    private fun initializeVariables() {
        goBackButton = findViewById(R.id.player_back_button)

        track = intent.getParcelableExtra(App.TRACK_KEY)!!

        trackHandleWidget = TrackHandleWidget(
            track,
            findViewById(R.id.player_play_button),
            findViewById(R.id.player_add_to_playlist_button),
            findViewById(R.id.player_add_to_favorites_button),
            findViewById(R.id.player_track_timer),
            findViewById(R.id.player_progressBar)
        )

        trackPropertiesWidget = TrackPropertiesWidget(
            track,
            findViewById(R.id.player_artwork),
            findViewById(R.id.player_track_name),
            findViewById(R.id.player_artist_name),
            findViewById(R.id.player_track_value_length),
            findViewById(R.id.player_track_value_album),
            findViewById(R.id.player_track_value_release),
            findViewById(R.id.player_track_value_genre),
            findViewById(R.id.player_track_value_country)
        )

        trackHandle = TrackHandle(
            App.sharedPref,
            App.favoritesFile,
            track,
            this@PlayerActivity
        )
    }

    private fun setOnClickListenersAtViews() {
        goBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        trackHandleWidget.isTrackPlaying = false
        trackHandle.onPausedParentActivity()
    }

    override fun onDestroy() {
        super.onDestroy()
        trackHandleWidget.apply {
            mediaPlayer.release()
            App.mainHandler.removeCallbacks(playerTimer)
        }
        trackHandle.onPausedParentActivity()
    }
}
package com.example.playlistmarket.features.player.presentation

import android.content.Intent
import com.example.playlistmarket.App.Companion.appContext
import com.example.playlistmarket.App.Companion.TRACK_KEY
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.features.player.presentation.ui.PlayerActivity

object Player {
    fun start(track: Track?) {
        if (track == null) return

        appContext.startActivity(
            Intent(appContext, PlayerActivity::class.java).apply {
                putExtra(TRACK_KEY, track)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }
}
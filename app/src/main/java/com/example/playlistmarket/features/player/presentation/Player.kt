package com.example.playlistmarket.features.player.presentation

import android.content.Intent
import com.example.playlistmarket.App
import com.example.playlistmarket.base.clickDebounce
import com.example.playlistmarket.base.domain.model.Track
import com.example.playlistmarket.features.player.presentation.ui.PlayerActivity

class Player {
    companion object {
        fun start(track: Track?) {
            if (track == null) return
            if (!clickDebounce(App.playerAllowed) { App.playerAllowed = it }) return

            val context = App.appContext
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(App.TRACK_KEY, track)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}
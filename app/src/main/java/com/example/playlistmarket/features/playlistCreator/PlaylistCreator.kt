package com.example.playlistmarket.features.playlistCreator

import android.content.Intent
import com.example.playlistmarket.App
import com.example.playlistmarket.features.playlistCreator.presentation.ui.PlaylistCreatorActivity
import com.example.playlistmarket.root.domain.model.Track

class PlaylistCreator {
    companion object {
        fun start(track: Track?) {
            App.appContext.startActivity(
                Intent(App.appContext, PlaylistCreatorActivity::class.java).apply {
                    putExtra(App.TRACK_KEY, track)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        }
    }
}
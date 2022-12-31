package com.example.playlistmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.player.PlayerActivity

fun startPlayer(track: Track?) {
    if (track == null) return
    val context = App.appContext
    val intent = Intent(context, PlayerActivity::class.java)
    intent.putExtra(App.TRACK_KEY, track)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

fun setDarkMode(status: Boolean) {
    when (status) {
        true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
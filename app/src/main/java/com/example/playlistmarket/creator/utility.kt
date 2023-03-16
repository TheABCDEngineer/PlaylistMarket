package com.example.playlistmarket.creator

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.App
import com.example.playlistmarket.features.main.domain.model.Track
import com.example.playlistmarket.features.player.presentation.ui.PlayerActivity

fun startPlayer(track: Track?) {
    if (track == null) return
    if (!clickDebounce(App.playerAllowed) { App.playerAllowed = it }) return

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

fun clickDebounce(
    isClickAllowed: Boolean,
    changeClicker: (Boolean) -> Unit
): Boolean {
    if (isClickAllowed) {
        changeClicker(false)
        App.mainHandler.postDelayed({ changeClicker(true) }, 1000L)
    }
    return isClickAllowed
}

fun hideKeyboard(activity: AppCompatActivity) {
    activity.currentFocus?.let { view ->
        val inputMethodManager =
            App.appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

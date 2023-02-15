package com.example.playlistmarket.creator

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.ui.player.PlayerActivity
import java.text.SimpleDateFormat
import java.util.*

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

fun convertMSecToClockFormat(value: String): String {
    val format = try {
        if ((value.toLong() / 3600000) > 0) "hh:mm:ss" else "mm:ss"
    } catch (e: NumberFormatException) {
        null
    } ?: return "0"

    return try {
        SimpleDateFormat(
            format, Locale.getDefault()
        ).format(
            value.toLong()
        )
    } catch (e: IllegalArgumentException) {
        "0"
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

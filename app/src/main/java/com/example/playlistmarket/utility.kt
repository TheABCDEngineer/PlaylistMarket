package com.example.playlistmarket

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.medialibrary.Track
import com.example.playlistmarket.player.PlayerActivity
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

inline fun <reified T> loadListFromFileOnKey(
    file: SharedPreferences,
    key: String,
    clazz: Class<Array<T>>
): Array<T> {
    val json: String = file.getString(key, null) ?: return emptyArray()
    return Gson().fromJson(json, clazz)
}

fun <T> saveListToFileOnKey(
    file: SharedPreferences,
    key: String,
    list: ArrayList<T>
) {
    val json = Gson().toJson(list)
    file.edit()
        .putString(key, json)
        .apply()
}

fun deleteFromFileOnKey(
    file: SharedPreferences,
    key: String
) {
    file.edit()
        .remove(key)
        .apply()
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

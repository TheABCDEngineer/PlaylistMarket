package com.example.playlistmarket.base.domain.util

import com.example.playlistmarket.App
import com.example.playlistmarket.R
import java.text.SimpleDateFormat
import java.util.*

fun getFormattedTrackTime(trackTimeMillis: Int): String {
    if (trackTimeMillis == 0) return App.appContext.getString(R.string.no_data)

    val result = convertMSecToClockFormat(trackTimeMillis)
    return if (result == "0") App.appContext.getString(R.string.no_data) else result
}

fun getArtwork(url: String?, frame: Int): String {
    if (url == null) return ""
    val size: Int = when (frame) {
        512 -> 512
        else -> 100
    }
    return url.replaceAfterLast('/', "${size}x${size}bb.jpg")
}

fun convertMSecToClockFormat(value: Int): String {
    val format = try {
        if ((value / 3600000) > 0) "hh:mm:ss" else "mm:ss"
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
package com.example.playlistmarket

import java.text.SimpleDateFormat
import java.util.*

data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String?,
    val artworkUrl100: String,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    fun formatTrackTimeFromMillis() = SimpleDateFormat(
        "mm:ss", Locale.getDefault()
    ).format(
        trackTimeMillis?.toInt()
    )
        ?: App.appContext.getString(R.string.no_track_time_from_response)


    fun getYearFromReleaseDate() = releaseDate?.substring(0, 4)
        ?: App.appContext.getString(R.string.no_track_time_from_response)
}
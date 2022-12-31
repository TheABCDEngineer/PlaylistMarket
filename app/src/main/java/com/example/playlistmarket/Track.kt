package com.example.playlistmarket

import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

@kotlinx.parcelize.Parcelize
data class Track(
    private val trackId: String?,
    private val trackName: String?,
    private val artistName: String?,
    private val trackTimeMillis: String?,
    private val artworkUrl100: String?,
    private val collectionName: String?,
    private val releaseDate: String?,
    private val primaryGenreName: String?,
    private val country: String?
) : Parcelable {

    fun getTrackId(): String {
        if (trackId != null) return trackId

        var sample = ""
        if (trackName != null) sample += trackName
        if (artistName != null) sample += artistName
        if (trackTimeMillis != null) sample += trackTimeMillis
        if (collectionName != null) sample += collectionName
        if (releaseDate != null) sample += releaseDate
        if (primaryGenreName != null) sample += primaryGenreName
        if (country != null) sample += country

        return sample.hashCode().toString()
    }

    fun getTrackName() = trackName ?: App.appContext.getString(R.string.no_title)

    fun getArtistName() = artistName ?: App.appContext.getString(R.string.no_title)

    fun getTrackTime(format: String): String {
        if (trackTimeMillis == null) return App.appContext.getString(R.string.no_data)
        return try {
            SimpleDateFormat(
                format, Locale.getDefault()
            ).format(
                trackTimeMillis.toInt()
            )
        } catch (e: IllegalArgumentException) {
            App.appContext.getString(R.string.no_data)
        }
    }

    fun getArtwork(frame: Int): String {
        if (artworkUrl100 == null) return ""
        val size: Int = when (frame) {
            512 -> 512
            else -> 100
        }
        return artworkUrl100!!.replaceAfterLast('/', "${size}x${size}bb.jpg")
    }

    fun getCollectionName() = collectionName ?: App.appContext.getString(R.string.no_title)

    fun getYearFromReleaseDate() = releaseDate?.substring(0, 4) ?: App.appContext.getString(R.string.no_data)

    fun getGenre() = primaryGenreName ?: App.appContext.getString(R.string.no_data)

    fun getCountry() = country ?: App.appContext.getString(R.string.no_data)

}
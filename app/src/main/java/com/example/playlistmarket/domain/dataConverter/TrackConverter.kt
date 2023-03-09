package com.example.playlistmarket.domain.dataConverter

import com.example.playlistmarket.R
import com.example.playlistmarket.App
import com.example.playlistmarket.features.search.data.dto.TrackDto
import com.example.playlistmarket.domain.model.Track
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object TrackConverter {

    private fun convertDtoToTrackModel(dto: TrackDto): Track {
        return Track(
            dto.trackId ?: dto.hashCode().toString(),
            dto.trackName ?: App.appContext.getString(R.string.no_title),
            dto.artistName ?: App.appContext.getString(R.string.no_title),
            dto.let { dto.trackTimeMillis?.toInt() }  ?: 0,
            getArtwork(dto.artworkUrl100,100),
            dto.collectionName ?: App.appContext.getString(R.string.no_title),
            dto.let { dto.releaseDate?.substring(0, 4) } ?: App.appContext.getString(R.string.no_data),
            dto.primaryGenreName ?: App.appContext.getString(R.string.no_data),
            dto.country ?: App.appContext.getString(R.string.no_data),
            dto.previewUrl ?: ""
        )
    }

    fun convertDtoToTrackModel(listDto: ArrayList<TrackDto>): ArrayList<Track> {
        val result = ArrayList<Track>()
        for (i in listDto) result.add(convertDtoToTrackModel(i))
        return result
    }

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
}
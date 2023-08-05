package com.example.playlistmarket.features.medialibrary.data

import android.net.Uri
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.features.medialibrary.domain.PlaylistRecyclerModel
import com.example.playlistmarket.features.medialibrary.domain.PlaylistScreenModel
import com.example.playlistmarket.root.domain.model.Playlist
import com.example.playlistmarket.root.domain.repository.PlaylistArtworksRepository
import kotlinx.coroutines.runBlocking

class PlaylistModelsConverter(
    private val artworksRepository: PlaylistArtworksRepository
) {
    fun mapToRecyclerModel(playlist: Playlist): PlaylistRecyclerModel {
        var artworkUri: Uri?
        runBlocking {
            artworkUri = artworksRepository.loadArtwork(playlist.id.toString())
        }
        return PlaylistRecyclerModel(
            playlist.id,
            artworkUri,
            playlist.title,
            getStringQuantity(playlist.trackQuantity)
        )
    }

    fun mapToScreenModel(playlist: Playlist, totalDuration: Int): PlaylistScreenModel {
        var artworkUri: Uri?
        runBlocking {
            artworkUri = artworksRepository.loadArtwork(playlist.id.toString())
        }
        val description =
            if (playlist.description == "") App.appContext.getString(R.string.no_description) else playlist.description
        return PlaylistScreenModel(
            artworkUri,
            playlist.title,
            description,
            getStringTotalDuration(totalDuration),
            getStringQuantity(playlist.trackQuantity)
        )
    }

    private fun getStringQuantity(quantity: Int): String {
        val ending = when (quantity.toString().takeLast(1).toInt()) {
            1 -> App.appContext.getString(R.string.track_1)
            2,3,4 -> App.appContext.getString(R.string.tracks_2_3_4)
            else -> App.appContext.getString(R.string.tracks_of)
        }
        return "$quantity $ending"
    }

    private fun getStringTotalDuration(duration: Int): String {
        val durationMinutes = String.format("%.0f",(duration.toDouble())/1000/60)
        val ending = when (durationMinutes.takeLast(1).toInt()) {
            1 -> App.appContext.getString(R.string.minute_1)
            2,3,4 -> App.appContext.getString(R.string.minutes_2_3_4)
            else -> App.appContext.getString(R.string.minutes_of)
        }
        return "$durationMinutes $ending"
    }
}
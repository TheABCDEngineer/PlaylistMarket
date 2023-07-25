package com.example.playlistmarket.features.medialibrary.data

import android.net.Uri
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.features.medialibrary.domain.PlaylistRecyclerModel
import com.example.playlistmarket.root.domain.model.Playlist
import com.example.playlistmarket.root.domain.repository.PlaylistArtworksRepository
import kotlinx.coroutines.runBlocking

class PlaylistModelsConverter(
    private val artworksRepository: PlaylistArtworksRepository
) {
    fun map(playlist: Playlist): PlaylistRecyclerModel {
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

    private fun getStringQuantity(quantity: Int): String {
        val ending = when (quantity.toString().takeLast(1).toInt()) {
            1 -> App.appContext.getString(R.string.track_1)
            2,3,4 -> App.appContext.getString(R.string.tracks_2_3_4)
            else -> App.appContext.getString(R.string.tracks_of)
        }
        return "$quantity $ending"
    }
}
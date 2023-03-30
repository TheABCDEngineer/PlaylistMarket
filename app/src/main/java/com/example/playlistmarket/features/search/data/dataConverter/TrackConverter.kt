package com.example.playlistmarket.features.search.data.dataConverter

import com.example.playlistmarket.R
import com.example.playlistmarket.App
import com.example.playlistmarket.features.search.data.dto.TrackDto
import com.example.playlistmarket.base.domain.model.Track
import kotlin.collections.ArrayList

object TrackConverter {

    private fun convertDtoToTrackModel(dto: TrackDto): Track {
        return Track(
            dto.trackId ?: dto.hashCode().toString(),
            dto.trackName ?: App.appContext.getString(R.string.no_title),
            dto.artistName ?: App.appContext.getString(R.string.no_title),
            dto.let { dto.trackTimeMillis?.toInt() }  ?: 0,
            //getArtwork(dto.artworkUrl100,100),
            dto.artworkUrl100 ?:App.appContext.getString(R.string.no_data),
            dto.collectionName ?: App.appContext.getString(R.string.no_title),
            dto.let { dto.releaseDate?.substring(0, 4) } ?: App.appContext.getString(R.string.no_data),
            dto.primaryGenreName ?: App.appContext.getString(R.string.no_data),
            dto.country ?: App.appContext.getString(R.string.no_data),
            dto.previewUrl ?: ""
        )
    }

    fun convertDtoToTrackModel(listDto: ArrayList<TrackDto>): ArrayList<Track> {
        val result = ArrayList<Track>()
        for (dto in listDto) result.add(convertDtoToTrackModel(dto))
        return result
    }
}
package com.example.playlistmarket.domain.models

import com.example.playlistmarket.R
import com.example.playlistmarket.creator.App

class TrackPropertiesModel(track: Track) {
    val artwork = track.getArtwork(512)
    val artworkDefault = App.appContext.getDrawable(R.drawable.default_album_image)
    val trackName = track.getTrackName()
    val artistName = track.getArtistName()
    val lenght = track.getFormattedTrackTime(true)
    val album = track.getCollectionName()
    val release = track.getReleaseYear()
    val genre = track.getGenre()
    val country = track.getCountry()
}
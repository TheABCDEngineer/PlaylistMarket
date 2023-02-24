package com.example.playlistmarket.domain.models

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Track(
    val trackId: String,
    val trackName: String,
    val artist: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val collection: String,
    val releaseYear: String,
    val genre: String,
    val country: String,
    val previewUrl: String
) : Parcelable {}
package com.example.playlistmarket.root.domain.model

import android.os.Parcelable

@kotlinx.parcelize.Parcelize
data class Track(
    val trackId: Int,
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
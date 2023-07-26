package com.example.playlistmarket.features.medialibrary.domain

import android.net.Uri

data class PlaylistScreenModel(
    val artwork: Uri?,
    val title: String,
    val description: String,
    val totalDuration: String,
    val trackQuantity: String
)
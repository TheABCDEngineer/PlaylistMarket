package com.example.playlistmarket.features.medialibrary.domain

import android.net.Uri

data class PlaylistRecyclerModel(
    val id: Int,
    val artwork: Uri?,
    val title: String,
    val tracksQuantity: String
)
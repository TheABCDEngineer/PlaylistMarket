package com.example.playlistmarket.root.domain.model

data class Playlist(
    val title: String,
    val artworkPath: String,
    val description: String,
    var trackQuantity: Int = 0,
    val id: Int = 0
)
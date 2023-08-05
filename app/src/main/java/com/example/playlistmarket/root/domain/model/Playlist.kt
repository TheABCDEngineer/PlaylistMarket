package com.example.playlistmarket.root.domain.model

data class Playlist(
    var title: String,
    var description: String,
    var trackQuantity: Int = 0,
    val id: Int = 0
)
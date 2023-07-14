package com.example.playlistmarket.root.domain.model

data class Playlist(
    val title: String,
    val artworkPath: String,
    val description: String
) {
    val id = this.hashCode()
}
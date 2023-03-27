package com.example.playlistmarket.base.domain.repository

import com.example.playlistmarket.base.domain.model.Track

interface PlaylistRepository {
    fun loadTitlesList(): Array<String>
    fun saveNewTitle(title: String)
    fun deleteTitle(title: String)
    fun checkTitle(title: String): Boolean

    fun loadPlaylist(playlistTitle: String): Array<Track>
    fun savePlaylist(playlistTitle: String, tracks: ArrayList<Track>)
    fun deletePlaylist(playlistTitle: String)
}

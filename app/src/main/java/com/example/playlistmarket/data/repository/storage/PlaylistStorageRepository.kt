package com.example.playlistmarket.data.repository.storage

import com.example.playlistmarket.domain.models.Track

interface PlaylistStorageRepository {
    fun loadTitlesList(): Array<String>
    fun saveNewTitle(title: String)
    fun deleteTitle(title: String)
    fun checkTitle(title: String): Boolean

    fun loadPlaylist(playlistTitle: String): Array<Track>
    fun savePlaylist(playlistTitle: String, tracks: ArrayList<Track>)
    fun deletePlaylist(playlistTitle: String)
}

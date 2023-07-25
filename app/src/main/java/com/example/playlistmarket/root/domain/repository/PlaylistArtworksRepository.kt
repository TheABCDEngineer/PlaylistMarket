package com.example.playlistmarket.root.domain.repository

import android.net.Uri

interface PlaylistArtworksRepository {
    suspend fun saveArtwork(uri: Uri, fileName: String)
    suspend fun loadArtwork(fileName: String): Uri?
}
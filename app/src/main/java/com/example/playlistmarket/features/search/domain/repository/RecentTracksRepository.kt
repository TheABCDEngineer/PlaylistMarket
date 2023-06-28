package com.example.playlistmarket.features.search.domain.repository

import com.example.playlistmarket.root.domain.model.Track

interface RecentTracksRepository {
    fun loadTracks(): ArrayList<Track>
    fun saveTracks(tracks: ArrayList<Track>)
    fun deleteTracks()
}

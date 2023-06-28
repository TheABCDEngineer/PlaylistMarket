package com.example.playlistmarket.features.search.domain

import com.example.playlistmarket.features.search.domain.repository.RecentTracksRepository
import com.example.playlistmarket.root.domain.model.Track

class RecentTracks(
    private val repository: RecentTracksRepository,
) {
    private val limit = 10
    val items get() = repository.loadTracks()

    fun addTrack(track: Track) {
        val tracks = this.items
        tracks.remove(track)
        tracks.add(0, track)
        if (tracks.size > limit) tracks.removeAt(limit)
        repository.saveTracks(tracks)
    }

    fun clear() {
        repository.saveTracks(ArrayList())
    }
}
package com.example.playlistmarket.features.search.data

import android.content.SharedPreferences
import com.example.playlistmarket.App
import com.example.playlistmarket.features.search.domain.repository.RecentTracksRepository
import com.example.playlistmarket.root.data.sharedPreferences.deleteFromFileOnKey
import com.example.playlistmarket.root.data.sharedPreferences.loadListFromFileOnKey
import com.example.playlistmarket.root.data.sharedPreferences.saveToFileOnKey
import com.example.playlistmarket.root.domain.model.Track

class RecentTracksRepositoryImplSharedPreferences(
    private val file: SharedPreferences
) : RecentTracksRepository {

    override fun loadTracks(): ArrayList<Track> {
        val tracks = ArrayList<Track>()
        tracks.addAll(
            loadListFromFileOnKey(file, App.RECENT_TRACKS_KEY, Array<Track>::class.java)
        )
        return tracks
    }

    override fun saveTracks(tracks: ArrayList<Track>) {
        saveToFileOnKey(file, App.RECENT_TRACKS_KEY, tracks)
    }

    override fun deleteTracks() {
        deleteFromFileOnKey(file, App.RECENT_TRACKS_KEY)
    }
}
package com.example.playlistmarket.features.main.data.sharedPreferences

import android.content.SharedPreferences
import com.example.playlistmarket.App
import com.example.playlistmarket.creator.enums.PlaylistHandle
import com.example.playlistmarket.features.main.domain.repository.PlaylistRepository
import com.example.playlistmarket.features.main.domain.model.Track

class PlaylistRepositoryImpSharedPreferences(
    private val file: SharedPreferences
) : PlaylistRepository {

    override fun loadTitlesList(): Array<String> {
        return loadListFromFileOnKey(file, App.PLAYLIST_TITLES_KEY, Array<String>::class.java)
    }

    override fun saveNewTitle(title: String) {
        if (checkTitle(title)) return
        manageTitle(title, PlaylistHandle.ADD_TRACK)
    }

    override fun deleteTitle(title: String) {
        manageTitle(title, PlaylistHandle.DELETE_TRACK)
    }

    override fun checkTitle(title: String): Boolean {
        if (
            title == App.FAVORITES_LIST_KEY ||
            title == App.RECENT_TRACKS_LIST_KEY
        ) return true

        val titles = ArrayList<String>()
        titles.addAll(loadTitlesList())

        for (i in titles) {
            if (i == title) {
                return true
            }
        }
        return false
    }

    override fun loadPlaylist(playlistTitle: String): Array<Track> {
        return loadListFromFileOnKey(file, playlistTitle, Array<Track>::class.java)
    }

    override fun savePlaylist(playlistTitle: String, tracks: ArrayList<Track>) {
        saveListToFileOnKey(file, playlistTitle, tracks)
        saveNewTitle(playlistTitle)
    }

    override fun deletePlaylist(playlistTitle: String) {
        deleteFromFileOnKey(file, playlistTitle)
        deleteTitle(playlistTitle)
    }

    private fun manageTitle(title: String, event: PlaylistHandle) {
        val titles = ArrayList<String>()
        titles.addAll(loadTitlesList())
        if (event == PlaylistHandle.ADD_TRACK) titles.add(title)
        if (event == PlaylistHandle.DELETE_TRACK) titles.remove(title)
        saveListToFileOnKey(file, App.PLAYLIST_TITLES_KEY, titles)
    }
}
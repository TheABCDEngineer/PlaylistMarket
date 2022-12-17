package com.example.playlistmarket.player

import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.Track
import com.example.playlistmarket.getSharePreferences
import com.google.gson.Gson

class TrackHandle(val track: Track) {
    var isTrackPlaying: Boolean = false
    var isTrackAtPlaylist: Boolean = false
    var isTrackAtFavorites: Boolean = false

    private val file = getSharePreferences()

    //private var playList = ArrayList<Track>()
    //private var favoritesList = ArrayList<Track>()

    init {
        //playList.addAll(loadListFromFile(App.appContext.getString(R.string.playlist_key)))
        //favoritesList.addAll(loadListFromFile(App.appContext.getString(R.string.favorites_list_key)))
        //isTrackAtPlaylist = setStatusOfTrackIsInTheList(playList)
        //isTrackAtFavorites = setStatusOfTrackIsInTheList(favoritesList)
    }

    private fun loadListFromFile(key: String): Array<Track> {
        val json: String = file.getString(key, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    private fun saveListToFile(key: String, list: ArrayList<Track>) {
        val json = Gson().toJson(list)
        file.edit()
            .putString(key, json)
            .apply()
    }

    private fun setStatusOfTrackIsInTheList (list: ArrayList<Track>) : Boolean {
        for (i in list) {
            if (track.trackId == i.trackId) return true
        }
        return false
    }

    fun addTrackToPlaylist () {

    }
}
package com.example.playlistmarket.controller.search

import com.example.playlistmarket.controller.search.enums.SearchScreenState
import com.example.playlistmarket.controller.search.recycler.SearchTrackAdapter

interface SearchView {
    fun updateScreenState(state: SearchScreenState)
    fun updateTrackFeed(adapter: SearchTrackAdapter)
}
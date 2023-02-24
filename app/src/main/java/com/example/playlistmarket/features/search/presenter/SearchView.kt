package com.example.playlistmarket.features.search.presenter

import com.example.playlistmarket.features.search.presenter.enums.SearchScreenState
import com.example.playlistmarket.features.search.ui.recycler.SearchTrackAdapter

interface SearchView {
    fun updateScreenState(state: SearchScreenState)
    fun updateTrackFeed(adapter: SearchTrackAdapter)
}
package com.example.playlistmarket.features.search.presenter

import com.example.playlistmarket.creator.Presenter
import com.example.playlistmarket.features.search.presenter.enums.FunctionalButtonMode
import com.example.playlistmarket.features.search.presenter.enums.QueryError
import com.example.playlistmarket.features.search.presenter.enums.SearchScreenState
import com.example.playlistmarket.data.repository.QueryRepository
import com.example.playlistmarket.features.search.ui.recycler.SearchTrackAdapter
import com.example.playlistmarket.App
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.domain.models.Playlist
import com.example.playlistmarket.domain.models.Track

class SearchPresenter(
    var view: SearchView,
    private val searchQuery: QueryRepository,
    private val historyPlaylist: Playlist
) : Presenter(), Observer {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    var currentScreenState: SearchScreenState? = null
    private val queryTrackList = ArrayList<Track>()

    private val queryAdapter = SearchTrackAdapter(queryTrackList)
    private val historyAdapter = SearchTrackAdapter(historyPlaylist.items)
    private var searchRunnable: Runnable? = null
    private var previousRequestText = ""

    init {
        searchQuery.addObserver(this)
        queryAdapter.addObserver(historyPlaylist)
        historyAdapter.addObserver(historyPlaylist)
    }

    override fun onViewResume() {
        super.onViewResume()
        if (currentScreenState == null) {
            if (historyPlaylist.items.isNotEmpty()) showHistoryFeed()
            if (historyPlaylist.items.isEmpty()) view.updateScreenState(SearchScreenState.NEWBORN)
            return
        }
        when (currentScreenState!!) {
            SearchScreenState.HISTORY -> showHistoryFeed()
            SearchScreenState.QUERY_RESULTS -> showQueryResultsFeed()
            else -> view.updateScreenState(currentScreenState!!)
        }
    }

    fun onUserRequestTextChange(text: String) {
        if (
            text == previousRequestText ||
            text == ""
        ) return
        previousRequestText = text

        if (searchRunnable != null) App.mainHandler.removeCallbacks(searchRunnable!!)
        searchRunnable = Runnable {
            view.updateScreenState(SearchScreenState.SEARCHING)
            searchQuery.executeQuery(text)
        }
        App.mainHandler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
    }

    fun onFunctionalButtonPressed(mode: FunctionalButtonMode) {
        when (mode) {
            FunctionalButtonMode.REFRESH -> App.mainHandler.post(searchRunnable!!)
            FunctionalButtonMode.CLEAR_HISTORY -> clearHistory()
        }
    }

    private fun clearHistory() {
        historyPlaylist.clear()
        view.updateScreenState(SearchScreenState.NEWBORN)
    }

    private fun showHistoryFeed() {
        view.updateTrackFeed(historyAdapter)
        view.updateScreenState(SearchScreenState.HISTORY)
    }

    private fun prepareQueryResultsFeed(trackList: ArrayList<Track>) {
        queryTrackList.clear()
        queryTrackList.addAll(trackList)
        view.updateTrackFeed(queryAdapter)
    }

    private fun showQueryResultsFeed() {
        view.updateTrackFeed(queryAdapter)
        view.updateScreenState(SearchScreenState.QUERY_RESULTS)
    }

    override fun <S, T> notifyObserver(event: S?, data: T?) {
        if ((data as ArrayList<Track>).isNotEmpty()) prepareQueryResultsFeed(data)
        when (event as QueryError) {
            QueryError.NO_ERRORS -> view.updateScreenState(SearchScreenState.QUERY_RESULTS)
            QueryError.NO_RESULTS -> view.updateScreenState(SearchScreenState.NO_RESULTS)
            QueryError.SOMETHING_WENT_WRONG -> view.updateScreenState(SearchScreenState.SOMETHING_WENT_WRONG)
            QueryError.NO_INTERNET_CONNECTION -> view.updateScreenState(SearchScreenState.NO_INTERNET_CONNECTION)
        }
    }

    override fun implementPresenterDestroyingMethod(): () -> Unit = {
        App.serviceApi = null
        Creator.searchPresenter = null
    }
}
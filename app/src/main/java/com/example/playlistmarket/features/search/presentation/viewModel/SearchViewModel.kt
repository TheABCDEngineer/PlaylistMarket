package com.example.playlistmarket.features.search.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmarket.features.search.domain.enums.FunctionalButtonMode
import com.example.playlistmarket.features.search.domain.enums.QueryError
import com.example.playlistmarket.features.search.domain.enums.SearchScreenState
import com.example.playlistmarket.features.search.domain.interactors.QueryInteractor
import com.example.playlistmarket.features.search.presentation.ui.recycler.SearchTrackAdapter
import com.example.playlistmarket.App
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.features.main.domain.entity.Playlist
import com.example.playlistmarket.features.main.domain.model.Track

class SearchViewModel(
    private val queryExecutor: QueryInteractor,
    private val historyPlaylist: Playlist
) : ViewModel(), Observer {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer { Creator.createSearchViewModel() }
        }
    }

    private val screenStateLiveData = MutableLiveData<SearchScreenState>()
    fun observeScreenState(): LiveData<SearchScreenState> = screenStateLiveData

    private val trackFeedLiveData = MutableLiveData<SearchTrackAdapter>()
    fun observeTrackFeedState(): LiveData<SearchTrackAdapter> = trackFeedLiveData

    private val queryTrackList = ArrayList<Track>()
    private val queryAdapter = SearchTrackAdapter(queryTrackList)
    private val historyAdapter = SearchTrackAdapter(historyPlaylist.items)
    private var searchRunnable: Runnable = Runnable {}
    private var previousRequestText = ""

    init {
        queryExecutor.addObserver(this)
        queryAdapter.addObserver(historyPlaylist)
        historyAdapter.addObserver(historyPlaylist)
        setStartScreen()
    }

    fun setStartScreen() {
        if (historyPlaylist.items.isNotEmpty()) {
            trackFeedLiveData.postValue(historyAdapter)
            screenStateLiveData.postValue(SearchScreenState.HISTORY)
        } else {
            screenStateLiveData.postValue(SearchScreenState.NEWBORN)
        }
    }

    fun onUserRequestTextChange(text: String) {
        if (
            text == previousRequestText ||
            text == ""
        ) return
        previousRequestText = text

        App.mainHandler.removeCallbacks(searchRunnable)

        searchRunnable = Runnable {
            screenStateLiveData.postValue(SearchScreenState.SEARCHING)
            queryExecutor.executeQuery(text)
        }
        App.mainHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun onFunctionalButtonPressed(mode: FunctionalButtonMode) {
        when (mode) {
            FunctionalButtonMode.REFRESH -> App.mainHandler.post(searchRunnable)
            FunctionalButtonMode.CLEAR_HISTORY -> clearHistory()
        }
    }

    private fun clearHistory() {
        historyPlaylist.clear()
        screenStateLiveData.postValue(SearchScreenState.NEWBORN)
    }

    private fun setQueryResultsFeed(trackList: ArrayList<Track>) {
        queryTrackList.clear()
        queryTrackList.addAll(trackList)
        trackFeedLiveData.postValue(queryAdapter)
    }

    override fun <S, T> notifyObserver(event: S?, data: T?) {
        if ((data as ArrayList<Track>).isNotEmpty()) setQueryResultsFeed(data)
        val searchScreenState = when (event as QueryError) {
            QueryError.NO_ERRORS -> SearchScreenState.QUERY_RESULTS
            QueryError.NO_RESULTS -> SearchScreenState.NO_RESULTS
            QueryError.SOMETHING_WENT_WRONG -> SearchScreenState.SOMETHING_WENT_WRONG
            QueryError.NO_INTERNET_CONNECTION -> SearchScreenState.NO_INTERNET_CONNECTION
        }
        screenStateLiveData.postValue(searchScreenState)
    }

    override fun onCleared() {
        App.mainHandler.removeCallbacks(searchRunnable)
        super.onCleared()
    }

    fun updateHistoryState() {
        if (screenStateLiveData.value!! == SearchScreenState.HISTORY) {
            trackFeedLiveData.postValue(historyAdapter)
        }
    }
}
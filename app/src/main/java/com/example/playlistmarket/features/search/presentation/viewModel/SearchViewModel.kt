package com.example.playlistmarket.features.search.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.features.search.domain.enums.FunctionalButtonMode
import com.example.playlistmarket.features.search.domain.enums.QueryError
import com.example.playlistmarket.features.search.domain.enums.SearchScreenState
import com.example.playlistmarket.features.search.domain.interactors.QueryInteractor
import com.example.playlistmarket.features.search.presentation.ui.recyclerView.SearchTrackAdapter
import com.example.playlistmarket.App.Companion.RECENT_TRACKS_LIST_KEY
import com.example.playlistmarket.App.Companion.CLICK_DEBOUNCE_DELAY
import com.example.playlistmarket.features.player.presentation.Player
import com.example.playlistmarket.root.debounce
import com.example.playlistmarket.root.observe.Observer
import com.example.playlistmarket.root.domain.PlaylistCreator
import com.example.playlistmarket.root.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val queryExecutor: QueryInteractor,
    playlistCreator: PlaylistCreator
) : ViewModel(), Observer {

    private val screenStateLiveData = MutableLiveData<SearchScreenState>()
    fun observeScreenState(): LiveData<SearchScreenState> = screenStateLiveData

    private val trackFeedLiveData = MutableLiveData<SearchTrackAdapter>()
    fun observeTrackFeedState(): LiveData<SearchTrackAdapter> = trackFeedLiveData

    private val historyPlaylist = playlistCreator.createPlaylist(RECENT_TRACKS_LIST_KEY, 10)
    private val queryTrackList = ArrayList<Track>()

    private val onAdapterItemClickedAction: (Track) -> Unit
        get() = debounce(CLICK_DEBOUNCE_DELAY, viewModelScope) { track: Track ->
            Player.start(track)
            historyPlaylist.addTrack(track)
        }
    private val queryAdapter = SearchTrackAdapter(queryTrackList, onAdapterItemClickedAction)
    private val historyAdapter = SearchTrackAdapter(historyPlaylist.items, onAdapterItemClickedAction)
    private var searchJob: Job? = null
    private var previousRequestText = ""

    init {
        queryExecutor.addObserver(this)
        setStartScreen()
    }

    override fun <S, T> notifyObserver(event: S?, data: T?) {
        if (data != null) {
            if ((data as ArrayList<Track>).isNotEmpty()) setQueryResultsFeed(data)
        }
        val searchScreenState = when (event as QueryError) {
            QueryError.NO_ERRORS -> SearchScreenState.QUERY_RESULTS
            QueryError.NO_RESULTS -> SearchScreenState.NO_RESULTS
            QueryError.SOMETHING_WENT_WRONG -> SearchScreenState.SOMETHING_WENT_WRONG
            QueryError.NO_INTERNET_CONNECTION -> SearchScreenState.NO_INTERNET_CONNECTION
        }
        screenStateLiveData.postValue(searchScreenState)
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
        runSearching(SEARCH_DEBOUNCE_DELAY,text)
    }

    fun onFunctionalButtonPressed(mode: FunctionalButtonMode) {
        when (mode) {
            FunctionalButtonMode.REFRESH -> runSearching(0L,previousRequestText)
            FunctionalButtonMode.CLEAR_HISTORY -> clearHistory()
        }
    }

    fun updateHistoryState() {
        if (screenStateLiveData.value!! == SearchScreenState.HISTORY) {
            trackFeedLiveData.postValue(historyAdapter)
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

    private fun runSearching(delay: Long, parameter: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(delay)
            screenStateLiveData.postValue(SearchScreenState.SEARCHING)
            queryExecutor.executeQuery(parameter)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
package com.example.playlistmarket.features.search.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.features.search.domain.RecentTracks
import com.example.playlistmarket.features.search.domain.enums.FunctionalButtonMode
import com.example.playlistmarket.features.search.domain.enums.QueryError
import com.example.playlistmarket.features.search.domain.enums.SearchScreenState
import com.example.playlistmarket.features.search.domain.model.ResponseModel
import com.example.playlistmarket.features.search.domain.repository.RecentTracksRepository
import com.example.playlistmarket.features.search.domain.repository.SearchTracksRepository
import com.example.playlistmarket.root.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchTracksRepository: SearchTracksRepository,
    private val recentTracksRepository: RecentTracksRepository
) : ViewModel() {

    private val screenStateLiveData = MutableLiveData<SearchScreenState>()
    fun observeScreenState(): LiveData<SearchScreenState> = screenStateLiveData

    private val trackFeedLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeTrackFeedState(): LiveData<ArrayList<Track>> = trackFeedLiveData

    private val recentTracks = RecentTracks(recentTracksRepository)
    private val queryTrackList = ArrayList<Track>()
    private var searchJob: Job? = null
    private var previousRequestText = ""

    init {
        setStartScreen()
    }

    fun setStartScreen() {
        val recentTrackList = recentTracks.items
        if (recentTrackList.isNotEmpty()) {
            trackFeedLiveData.postValue(recentTrackList)
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
        runSearching(SEARCH_DEBOUNCE_DELAY, text)
    }

    fun onFunctionalButtonPressed(mode: FunctionalButtonMode) {
        when (mode) {
            FunctionalButtonMode.REFRESH -> runSearching(0L, previousRequestText)
            FunctionalButtonMode.CLEAR_HISTORY -> clearHistory()
        }
    }

    fun updateHistoryState() {
        if (screenStateLiveData.value!! == SearchScreenState.HISTORY) {
            trackFeedLiveData.postValue(recentTracks.items)
        }
    }

    fun onTrackSelected(track: Track) {
        recentTracks.addTrack(track)
    }

    private fun clearHistory() {
        recentTracks.clear()
        screenStateLiveData.postValue(SearchScreenState.NEWBORN)
    }

    private fun setQueryResultsFeed(trackList: ArrayList<Track>) {
        queryTrackList.clear()
        queryTrackList.addAll(trackList)
        trackFeedLiveData.postValue(queryTrackList)
    }

    private fun runSearching(delay: Long, parameter: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(delay)
            screenStateLiveData.postValue(SearchScreenState.SEARCHING)
            searchTracksRepository
                .searchTracks(parameter)
                .collect {
                    handleSearchingResponse(it)
                }
            searchJob = null
        }
    }

    private fun handleSearchingResponse(response: ResponseModel) {
        if (response.resultTrackList.isNotEmpty()) setQueryResultsFeed(response.resultTrackList)

        val searchScreenState = when (response.error) {
            QueryError.NO_ERRORS -> SearchScreenState.QUERY_RESULTS
            QueryError.NO_RESULTS -> SearchScreenState.NO_RESULTS
            QueryError.SOMETHING_WENT_WRONG -> SearchScreenState.SOMETHING_WENT_WRONG
            QueryError.NO_INTERNET_CONNECTION -> SearchScreenState.NO_INTERNET_CONNECTION
        }
        screenStateLiveData.postValue(searchScreenState)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
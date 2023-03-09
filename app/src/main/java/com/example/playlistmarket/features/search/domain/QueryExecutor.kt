package com.example.playlistmarket.features.search.domain

import com.example.playlistmarket.features.search.viewModel.enums.QueryError
import com.example.playlistmarket.features.search.viewModel.interactors.QueryInteractor
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.features.search.data.dto.TracksResponse
import com.example.playlistmarket.domain.dataConverter.TrackConverter
import com.example.playlistmarket.domain.model.Track
import com.example.playlistmarket.features.search.data.dto.TracksRequest
import com.example.playlistmarket.features.search.data.network.NetworkClient
import com.example.playlistmarket.features.search.data.dto.Response

class QueryExecutor(
    private val networkClient: NetworkClient
) : QueryInteractor {

    private lateinit var observer: Observer
    private val resultList = ArrayList<Track>()
    private var errorStatus: QueryError = QueryError.NO_ERRORS

    init {
        networkClient.callback = { executeCallback(it) }
    }

    override fun addObserver(observer: Observer) {
        this.observer = observer
    }

    private fun executeCallback(response: Response) {
        errorStatus = when (response.responseCode) {
            in 200..299 -> QueryError.NO_ERRORS
            in 400..499 -> QueryError.NO_INTERNET_CONNECTION
            else -> QueryError.SOMETHING_WENT_WRONG
        }
        resultList.clear()
        if (errorStatus == QueryError.NO_ERRORS) {
            resultList.addAll(TrackConverter.convertDtoToTrackModel((response as TracksResponse).results))
            if (resultList.isEmpty()) errorStatus = QueryError.NO_RESULTS
        }
        observer.notifyObserver(errorStatus, resultList)
    }

    override fun executeQuery(queryValue: String) {
        networkClient.executeRequest(TracksRequest(queryValue))
    }
}
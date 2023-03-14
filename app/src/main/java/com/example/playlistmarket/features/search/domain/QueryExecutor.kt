package com.example.playlistmarket.features.search.domain

import com.example.playlistmarket.features.search.domain.interactors.QueryInteractor
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.features.search.domain.repository.NetworkClientRepository

class QueryExecutor(
    private val networkClient: NetworkClientRepository
) : QueryInteractor {

    private lateinit var observer: Observer

    init {
        networkClient.callback = {
            observer.notifyObserver(
                it.error,
                it.resultTrackList
            )
        }
    }

    override fun addObserver(observer: Observer) {
        this.observer = observer
    }

    override fun executeQuery(queryValue: String) {
        networkClient.executeRequest(queryValue)
    }
}